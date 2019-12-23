package com.tinhvan.hd.promotion.dao.impl;

import com.tinhvan.hd.promotion.dao.PromotionLogDao;
import com.tinhvan.hd.promotion.entity.PromotionLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PromotionLogDaoImpl implements PromotionLogDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void insert(PromotionLog log) {
        try{
            entityManager.persist(log);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
