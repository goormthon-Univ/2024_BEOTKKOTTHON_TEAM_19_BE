package com.example.feelsun.repository;

import com.example.feelsun.domain.TreePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreePostJpaRepository extends JpaRepository<TreePost, Integer> {
    Page<TreePost> findAllByTreeId(Integer treeId, Pageable pageable);
}