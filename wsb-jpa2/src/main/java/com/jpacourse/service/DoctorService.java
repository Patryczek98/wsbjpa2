package com.jpacourse.service;

import com.jpacourse.persistance.entity.DoctorEntity;

import java.util.Optional;

public interface DoctorService {
    DoctorEntity saveDoctor(DoctorEntity doctor);
    Optional<DoctorEntity> findDoctorById(Long id);
}