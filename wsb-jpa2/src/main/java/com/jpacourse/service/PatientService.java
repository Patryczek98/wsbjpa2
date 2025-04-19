package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    PatientEntity savePatient(PatientEntity patient);
    Optional<PatientEntity> findPatientById(Long id);
    void deletePatient(Long id);
    Optional<PatientTO> findPatientTOById(Long id);
    List<VisitEntity> findVisitsByPatientId(Long patientId);
}