package com.example.feelsun.service;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import com.example.feelsun.config.auth.PrincipalUserLoader;
import com.example.feelsun.config.errors.exception.Exception404;
import com.example.feelsun.domain.Tree;
import com.example.feelsun.domain.User;
import com.example.feelsun.repository.TreeJpaRepository;
import com.example.feelsun.repository.TreePostJpaRepository;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.TreeRequest;
import com.example.feelsun.response.TreeResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TreeService {
    private final TreeJpaRepository treeRepository;
    private final PrincipalUserLoader principalUserLoader;
    private final UserJpaRepository userJPARepository;
    private final TreePostJpaRepository treePostJpaRepository;

    public TreeService(TreeJpaRepository treeRepository, UserJpaRepository userRepository, PrincipalUserLoader principalUserLoader, UserJpaRepository userJPARepository, TreePostJpaRepository treePostJpaRepository) {
        this.treeRepository = treeRepository;
        this.principalUserLoader = principalUserLoader;
        this.userJPARepository = userJPARepository;
        this.treePostJpaRepository = treePostJpaRepository;
    }

    @Transactional(readOnly = true)
    public List<TreeResponse.MainTreeList> treeList(PrincipalUserDetails principalUserDetails) {
        List<Tree> trees = treeRepository.findByUser(principalUserDetails.getUser());

        if (trees.isEmpty()) {
            throw new Exception404("생성한 나무가 없습니다.");
        }

        return trees.stream()
                .map(this::mapToTreeResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createTree(TreeRequest.TreeCreateRequest requestDTO, User user) {
        Tree tree = Tree.builder()
                .name(requestDTO.getName())
                .user(user)
                .build();
        treeRepository.save(tree);
    }

    @Transactional
    public void updateTree(TreeRequest.TreeCreateRequest requestDTO, Integer treeId) {
        treeRepository.treeUpdateName(requestDTO.getName(), treeId);
    }

    @Transactional
    public void deleteTree(Integer treeId) {
        treeRepository.deleteById(treeId);
    }

    @Transactional
    public TreeResponse.TreeDetail getTreeDetails(Integer treeId) {
        Optional<Tree> treeOptional = treeRepository.findById(treeId);
        if (treeOptional.isPresent()) {
            Tree tree = treeOptional.get();
            return mapToTreeDetatailResponse(tree);
        } else {
            throw new Exception404("습관 정보를 찾을 수 없습니다.");
        }
    }

    @Transactional
    public TreeResponse.UserContinuousPeriod getUserContinuousPeriod(List<TreeResponse.MainTreeList> treeList){
        List<Integer> continuousperiodlist = new ArrayList<Integer>();
        if (treeList.isEmpty()) new Exception404("생성한 나무가 없습니다.");
        for (TreeResponse.MainTreeList tree : treeList) {
            if (continuousperiodlist.isEmpty()) {
                continuousperiodlist.add(tree.getContinuousPeriod());
            } else {
                if (continuousperiodlist.get(0) < tree.getContinuousPeriod()) {
                    continuousperiodlist.add(tree.getContinuousPeriod());
                    continuousperiodlist.remove(0);
                }
            }
        }

        int usercountinousperiod = continuousperiodlist.get(0);
        TreeResponse.UserContinuousPeriod response = new TreeResponse.UserContinuousPeriod();
        response.setUserContinuousPeriod(usercountinousperiod);

        return response;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetPlus() {
        treeRepository.updateBooleanFieldForAllTrees(false);
        treeRepository.updateDeadlineIfConditionIsMet();
        treeRepository.updateDeadBooleanForAllTrees();
    }

    private TreeResponse.TreeDetail mapToTreeDetatailResponse(Tree tree) {
        TreeResponse.TreeDetail response = new TreeResponse.TreeDetail();
        response.setUserId(tree.getUser().getId());
        response.setUserName(tree.getUser().getNickname());
        response.setTreeId(tree.getId());
        response.setTreeLevel(tree.getLevel());
        response.setExperience(tree.getExperience());
        response.setHabitName(tree.getName());
        response.setImageUrl(tree.getImageUrl());
        response.setPrice(tree.getPrice());
        response.setAccessLevel(tree.getAccessLevel());
        response.setCreatedAt(tree.getCreatedAt());

        return response;

    }

    private User getUserFromPrincipal() {
        User user = principalUserLoader.getRequestUser();
        if (user == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        return user;
    }

    private TreeResponse.MainTreeList mapToTreeResponse(Tree tree) {
        TreeResponse.MainTreeList response = new TreeResponse.MainTreeList();
        response.setUserId(tree.getUser().getId());
        response.setTreeId(tree.getId());
        response.setTreeLevel(tree.getLevel());
        response.setExperience(tree.getExperience());
        response.setHabitName(tree.getName());
        response.setTreeImageUrl(tree.getImageUrl());
        response.setContinuousPeriod(tree.getContinuousPeriod());
        response.setCertification(tree.isCertification());

        return response;
    }


    @Transactional
    public void updateTreeName(Integer treeId, TreeRequest.TreeCreateRequest requestDTO, PrincipalUserDetails principalUserDetails) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new Exception404("습관 정보를 찾을 수 없습니다."));

        if (!tree.getUser().getId().equals(principalUserDetails.getUser().getId())) {
            throw new Exception404("습관 정보를 찾을 수 없습니다.");
        }

        tree.setName(requestDTO.getName());

        treeRepository.save(tree);
    }
}