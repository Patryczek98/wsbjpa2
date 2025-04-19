package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.AddressEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AddressDaoTest {

    @Autowired
    private AddressDao addressDao;

    @Transactional
    @Rollback
    @Test
    public void testShouldFindAddressById() {
        // given
        AddressEntity address = new AddressEntity();
        address.setAddressLine1("Main Street");
        address.setAddressLine2("Apt 1");
        address.setCity("Warsaw");
        address.setPostalCode("00-001");
        AddressEntity savedAddress = addressDao.save(address);

        // when
        AddressEntity addressEntity = addressDao.findOne(savedAddress.getId());

        // then
        assertThat(addressEntity).isNotNull();
        assertThat(addressEntity.getPostalCode()).isEqualTo("00-001");
        assertThat(addressEntity.getAddressLine1()).isEqualTo("Main Street");
        assertThat(addressEntity.getCity()).isEqualTo("Warsaw");
    }

    @Transactional
    @Rollback
    @Test
    public void testShouldSaveAddress() {
        // given
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddressLine1("line1");
        addressEntity.setAddressLine2("line2");
        addressEntity.setCity("City1");
        addressEntity.setPostalCode("66-666");
        long entitiesNumBefore = addressDao.count();

        // when
        final AddressEntity saved = addressDao.save(addressEntity);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(addressDao.count()).isEqualTo(entitiesNumBefore + 1);
    }

    @Transactional
    @Rollback
    @Test
    public void testShouldSaveAndRemoveAddress() {
        // given
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddressLine1("line1");
        addressEntity.setAddressLine2("line2");
        addressEntity.setCity("City1");
        addressEntity.setPostalCode("66-666");

        // when
        final AddressEntity saved = addressDao.save(addressEntity);
        assertThat(saved.getId()).isNotNull();
        final AddressEntity newSaved = addressDao.findOne(saved.getId());
        assertThat(newSaved).isNotNull();

        addressDao.delete(saved.getId());

        // then
        final AddressEntity removed = addressDao.findOne(saved.getId());
        assertThat(removed).isNull();
    }
}