package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DoctorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public DoctorEntity save(DoctorEntity doctor) {
        if (doctor.getId() == null) {
            entityManager.persist(doctor);
        } else {
            doctor = entityManager.merge(doctor);
        }
        return doctor;
    }

    public DoctorEntity findOne(Long id) {
        return entityManager.find(DoctorEntity.class, id);
    }
}