package com.example.feelsun.service;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import com.example.feelsun.config.errors.exception.Exception401;
import com.example.feelsun.config.errors.exception.Exception403;
import com.example.feelsun.config.errors.exception.Exception404;
import com.example.feelsun.config.s3.S3UploadService;
import com.example.feelsun.domain.Tree;
import com.example.feelsun.domain.TreePost;
import com.example.feelsun.domain.User;
import com.example.feelsun.repository.TreeJpaRepository;
import com.example.feelsun.repository.TreePostJpaRepository;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.TreePostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Transactional
@Service
public class TreePostService {

    private final S3UploadService s3UploadService;
    private final UserJpaRepository userJpaRepository;
    private final TreeJpaRepository treeJpaRepository;
    private final TreePostJpaRepository treePostJpaRepository;

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

        // 게시물 생성
        TreePost treePost = TreePost.builder()
                .user(user)
                .tree(tree)
                .content(requestDTO.getContent())
                .imageUrl(requestDTO.getImageUrl())
                .build();

        // 게시물 저장
        treePostJpaRepository.save(treePost);

    }

    private User validateUser(PrincipalUserDetails principalUserDetails) {
        return userJpaRepository.findByUsername(principalUserDetails.getUsername())
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다."));
    }

    public int getTreePostCounts(Integer treeId, PrincipalUserDetails principalUserDetails) {
        // 인증
        User user = validateUser(principalUserDetails);

        // 나무 데이터 가져오기
        Tree tree = treeJpaRepository.findById(treeId)
                .orElseThrow(() -> new Exception404("나무를 찾을 수 없습니다."));

        // 나무 데이터에서 가져온 값과 현재 유저 아이디 값이 다를 경우 예외처리
        if (!tree.getUser().getId().equals(user.getId())) {
            throw new Exception401("인증되지 않은 사용자입니다.");
        }

        return treePostJpaRepository.countByTreeId(treeId);
    }
}