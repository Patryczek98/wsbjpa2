package com.jpacourse.persistance.repository;

import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalTreatmentRepository extends JpaRepository<MedicalTreatmentEntity, Long> {
}