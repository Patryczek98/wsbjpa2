package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

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

        LocalDateTime visitDateTime = LocalDateTime.now().minusDays(1);
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

    @Test
    void testShouldFindPatientsByLastName() {
        // Given
        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("Liam");
        patient1.setLastName("KowalskiTest");
        patient1.setDateOfBirth(LocalDate.of(1995, 6, 15));
        patientDao.save(patient1);

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("Emma");
        patient2.setLastName("KowalskiTest");
        patient2.setDateOfBirth(LocalDate.of(1992, 12, 25));
        patientDao.save(patient2);

        // When
        List<PatientEntity> patients = patientDao.findByLastName("KowalskiTest");

        // Then
        assertEquals(2, patients.size(), "Should find two patients with last name KowalskiTest");
        assertTrue(patients.stream().anyMatch(p -> p.getFirstName().equals("Liam")));
        assertTrue(patients.stream().anyMatch(p -> p.getFirstName().equals("Emma")));
    }

    @Test
    void testShouldFindPatientsWithMoreThanXVisits() {
        // Given
        // Wyczyść bazę przed testem
        entityManager.createQuery("DELETE FROM MedicalTreatmentEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM VisitEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM PatientEntity").executeUpdate();
        entityManager.flush();

        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("Noah");
        patient1.setLastName("Nowak");
        patient1.setDateOfBirth(LocalDate.of(1980, 2, 10));
        patientDao.save(patient1);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jan");
        doctor.setLastName("Lekarz");
        doctor.setSpecialization("Kardiolog");
        DoctorEntity savedDoctor = doctorDao.save(doctor);

        // Dodaj 3 wizyty dla pacjenta
        patientDao.addVisitToPatient(patient1.getId(), savedDoctor.getId(), LocalDateTime.now().minusDays(10), "Visit 1");
        patientDao.addVisitToPatient(patient1.getId(), savedDoctor.getId(), LocalDateTime.now().minusDays(5), "Visit 2");
        patientDao.addVisitToPatient(patient1.getId(), savedDoctor.getId(), LocalDateTime.now().minusDays(1), "Visit 3");

        // Dodaj innego pacjenta z 1 wizytą
        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("Olivia");
        patient2.setLastName("Wiśniewska");
        patient2.setDateOfBirth(LocalDate.of(2000, 8, 20));
        patientDao.save(patient2);
        patientDao.addVisitToPatient(patient2.getId(), savedDoctor.getId(), LocalDateTime.now().minusDays(1), "Visit 1");

        // When
        List<PatientEntity> patients = patientDao.findWithMoreThanXVisits(2);

        // Then
        assertEquals(1, patients.size(), "Should find one patient with more than 2 visits");
        assertEquals("Noah", patients.get(0).getFirstName());
    }

    @Test
    void testShouldFindPatientsByDateOfBirthBefore() {
        // Given
        // Wyczyść bazę przed testem
        entityManager.createQuery("DELETE FROM MedicalTreatmentEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM VisitEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM PatientEntity").executeUpdate();
        entityManager.flush();

        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("Ava");
        patient1.setLastName("Zielińska");
        patient1.setDateOfBirth(LocalDate.of(1975, 3, 5));
        patientDao.save(patient1);

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("Olivia");
        patient2.setLastName("Wiśniewska");
        patient2.setDateOfBirth(LocalDate.of(2000, 8, 20));
        patientDao.save(patient2);

        // When
        List<PatientEntity> patients = patientDao.findByDateOfBirthBefore(LocalDate.of(1976, 1, 1));

        // Then
        assertEquals(1, patients.size(), "Should find one patient born before 1976");
        assertEquals("Ava", patients.get(0).getFirstName());
    }

    @Test
    void testOptimisticLockOnPatient() {
        // Given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Kowalska");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        entityManager.persist(patient);
        entityManager.flush();
        entityManager.clear();

        Long patientId = patient.getId();
        System.out.println("Initial patient version: " + patient.getVersion());

        // Pobierz pacjenta przed pierwszą modyfikacją (version=0)
        PatientEntity p2 = entityManager.find(PatientEntity.class, patientId);
        assertNotNull(p2, "Patient should be found before first transaction");
        System.out.println("Before first transaction, p2 version: " + p2.getVersion());
        entityManager.detach(p2); // Odłącz p2, aby zachować version=0

        // Modyfikuj w pierwszej sesji w oddzielnej transakcji
        transactionTemplate.execute(status -> {
            PatientEntity p1 = entityManager.find(PatientEntity.class, patientId);
            assertNotNull(p1, "Patient should be found in first transaction");
            System.out.println("Before update in first transaction, version: " + p1.getVersion());
            p1.setFirstName("Anna Maria");
            entityManager.merge(p1);
            entityManager.flush();
            entityManager.clear();
            System.out.println("After update in first transaction, version: " + p1.getVersion());
            return null;
        });

        // Modyfikuj p2 w drugiej sesji (powinno rzucić OptimisticLockException)
        transactionTemplate.execute(status -> {
            System.out.println("Before update in second transaction, p2 version: " + p2.getVersion());
            p2.setFirstName("Anna Zofia");
            try {
                entityManager.merge(p2);
                entityManager.flush();
                fail("Expected OptimisticLockException");
            } catch (OptimisticLockException e) {
                return null;
            }
            return null;
        });
    }

    @Test
    void testFetchPatientWithVisitsSelect() {
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

        patientDao.addVisitToPatient(savedPatient.getId(), savedDoctor.getId(), LocalDateTime.now().minusDays(10), "Visit 1");
        patientDao.addVisitToPatient(savedPatient.getId(), savedDoctor.getId(), LocalDateTime.now().minusDays(5), "Visit 2");

        // When
        PatientEntity fetchedPatient = patientDao.findOne(savedPatient.getId());

        // Then
        assertNotNull(fetchedPatient, "Patient should be found");
        assertEquals(2, fetchedPatient.getVisits().size(), "Patient should have two visits");
    }
}