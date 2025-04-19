package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.TreatmentType;
import com.jpacourse.service.DoctorService;
import com.jpacourse.service.MedicalTreatmentService;
import com.jpacourse.service.PatientService;
import com.jpacourse.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class MedicalTreatmentServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private MedicalTreatmentService treatmentService;

    @Test
    void shouldSaveTreatmentForVisit() {
        // Given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Kowalska");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        PatientEntity savedPatient = patientService.savePatient(patient);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jan");
        doctor.setLastName("Lekarz");
        doctor.setSpecialization("Kardiolog");
        DoctorEntity savedDoctor = doctorService.saveDoctor(doctor);

        VisitEntity visit = new VisitEntity();
        visit.setPatient(savedPatient);
        visit.setDoctor(savedDoctor);
        visit.setVisitDateTime(LocalDateTime.now());
        visit.setDescription("Konsultacja kardiologiczna");
        VisitEntity savedVisit = visitService.saveVisit(visit);

        MedicalTreatmentEntity treatment = new MedicalTreatmentEntity();
        treatment.setType(TreatmentType.MEDICATION);
        treatment.setVisit(savedVisit);

        // When
        MedicalTreatmentEntity savedTreatment = treatmentService.saveTreatment(treatment);

        // Then
        assertNotNull(savedTreatment.getId(), "Treatment should have an ID");
        assertEquals(TreatmentType.MEDICATION, savedTreatment.getType(), "Treatment type should match");
        assertEquals(savedVisit.getId(), savedTreatment.getVisit().getId(), "Treatment should be linked to the visit");
        assertTrue(treatmentService.findTreatmentById(savedTreatment.getId()).isPresent(), "Treatment should be found in the database");
    }
}