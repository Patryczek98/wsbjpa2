package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
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
class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Test
    void testShouldAddVisitToPatient() {
        // Given
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jan");
        doctor.setLastName("Lekarz");
        doctor.setSpecialization("Kardiolog");
        DoctorEntity savedDoctor = doctorDao.save(doctor);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Kowalska");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        PatientEntity savedPatient = patientDao.save(patient);

        LocalDateTime visitDateTime = LocalDateTime.now().minusDays(1); // Wizyta w przeszłości
        String description = "Konsultacja kardiologiczna";

        // When
        PatientEntity updatedPatient = patientDao.addVisitToPatient(
                savedPatient.getId(),
                savedDoctor.getId(),
                visitDateTime,
                description
        );

        // Then
        assertNotNull(updatedPatient, "Updated patient should not be null");
        assertEquals(1, updatedPatient.getVisits().size(), "Patient should have one visit");
        VisitEntity visit = updatedPatient.getVisits().get(0);
        assertNotNull(visit.getId(), "Visit should have an ID");
        assertEquals(visitDateTime, visit.getVisitDateTime(), "Visit date should match");
        assertEquals(description, visit.getDescription(), "Visit description should match");
        assertEquals(savedDoctor.getId(), visit.getDoctor().getId(), "Doctor ID should match");
        assertEquals(savedPatient.getId(), visit.getPatient().getId(), "Patient ID should match");
    }
}