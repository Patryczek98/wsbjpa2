package com.jpacourse.service.impl;

import com.jpacourse.persistance.dao.DoctorDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorDao doctorDao;

    public DoctorServiceImpl(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    @Override
    @Transactional
    public DoctorEntity saveDoctor(DoctorEntity doctor) {
        return doctorDao.save(doctor);
    }

    @Override
    public Optional<DoctorEntity> findDoctorById(Long id) {
        return Optional.ofNullable(doctorDao.findOne(id));
    }
}