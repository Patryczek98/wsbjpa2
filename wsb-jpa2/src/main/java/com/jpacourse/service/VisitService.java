package com.jpacourse.service;

import com.jpacourse.persistance.entity.VisitEntity;

import java.util.Optional;

public interface VisitService {
    VisitEntity saveVisit(VisitEntity visit);
    Optional<VisitEntity> findVisitById(Long id);
}