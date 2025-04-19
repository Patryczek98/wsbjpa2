package com.jpacourse.mapper;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.TreatmentType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientMapper {

    public PatientTO toDto(PatientEntity patientEntity) {
        return new PatientTO(
                patientEntity.getId(),
                patientEntity.getFirstName(),
                patientEntity.getLastName(),
                patientEntity.getDateOfBirth(),
                patientEntity.getVisits() != null ?
                        patientEntity.getVisits().stream()
                                .filter(visit -> visit.getVisitDateTime() != null && visit.getVisitDateTime().isBefore(LocalDateTime.now()))
                                .map(this::toVisitDto)
                                .collect(Collectors.toList()) :
                        Collections.emptyList()
        );
    }

    public PatientEntity toEntity(PatientTO patientTO) {
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(patientTO.getId());
        patientEntity.setFirstName(patientTO.getFirstName());
        patientEntity.setLastName(patientTO.getLastName());
        patientEntity.setDateOfBirth(patientTO.getDateOfBirth());
        return patientEntity;
    }

    private VisitTO toVisitDto(VisitEntity visitEntity) {
        return new VisitTO(
                visitEntity.getId(),
                visitEntity.getVisitDateTime(),
                visitEntity.getDoctor() != null ? visitEntity.getDoctor().getFirstName() : null,
                visitEntity.getDoctor() != null ? visitEntity.getDoctor().getLastName() : null,
                visitEntity.getTreatments() != null ?
                        visitEntity.getTreatments().stream()
                                .filter(treatment -> treatment.getType() != null)
                                .map(MedicalTreatmentEntity::getType)
                                .collect(Collectors.toList()) :
                        Collections.emptyList()
        );
    }
}