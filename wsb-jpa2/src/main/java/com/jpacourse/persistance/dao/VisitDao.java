package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitDao extends JpaRepository<VisitEntity, Long> {
}