package com.jpacourse.persistance.repository;

import com.jpacourse.persistance.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}