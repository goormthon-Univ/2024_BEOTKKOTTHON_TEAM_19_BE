package com.example.feelsun.repository;

import com.example.feelsun.domain.TreePost;
import com.example.feelsun.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreePostJpaRepository extends JpaRepository<TreePost, Integer> {
    Page<TreePost> findAllByTreeId(Integer treeId, Pageable pageable);

    Page<TreePost> findAllByUserId(Integer userId, Pageable pageable);

    List<TreePost> findAllTreePostByUserId(User user);
}
