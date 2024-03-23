package com.example.feelsun.service;

import com.example.feelsun.config.auth.PrincipalUserDetails;
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
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TreePostService {

    private final S3UploadService s3UploadService;
    private final UserJpaRepository userJpaRepository;
    private final TreeJpaRepository treeJpaRepository;
    private final TreePostJpaRepository treePostJpaRepository;

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
}