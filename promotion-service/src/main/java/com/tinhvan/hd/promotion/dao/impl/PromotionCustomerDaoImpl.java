package com.tinhvan.hd.promotion.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.promotion.dao.PromotionCustomerDao;
import com.tinhvan.hd.promotion.entity.PromotionCustomer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Repository
public class PromotionCustomerDaoImpl implements PromotionCustomerDao {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public void insert(PromotionCustomer PromotionCustomer) {
//        try{
//            entityManager.persist(PromotionCustomer);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void update(PromotionCustomer PromotionCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(PromotionCustomer);
//        });
//    }
//
//    @Override
//    public void delete(PromotionCustomer PromotionCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.remove(PromotionCustomer);
//        });
//    }

    @Override
    public PromotionCustomer findById(UUID id) {
        List<PromotionCustomer> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        ls.add(entityManager.find(PromotionCustomer.class, id));
        if (ls.size() > 0)
            return ls.get(0);
        return null;
    }

    @Override
    public List<PromotionCustomer> getListPromotionCustomerByCustomer(UUID customerUuid) {
        List<PromotionCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from PromotionCustomer where customerId = :customerId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("customerId", customerUuid);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<PromotionCustomer> getListPromotionCustomerByPromotionId(UUID promotionId) {
        List<PromotionCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from PromotionCustomer where promotionId = :promotionId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("promotionId", promotionId);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public int countListPromotionCustomerByPromotionId(UUID promotionId) {
        int count = 0;
        List<PromotionCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from PromotionCustomer where promotionId = :promotionId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("promotionId", promotionId);
        ls.addAll(query.getResultList());
        if (ls != null)
            count = ls.size();
        return count;
    }

    @Override
    public PromotionCustomer find(UUID promotionId, UUID customerUuid) {
        List<PromotionCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from PromotionCustomer where promotionId = :promotionId and customerId = :customerUuid");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("promotionId", promotionId);
        query.setParameter("customerUuid", customerUuid);
        ls.addAll(query.getResultList());
        if (ls != null && ls.size() > 0)
            return ls.get(0);
        return null;
    }
}
