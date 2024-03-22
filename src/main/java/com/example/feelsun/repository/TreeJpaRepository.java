package com.example.feelsun.repository;

import com.example.feelsun.domain.Tree;
import com.example.feelsun.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeJpaRepository extends JpaRepository<Tree, Integer> {

    Page<Tree> findAll(Specification<Tree> spec, Pageable pageable);
  
    List<Tree> findByUser(User user);
  
    List<Tree> findAllByUserId(Integer userId);
}
