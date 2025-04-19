package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PatientDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public PatientEntity save(PatientEntity patient) {
        if (patient.getId() == null) {
            entityManager.persist(patient);
        } else {
            patient = entityManager.merge(patient);
        }
        return patient;
    }

    public PatientEntity findOne(Long id) {
        return entityManager.find(PatientEntity.class, id);
    }

    @Transactional
    public void delete(Long id) {
        PatientEntity patient = findOne(id);
        if (patient != null) {
            entityManager.remove(patient);
        }
    }

    @Transactional
    public PatientEntity addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitDateTime, String description) {
        PatientEntity patient = entityManager.find(PatientEntity.class, patientId);
        DoctorEntity doctor = entityManager.find(DoctorEntity.class, doctorId);

        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient or Doctor not found");
        }

        VisitEntity visit = new VisitEntity();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setVisitDateTime(visitDateTime);
        visit.setDescription(description);

        patient.getVisits().add(visit);
        return entityManager.merge(patient);
    }

    public List<PatientEntity> findByLastName(String lastName) {
        TypedQuery<PatientEntity> query = entityManager.createQuery(
                "SELECT p FROM PatientEntity p WHERE p.lastName = :lastName", PatientEntity.class);
        query.setParameter("lastName", lastName);
        return query.getResultList();
    }

    public List<PatientEntity> findWithMoreThanXVisits(int x) {
        TypedQuery<PatientEntity> query = entityManager.createQuery(
                "SELECT p FROM PatientEntity p WHERE SIZE(p.visits) > :x", PatientEntity.class);
        query.setParameter("x", x);
        return query.getResultList();
    }

    public List<PatientEntity> findByDateOfBirthBefore(LocalDate date) {
        TypedQuery<PatientEntity> query = entityManager.createQuery(
                "SELECT p FROM PatientEntity p WHERE p.dateOfBirth < :date", PatientEntity.class);
        query.setParameter("date", date);
        return query.getResultList();
    }
}