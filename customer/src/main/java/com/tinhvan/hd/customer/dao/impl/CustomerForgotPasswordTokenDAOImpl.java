package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.customer.dao.CustomerForgotPasswordTokenDAO;
import com.tinhvan.hd.customer.model.CustomerForgotPasswordToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomerForgotPasswordTokenDAOImpl implements CustomerForgotPasswordTokenDAO {
//    @Override
//    public void insert(CustomerForgotPasswordToken forgotPasswordToken) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(forgotPasswordToken);
//        });
//    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void delete(CustomerForgotPasswordToken forgotPasswordToken) {
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("update CustomerForgotPasswordToken set status = :status0 where customerUuid = :customerUuid and status = :status1");
        query.setParameter("status0", HDConstant.STATUS.DISABLE);
        query.setParameter("status1", HDConstant.STATUS.ENABLE);
        query.setParameter("customerUuid", forgotPasswordToken.getCustomerUuid());
        query.executeUpdate();
    }
//    @Override
//    public void update(CustomerForgotPasswordToken forgotPasswordToken) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(forgotPasswordToken);
//        });
//    }

    @Override
    public CustomerForgotPasswordToken findActive(String token) {
        List<CustomerForgotPasswordToken> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from CustomerForgotPasswordToken where token = :token and status = :status1");
        query.setParameter("status1", HDConstant.STATUS.ENABLE);
        query.setParameter("token", token);
        ls.addAll(query.getResultList());
        if(ls.size()>0)
            return ls.get(0);
        return null;
    }

    @Override
    public List<CustomerForgotPasswordToken> getForgotPasswordByCustomerUuidAndStatus(UUID customerUuid) {
        List<CustomerForgotPasswordToken> ls = new ArrayList<>();

        Query query = entityManager.createQuery("from CustomerForgotPasswordToken where customerUuid = :customerUuid and status = :status ");

        query.setParameter("customerUuid", customerUuid);
        query.setParameter("status", HDConstant.STATUS.ENABLE);

        ls = query.getResultList();

        return ls;
    }
}
