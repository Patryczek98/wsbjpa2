package com.jpacourse.service.impl;

import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.repository.MedicalTreatmentRepository;
import com.jpacourse.service.MedicalTreatmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MedicalTreatmentServiceImpl implements MedicalTreatmentService {

    private final MedicalTreatmentRepository treatmentRepository;

    public MedicalTreatmentServiceImpl(MedicalTreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    @Transactional
    public MedicalTreatmentEntity saveTreatment(MedicalTreatmentEntity treatment) {
        // Upewnij się, że relacja visit jest ustawiona obustronnie
        if (treatment.getVisit() != null) {
            treatment.getVisit().getTreatments().add(treatment);
        }
        return treatmentRepository.save(treatment);
    }

    @Override
    public Optional<MedicalTreatmentEntity> findTreatmentById(Long id) {
        return treatmentRepository.findById(id);
    }
}