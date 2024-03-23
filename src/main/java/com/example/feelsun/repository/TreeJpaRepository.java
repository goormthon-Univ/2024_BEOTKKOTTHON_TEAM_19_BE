package com.example.feelsun.repository;

import com.example.feelsun.domain.Tree;
import com.example.feelsun.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TreeJpaRepository extends JpaRepository<Tree, Integer> {

    Page<Tree> findAll(Specification<Tree> spec, Pageable pageable);

    List<Tree> findByUser(User user);

    @Modifying
    @Transactional
    @Query("UPDATE Tree t SET t.certification = :newValue")
    void updateBooleanFieldForAllTrees(@Param("newValue") boolean newValue);

    List<Tree> findAllByUserId(Integer userId);
}
