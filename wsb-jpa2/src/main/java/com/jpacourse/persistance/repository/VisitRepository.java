package com.jpacourse.persistance.repository;

import com.jpacourse.persistance.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {
}