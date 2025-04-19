package com.jpacourse.service;

import com.jpacourse.persistance.entity.MedicalTreatmentEntity;

import java.util.Optional;

public interface MedicalTreatmentService {
    MedicalTreatmentEntity saveTreatment(MedicalTreatmentEntity treatment);
    Optional<MedicalTreatmentEntity> findTreatmentById(Long id);
}