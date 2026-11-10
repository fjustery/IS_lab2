package com.p3319.lab2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.p3319.lab2.entity.ImportOperation;

@Repository
public interface ImportOperationRepository extends JpaRepository<ImportOperation, Long> {

  @EntityGraph(attributePaths = "user")
  Page<ImportOperation> findAllByOrderByIdDesc(Pageable pageable);

  @EntityGraph(attributePaths = "user")
  Page<ImportOperation> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);
}