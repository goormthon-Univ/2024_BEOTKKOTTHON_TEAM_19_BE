package com.example.feelsun.service;

import com.example.feelsun.config.auth.PrincipalUserLoader;
import com.example.feelsun.domain.Tree;
import com.example.feelsun.domain.TreePost;
import com.example.feelsun.domain.User;
import com.example.feelsun.repository.TreeJpaRepository;
import com.example.feelsun.repository.TreePostJpaRepository;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.TreePostRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TreePostService {
    private final TreePostJpaRepository treePostJpaRepository;
    private final TreeJpaRepository treeRepository;
    private final PrincipalUserLoader principalUserLoader;
    private final UserJpaRepository userJPARepository;

    public TreePostService(TreePostJpaRepository treePostJpaRepository, TreeJpaRepository treeRepository, PrincipalUserLoader principalUserLoader, UserJpaRepository userJPARepository, TreePostJpaRepository treePostJpaRepository1) {
        this.treePostJpaRepository = treePostJpaRepository;
        this.treeRepository = treeRepository;
        this.principalUserLoader = principalUserLoader;
        this.userJPARepository = userJPARepository;
    }
//
//    @Transactional(readOnly = true)
//    public List<TreeResponse.MainTreeList> treeList() {
//        User user = getUserFromPrincipal();
//        List<Tree> trees = treeRepository.findByUser(user);
//        return trees.stream()
//                .map(this::mapToTreeResponse)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public void createTree(TreePostRequest.TreePostCreateRequest requestDTO, User user, Tree tree) {
        TreePost treePost = TreePost.builder()
                .content(requestDTO.getContent())
                .imageUrl(requestDTO.getImageUrl())
                .tree(tree)
                .user(user)
                .build();

//        treePostJpaRepository.save(treePost);
//        // 현재 하루의 종료 시간
//        LocalDateTime todayEndTime = LocalDate.now().atTime(LocalTime.max);
//        // 현재 시간
//        LocalDateTime currentTime = LocalDateTime.now();
//        // 하루 종료 시간을 시간초로 변환
//        long todayEndSecond = todayEndTime.toEpochSecond(ZoneOffset.UTC);
//        // 현재 시간을 시간초로 변환
//        long currentSecond = currentTime.toEpochSecond(ZoneOffset.UTC);
//        // 하루 종료까지 남은 시간초
//        long remainingTime = todayEndSecond - currentSecond;


        if (!tree.isCertification()) {
            tree.setCertification(true);
            Integer plus = tree.getContinuousPeriod();
            plus++;
            tree.setContinuousPeriod(plus);
            treeRepository.save(tree);
        }

    }
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void reset() {
        treeRepository.updateBooleanFieldForAllTrees(false);
    }

}
