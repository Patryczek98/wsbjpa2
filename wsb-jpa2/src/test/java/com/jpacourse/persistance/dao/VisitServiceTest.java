package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.service.DoctorService;
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
class VisitServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private VisitService visitService;

    @Test
    void shouldSaveVisitForPatientAndDoctor() {
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

        // When
        VisitEntity savedVisit = visitService.saveVisit(visit);

        // Then
        assertNotNull(savedVisit.getId(), "Visit should have an ID");
        assertEquals(savedPatient.getId(), savedVisit.getPatient().getId(), "Visit should be linked to the patient");
        assertEquals(savedDoctor.getId(), savedVisit.getDoctor().getId(), "Visit should be linked to the doctor");
        assertEquals("Konsultacja kardiologiczna", savedVisit.getDescription(), "Visit description should match");
        assertTrue(visitService.findVisitById(savedVisit.getId()).isPresent(), "Visit should be found in the database");
    }
}