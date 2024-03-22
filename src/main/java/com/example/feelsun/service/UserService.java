package com.example.feelsun.service;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import com.example.feelsun.config.errors.exception.Exception400;
import com.example.feelsun.config.errors.exception.Exception404;
import com.example.feelsun.config.jwt.JwtProvider;
import com.example.feelsun.config.jwt.refreshToken.RefreshTokenService;
import com.example.feelsun.config.redis.RedisService;
import com.example.feelsun.domain.Tree;
import com.example.feelsun.domain.TreePost;
import com.example.feelsun.domain.User;
import com.example.feelsun.domain.UserEnum;
import com.example.feelsun.repository.TreeJpaRepository;
import com.example.feelsun.repository.TreePostJpaRepository;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.UserRequest.*;
import com.example.feelsun.response.UserResponse.*;
import com.example.feelsun.response.UserResponse.UserShareResponse.UserShareTreeResponse;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final JwtProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final TreeJpaRepository treeJpaRepository;
    private final RedisService redisService;
    private final TreePostJpaRepository treePostJpaRepository;

    @Transactional
    public void signup(UserSignUpRequest requestDTO) {
        // 비밀번호 암호화
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // 이미 있는 아이디일경우 예외처리
        userJpaRepository.findByUsername(requestDTO.getUsername()).ifPresent(user -> {
            throw new Exception400(null, "이미 존재하는 아이디입니다.");
        });
        // 이미 있는 닉네임일경우 예외처리
        userJpaRepository.findByNickname(requestDTO.getNickname()).ifPresent(user -> {
            throw new Exception400(null, "이미 존재하는 닉네임입니다.");
        });
        // 저장
        userJpaRepository.save(requestDTO.toEntity(UserEnum.USER));
    }

    @Transactional
    public UserLoginResponseWithToken login(UserLoginRequest requestDTO) {
        User user = userJpaRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new Exception400(null, "아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new Exception400(null, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = JwtProvider.TOKEN_PREFIX + tokenProvider.createToken(user.getId().toString(), user.getRole().toString(), user.getNickname());

        // 리프래쉬 토큰 생성
        String refreshToken = tokenProvider.createRefreshToken(user.getId().toString());

        // 리프래쉬 토큰을 Redis에 저장
        refreshTokenService.saveRefreshToken(user.getId().toString(), refreshToken);

        UserLoginResponse loginResponseDTO = new UserLoginResponse(user.getId(), user.getUsername(), user.getNickname());

        return new UserLoginResponseWithToken(loginResponseDTO, accessToken, refreshToken);
    }

    public boolean checkUsername(UserCheckUsernameRequest requestDTO) {
        return userJpaRepository.existsByUsername(requestDTO.getUsername());
    }

    public boolean checkNickname(UserCheckNicknameRequest requestDTO) {
        return userJpaRepository.existsByNickname(requestDTO.getNickname());
    }

    @Transactional
    public UserLoginResponseWithToken generateToken(UserSignUpRequest requestDTO) {
        User user = userJpaRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new Exception400(null, "회원가입에 실패했습니다."));

        String accessToken = JwtProvider.TOKEN_PREFIX + tokenProvider.createToken(user.getId().toString(), user.getRole().toString(), user.getNickname());

        // 리프래쉬 토큰 생성
        String refreshToken = tokenProvider.createRefreshToken(user.getId().toString());

        // 리프래쉬 토큰을 Redis에 저장
        refreshTokenService.saveRefreshToken(user.getId().toString(), refreshToken);

        UserLoginResponse loginResponseDTO = new UserLoginResponse(user.getId(), user.getUsername(), user.getNickname());

        return new UserLoginResponseWithToken(loginResponseDTO, accessToken, refreshToken);
    }
    /**
     * 어떻게 랜덤하게 사용자들의 트리 정보를 가져올 수 있을까?
     * 1. 중복되지 않고 랜덤하게 나무 정보를 가져온다.
     **/

    @Transactional
    public List<UserTreeListResponse> getUserTreeList(PrincipalUserDetails principalUserDetails, int page, int size) {
        // 인증
        User user = validateUser(principalUserDetails);

        // redis 에 저장해둔 나무 정보 리스트 가져오기
        Set<Integer> excludedIds = redisService.getExcludedIds(String.valueOf(user.getId()));

        Specification<Tree> spec = (root, query, cb) -> {
            Predicate notInExcludedIds = cb.not(root.get("id").in(excludedIds));
            query.orderBy(cb.desc(cb.function("RAND", Double.class))); // 랜덤 정렬
            return notInExcludedIds;
        };

        Pageable pageable = PageRequest.of(page, size);

        Page<Tree> trees = treeJpaRepository.findAll(spec, pageable);

        // 조회된 나무 ID 목록을 Redis에 저장
        Set<Integer> treeIds = trees.
                getContent()
                .stream()
                .map(Tree::getId)
                .collect(Collectors.toSet());

        if (!treeIds.isEmpty()) {
            redisService.addExcludedIds(String.valueOf(user.getId()), treeIds);
        }

        return trees
                .getContent()
                .stream()
                .map(tree -> new UserTreeListResponse(tree.getUser().getId(), tree.getId(), tree.getName(), tree.getImageUrl()))
                .toList();

    }

    private User validateUser(PrincipalUserDetails principalUserDetails) {
       return userJpaRepository.findByUsername(principalUserDetails.getUser().getUsername())
                .orElseThrow(() -> new Exception404("사용자 정보를 찾을 수 없습니다."));
    }

    /**
     * 나무를 클릭했을 때, 인증글 목록 리스트 보여주기
     * **/
    public List<UserTreeDetailResponse> getUserTreeDetail(PrincipalUserDetails principalUserDetails, Integer treeId, int page, int size) {
        // 인증
        validateUser(principalUserDetails);

        // 인증글 목록 조회해서 가져오기
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<TreePost> treePosts = treePostJpaRepository.findAllByTreeId(treeId, pageable);

        // 페이징 처리해서 가져온 인증글 목록을 dto 로 만들기
        return treePosts
                .getContent()
                .stream()
                .map(treePost -> new UserTreeDetailResponse(treePost.getId(), treePost.getImageUrl(), treePost.getContent(), treePost.getCreatedAt()))
                .toList();
    }

    /**
     * 나의 히스토리 목록 보기 ( 최신순 )
     * **/
    @Transactional
    public List<UserHistoryListResponse> getUserHistories(PrincipalUserDetails principalUserDetails, int page, int size) {
        // 인증
        User user = validateUser(principalUserDetails);

        // redis 에 존재하는 자신의 나무 id 목록 지우기
        redisService.deleteValues(String.valueOf(user.getId()));

        // 나의 히스토리 목록 조회해서 가져오기
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<TreePost> treePosts = treePostJpaRepository.findAllByUserId(user.getId(), pageable);

        // 페이징 처리해서 가져온 나의 히스토리 목록을 dto 로 만들기
        return treePosts
                .getContent()
                .stream()
                .map(treePost -> new UserHistoryListResponse(treePost.getId(), treePost.getImageUrl(), treePost.getContent(), treePost.getCreatedAt()))
                .toList();
    }

    /**
     * 공유 버튼을 눌렀을 때 보여주는 API
     * 1. 나무 목록 가져오기
     * 2. DTO 만들기
     * **/
    public UserShareResponse getUserShare(Integer userId) {
        User user = userJpaRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new Exception404("사용자 정보를 찾을 수 없습니다."));

        List<Tree> trees = treeJpaRepository.findAllByUserId(userId);

        List<UserShareTreeResponse> userShareTreeResponses = trees
                .stream()
                .map(tree -> new UserShareTreeResponse(tree.getId(), tree.getName(), tree.getImageUrl()))
                .toList();

        return new UserShareResponse(user.getNickname(), userShareTreeResponses);

    }
}
