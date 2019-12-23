package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.customer.dao.CustomerTokenDAO;
import com.tinhvan.hd.customer.model.CustomerToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomerTokenDAOImpl implements CustomerTokenDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void insert(CustomerToken customerToken) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(customerToken);
//        });
//    }

    @Override
    public void disable(UUID customerUuid, String environment, Date deletedAt) {
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("update CustomerToken set status = :status0, deletedAt = :deletedAt where customerUuid = :customerUuid and status = :status1 and environment = :environment");
        query.setParameter("status0", HDConstant.STATUS.DISABLE);
        query.setParameter("status1", HDConstant.STATUS.ENABLE);
        query.setParameter("deletedAt", deletedAt);
        query.setParameter("customerUuid", customerUuid);
        query.setParameter("environment", environment);
        query.executeUpdate();
    }
    @Override
    public void disableAllByCustomer(UUID customerUuid, Date deletedAt) {
        Query query = entityManager.createQuery("update CustomerToken set status = :status0, deletedAt = :deletedAt where customerUuid = :customerUuid and status = :status1");
        query.setParameter("status0", HDConstant.STATUS.DISABLE);
        query.setParameter("status1", HDConstant.STATUS.ENABLE);
        query.setParameter("deletedAt", deletedAt);
        query.setParameter("customerUuid", customerUuid);
        query.executeUpdate();
    }
    @Override
    public List<CustomerToken> getCustomerTokenByCustomerUuidAndEnvironments(UUID customerUuid, String environment) {
        List<CustomerToken> ls = new ArrayList<>();

        Query query = entityManager.createQuery("from CustomerToken where customerUuid = :customerUuid and status = :status1 and environment = :environment ");
        query.setParameter("status1", HDConstant.STATUS.ENABLE);
        query.setParameter("customerUuid", customerUuid);
        query.setParameter("environment", environment);
        ls.addAll(query.getResultList());

        return ls;
    }

    @Override
    public CustomerToken findByToken(String token) {
        List<CustomerToken> ls = new ArrayList<>();

        Query query = entityManager.createQuery("from CustomerToken where token =:token");
        query.setParameter("token", token);
        ls.addAll(query.getResultList());
        if(ls.size()>0)
            return ls.get(0);
        return null;
    }

}
