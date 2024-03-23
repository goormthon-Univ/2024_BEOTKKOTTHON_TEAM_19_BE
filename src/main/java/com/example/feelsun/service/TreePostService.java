package com.example.feelsun.service;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import com.example.feelsun.config.errors.exception.Exception401;
import com.example.feelsun.config.errors.exception.Exception404;
import com.example.feelsun.config.s3.S3UploadService;
import com.example.feelsun.domain.Tree;
import com.example.feelsun.domain.TreeImage;
import com.example.feelsun.domain.TreePost;
import com.example.feelsun.domain.User;
import com.example.feelsun.repository.TreeImageJpaRepository;
import com.example.feelsun.repository.TreeJpaRepository;
import com.example.feelsun.repository.TreePostJpaRepository;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.TreePostRequest;
import com.example.feelsun.response.TreePostResponse.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TreePostService {

    // 기본적으로 오르는 경험치
    public static final int EXPERIENCE = 1;

    private final S3UploadService s3UploadService;
    private final UserJpaRepository userJpaRepository;
    private final TreeJpaRepository treeJpaRepository;
    private final TreePostJpaRepository treePostJpaRepository;
    private final TreeImageJpaRepository treeImageJpaRepository;

    @Transactional
    public Integer userTreePostAll(PrincipalUserDetails principalUserDetails) {
        User user = principalUserDetails.getUser();
        List<TreePost> treePosts = treePostJpaRepository.findAllTreePostByUserId(user);
        Integer userAllTreePost = treePosts.size();
        return userAllTreePost;
    }

    @Transactional
    public String uploadTreePostImage(MultipartFile file, PrincipalUserDetails principalUserDetails) throws IOException {
        // 인증
        validateUser(principalUserDetails);

        // 이미지 업로드
        return s3UploadService.uploadSingleFile(file);
    }

    @Transactional
    public void createTreePost(TreePostRequest.TreePostCreateRequest requestDTO, Integer treeId, PrincipalUserDetails principalUserDetails) {
        // 인증
        User user = validateUser(principalUserDetails);

        // 나무 데이터 가져오기
        Tree tree = treeJpaRepository.findById(treeId)
                .orElseThrow(() -> new Exception404("나무를 찾을 수 없습니다."));

        // 나무 데이터에서 가져온 값과 현재 유저 아이디 값이 다를 경우 예외처리
        if (!tree.getUser().getId().equals(user.getId())) {
            throw new Exception401("인증되지 않은 사용자입니다.");
        }

        // 오늘 날짜의 시작 시간과 종료 시간을 계산
        LocalDateTime startOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime startOfNextDay = startOfToday.plusDays(1);

        // 수정된 메서드를 호출
        int todayCount = treePostJpaRepository.countByTreeIdAndCreatedAt(treeId, startOfToday, startOfNextDay);

        if (todayCount >= 3) {
            throw new Exception401("하루에 인증글은 3개까지만 작성할 수 있습니다.");
        }

        // 게시물 생성
        TreePost treePost = TreePost.builder()
                .user(user)
                .tree(tree)
                .content(requestDTO.getContent())
                .imageUrl(requestDTO.getImageUrl())
                .build();

        // tree 의 경험치 업데이트
        if (!tree.isCertification()) {
            tree.updateExperience(EXPERIENCE);

            // 다음 나무로 업그레이드할 수 있는 경우
            List<TreeImage> treeImages = treeImageJpaRepository.findAll();

            tree.upgradeTree(treeImages);
        }

        // 게시물 저장
        treePostJpaRepository.save(treePost);

    }

    private User validateUser(PrincipalUserDetails principalUserDetails) {
        return userJpaRepository.findByUsername(principalUserDetails.getUsername())
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다."));
    }

    public TreePostCountResponse getTreePostCounts(Integer treeId, PrincipalUserDetails principalUserDetails) {
        // 인증
        User user = validateUser(principalUserDetails);

        // 나무 데이터 가져오기
        Tree tree = treeJpaRepository.findById(treeId)
                .orElseThrow(() -> new Exception404("나무를 찾을 수 없습니다."));

        // 전체 인증글 수 조회
        int totalCount = treePostJpaRepository.countByTreeId(treeId);

        // 오늘 날짜의 시작 시간과 종료 시간을 계산
        LocalDateTime startOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime startOfNextDay = startOfToday.plusDays(1);

        // 수정된 메서드를 호출
        int todayCount = treePostJpaRepository.countByTreeIdAndCreatedAt(treeId, startOfToday, startOfNextDay);
        return new TreePostCountResponse(todayCount, totalCount);
    }
}