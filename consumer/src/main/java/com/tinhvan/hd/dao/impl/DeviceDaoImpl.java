package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.DeviceDao;
import com.tinhvan.hd.entity.Device;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;


@Repository
public class DeviceDaoImpl  implements DeviceDao {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public void saveDevice(Device device) {
        entityManager.persist(device);
    }

    @Override
    @Transactional
    public void deleteDeviceByUserId(UUID customerUuid) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update Device set status = 0 ");
        queryBuilder.append(" where customerUuid=:customerUuid");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("customerUuid", customerUuid);
        query.executeUpdate();
    }

    @Override
    public List<Device> getAllDeviceByStatus(Integer status) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Device where status = :status ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public Device getDeviceByCustomerUuid(UUID customerUuid) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Device where customerUuid = :customerUuid ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("customerUuid", customerUuid);
        return (Device) query.getResultStream().findFirst().orElse(null);
    }
}
