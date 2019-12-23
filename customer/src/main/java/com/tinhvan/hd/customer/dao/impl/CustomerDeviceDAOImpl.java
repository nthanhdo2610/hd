package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.customer.dao.CustomerDeviceDAO;
import com.tinhvan.hd.customer.model.Customer;
import com.tinhvan.hd.customer.model.CustomerDevice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomerDeviceDAOImpl implements CustomerDeviceDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void insert(CustomerDevice device) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(device);
//        });
//    }
//
//    @Override
//    public void update(CustomerDevice device) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(device);
//        });
//    }

    @Override
    public void disableByUuidOrToken(UUID uuid, String fcmToken) {

        Query query = entityManager.createQuery("update CustomerDevice set status = :status0 where status = :status1 and (customerUuid = :uuid or fcmToken = :fcmToken)");
        query.setParameter("status0", HDConstant.STATUS.DISABLE);
        query.setParameter("status1", HDConstant.STATUS.ENABLE);
        query.setParameter("fcmToken", fcmToken);
        query.setParameter("uuid", uuid);
        query.executeUpdate();
    }

    @Override
    public CustomerDevice findByUuidAndToken(UUID uuid, String fcmToken) {
        if (HDUtil.isNullOrEmpty(fcmToken)||uuid==null)
            return null;
        List<CustomerDevice> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from CustomerDevice where fcmToken = :fcmToken and customerUuid = :uuid");
        query.setParameter("uuid", uuid);
        query.setParameter("fcmToken", fcmToken);
        lst.addAll(query.getResultList());
        if (!lst.isEmpty())
            return lst.get(0);
        return null;
    }

    @Override
    public List<CustomerDevice> findByUuid(UUID uuid) {
        List<CustomerDevice> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from CustomerDevice where status = :status and customerUuid = :uuid order by createAt DESC");
        query.setParameter("uuid", uuid);
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public List<CustomerDevice> findByUuidOrToken(UUID uuid, String fcmToken) {
        List<CustomerDevice> lst = new ArrayList<>();
        Query query = entityManager.createQuery("from CustomerDevice where customerUuid = :uuid or fcmToken = :fcmToken");
        query.setParameter("fcmToken", fcmToken);
        query.setParameter("uuid", uuid);
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public List<CustomerDevice> findByFcmToken(String fcmToken) {
        List<CustomerDevice> lst = new ArrayList<>();
        Query query = entityManager.createQuery("from CustomerDevice where fcmToken = :fcmToken");
        query.setParameter("fcmToken", fcmToken);
        lst.addAll(query.getResultList());
        return lst;
    }
}
