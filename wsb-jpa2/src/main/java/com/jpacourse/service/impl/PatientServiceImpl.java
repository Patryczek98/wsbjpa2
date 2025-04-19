package com.jpacourse.service.impl;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.mapper.PatientMapper;
import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientDao patientDao, PatientMapper patientMapper) {
        this.patientDao = patientDao;
        this.patientMapper = patientMapper;
    }

    @Override
    public PatientEntity savePatient(PatientEntity patient) {
        return patientDao.save(patient);
    }

    @Override
    public Optional<PatientEntity> findPatientById(Long id) {
        return Optional.ofNullable(patientDao.findOne(id));
    }

    @Override
    public void deletePatient(Long id) {
        patientDao.delete(id);
    }

    @Override
    public Optional<PatientTO> findPatientTOById(Long id) {
        return Optional.ofNullable(patientDao.findOne(id))
                .map(patientMapper::toDto);
    }

    @Override
    public List<VisitEntity> findVisitsByPatientId(Long patientId) {
        Optional<PatientEntity> patient = findPatientById(patientId);
        return patient.map(PatientEntity::getVisits).orElse(List.of());
    }
}