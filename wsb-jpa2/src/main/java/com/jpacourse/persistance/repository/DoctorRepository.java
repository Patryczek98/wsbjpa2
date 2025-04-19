package com.jpacourse.persistance.repository;

import com.jpacourse.persistance.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
}