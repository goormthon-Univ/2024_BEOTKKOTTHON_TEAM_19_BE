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

    List<Tree> findAllByUserId(Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE Tree t SET t.certification = :newValue")
    void updateBooleanFieldForAllTrees(@Param("newValue") boolean newValue);

    @Modifying
    @Transactional
    @Query("UPDATE Tree t SET t.deadline = t.deadline + 1 WHERE t.certification=false")
    void updateDeadlineIfConditionIsMet();

    @Modifying
    @Transactional
    @Query("UPDATE Tree t SET t.dead=true WHERE t.deadline>6")
    void updateDeadBooleanForAllTrees();

    @Modifying
    @Transactional
    @Query("UPDATE Tree t SET t.name = :newValue where t.id = :treeId")
    void treeUpdateName(@Param("newValue") String newValue, @Param("treeId") Integer treeId);

    @Transactional
    @Query("SELECT t FROM Tree t WHERE t.dead = true and t.user = :newUser")
    List<Tree> selectDeadTree(@Param("newValue") User newUser);
}
