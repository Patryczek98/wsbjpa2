package com.jpacourse.persistance.dao;

import com.jpacourse.dto.PatientTO;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private MedicalTreatmentService medicalTreatmentService;

    @Test
    void testShouldDeletePatientAndCascadeVisits() {
        // Given
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jan");
        doctor.setLastName("Lekarz");
        doctor.setSpecialization("Kardiolog");
        DoctorEntity savedDoctor = doctorService.saveDoctor(doctor);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Kowalska");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        PatientEntity savedPatient = patientService.savePatient(patient);

        VisitEntity visit = new VisitEntity();
        visit.setPatient(savedPatient);
        visit.setDoctor(savedDoctor);
        visit.setVisitDateTime(LocalDateTime.now().minusDays(1));
        visit.setDescription("Konsultacja kardiologiczna");
        VisitEntity savedVisit = visitService.saveVisit(visit);

        MedicalTreatmentEntity treatment = new MedicalTreatmentEntity();
        treatment.setType(TreatmentType.MEDICATION);
        treatment.setVisit(savedVisit);
        medicalTreatmentService.saveTreatment(treatment);

        // Debug: Sprawdź, czy wizyta jest zapisana
        Optional<VisitEntity> debugVisit = visitService.findVisitById(savedVisit.getId());
        assertTrue(debugVisit.isPresent(), "Visit should be saved before deletion");
        assertEquals(savedPatient.getId(), debugVisit.get().getPatient().getId(), "Visit should be linked to patient");

        // Debug: Sprawdź, czy pacjent ma wizytę
        PatientEntity debugPatient = patientService.findPatientById(savedPatient.getId())
                .orElseThrow(() -> new AssertionError("Patient not found"));
        assertNotNull(debugPatient.getVisits(), "Patient visits should not be null");
        assertFalse(debugPatient.getVisits().isEmpty(), "Patient should have at least one visit");

        // When
        patientService.deletePatient(savedPatient.getId());

        // Then
        assertTrue(patientService.findPatientById(savedPatient.getId()).isEmpty(), "Patient should be deleted");
        assertTrue(visitService.findVisitById(savedVisit.getId()).isEmpty(), "Visit should be deleted");
        assertTrue(doctorService.findDoctorById(savedDoctor.getId()).isPresent(), "Doctor should not be deleted");
    }

    @Test
    void testShouldFindPatientTOById() {
        // Given
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jan");
        doctor.setLastName("Lekarz");
        doctor.setSpecialization("Kardiolog");
        DoctorEntity savedDoctor = doctorService.saveDoctor(doctor);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Kowalska");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        PatientEntity savedPatient = patientService.savePatient(patient);

        VisitEntity visit = new VisitEntity();
        visit.setPatient(savedPatient);
        visit.setDoctor(savedDoctor);
        visit.setVisitDateTime(LocalDateTime.now().minusDays(1));
        visit.setDescription("Konsultacja kardiologiczna");
        VisitEntity savedVisit = visitService.saveVisit(visit);

        MedicalTreatmentEntity treatment = new MedicalTreatmentEntity();
        treatment.setType(TreatmentType.MEDICATION);
        treatment.setVisit(savedVisit);
        MedicalTreatmentEntity savedTreatment = medicalTreatmentService.saveTreatment(treatment);

        // Debug: Sprawdź, czy wizyta jest zapisana
        Optional<VisitEntity> debugVisit = visitService.findVisitById(savedVisit.getId());
        assertTrue(debugVisit.isPresent(), "Visit should be saved");
        assertEquals(savedPatient.getId(), debugVisit.get().getPatient().getId(), "Visit should be linked to patient");

        // Debug: Sprawdź, czy leczenie jest zapisane
        Optional<MedicalTreatmentEntity> debugTreatment = medicalTreatmentService.findTreatmentById(savedTreatment.getId());
        assertTrue(debugTreatment.isPresent(), "Treatment should be saved");
        assertEquals(savedVisit.getId(), debugTreatment.get().getVisit().getId(), "Treatment should be linked to visit");

        // Debug: Sprawdź, czy pacjent ma wizytę
        PatientEntity debugPatient = patientService.findPatientById(savedPatient.getId())
                .orElseThrow(() -> new AssertionError("Patient not found"));
        assertNotNull(debugPatient.getVisits(), "Patient visits should not be null");
        assertFalse(debugPatient.getVisits().isEmpty(), "Patient should have at least one visit");

        // Debug: Sprawdź, czy wizyta ma leczenie
        assertNotNull(debugVisit.get().getTreatments(), "Visit treatments should not be null");
        assertFalse(debugVisit.get().getTreatments().isEmpty(), "Visit should have at least one treatment");

        // When
        PatientTO patientTO = patientService.findPatientTOById(savedPatient.getId())
                .orElseThrow(() -> new AssertionError("PatientTO not found"));

        // Then
        assertNotNull(patientTO, "PatientTO should not be null");
        assertEquals(savedPatient.getId(), patientTO.getId(), "Patient ID should match");
        assertEquals("Anna", patientTO.getFirstName(), "First name should match");
        assertEquals("Kowalska", patientTO.getLastName(), "Last name should match");
        assertEquals(LocalDate.of(1990, 5, 15), patientTO.getDateOfBirth(), "Date of birth should match");
        assertEquals(1, patientTO.getVisits().size(), "Should have one visit");
        assertEquals(savedVisit.getVisitDateTime(), patientTO.getVisits().get(0).getVisitDateTime(), "Visit date should match");
        assertEquals("Jan", patientTO.getVisits().get(0).getDoctorFirstName(), "Doctor first name should match");
        assertEquals("Lekarz", patientTO.getVisits().get(0).getDoctorLastName(), "Doctor last name should match");
        assertEquals(1, patientTO.getVisits().get(0).getTreatmentTypes().size(), "Should have one treatment");
        assertEquals(TreatmentType.MEDICATION, patientTO.getVisits().get(0).getTreatmentTypes().get(0), "Treatment type should match");
    }

    @Test
    void testShouldFindVisitsByPatientId() {
        // Given
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jan");
        doctor.setLastName("Lekarz");
        doctor.setSpecialization("Kardiolog");
        DoctorEntity savedDoctor = doctorService.saveDoctor(doctor);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Kowalska");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        PatientEntity savedPatient = patientService.savePatient(patient);

        VisitEntity visit1 = new VisitEntity();
        visit1.setPatient(savedPatient);
        visit1.setDoctor(savedDoctor);
        visit1.setVisitDateTime(LocalDateTime.now().minusDays(2));
        visit1.setDescription("Konsultacja kardiologiczna");
        visitService.saveVisit(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setPatient(savedPatient);
        visit2.setDoctor(savedDoctor);
        visit2.setVisitDateTime(LocalDateTime.now().minusDays(1));
        visit2.setDescription("Follow-up");
        visitService.saveVisit(visit2);

        // When
        List<VisitEntity> visits = patientService.findVisitsByPatientId(savedPatient.getId());

        // Then
        assertEquals(2, visits.size(), "Should find two visits for the patient");
        assertTrue(visits.stream().anyMatch(v -> v.getDescription().equals("Konsultacja kardiologiczna")));
        assertTrue(visits.stream().anyMatch(v -> v.getDescription().equals("Follow-up")));
    }
}