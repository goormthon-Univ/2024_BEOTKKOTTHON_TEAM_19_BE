package com.example.feelsun.repository;

import com.example.feelsun.domain.TreePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TreePostJpaRepository extends JpaRepository<TreePost, Integer> {
    Page<TreePost> findAllByTreeId(Integer treeId, Pageable pageable);

    Page<TreePost> findAllByUserId(Integer userId, Pageable pageable);

    int countByTreeId(Integer treeId);

    @Query("SELECT COUNT(tp) FROM TreePost tp WHERE tp.tree.id = :treeId AND tp.createdAt >= :startDate AND tp.createdAt < :endDate")
    int countByTreeIdAndCreatedAt(Integer treeId, LocalDateTime startDate, LocalDateTime endDate);

}
