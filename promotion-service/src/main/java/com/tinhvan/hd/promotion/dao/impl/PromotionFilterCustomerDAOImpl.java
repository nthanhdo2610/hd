package com.tinhvan.hd.promotion.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.promotion.dao.PromotionFilterCustomerDAO;
import com.tinhvan.hd.promotion.entity.PromotionFilterCustomer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class PromotionFilterCustomerDAOImpl implements PromotionFilterCustomerDAO {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public void insert(PromotionFilterCustomer filterCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(filterCustomer);
//        });
//    }
//
//    @Override
//    public void update(PromotionFilterCustomer filterCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(filterCustomer);
//        });
//    }
//
//    @Override
//    public void delete(int id) {
//        DAO.query((entityManager) -> {
//            PromotionFilterCustomer filterCustomer = entityManager.find(PromotionFilterCustomer.class, id);
//            entityManager.remove(filterCustomer);
//        });
//    }

    @Override
    public List<PromotionFilterCustomer> findList(UUID PromotionId) {
        List<PromotionFilterCustomer> lst = new ArrayList<>();
        try {
            Query query = entityManager.createQuery("from PromotionFilterCustomer where promotionId = :PromotionId");
            query.setParameter("PromotionId", PromotionId);
            lst.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }
}
