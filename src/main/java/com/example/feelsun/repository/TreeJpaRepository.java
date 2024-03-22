package com.example.feelsun.repository;

import com.example.feelsun.domain.Tree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TreeJpaRepository extends JpaRepository<Tree, Integer> {

    Page<Tree> findAll(Specification<Tree> spec, Pageable pageable);
}
