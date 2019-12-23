package com.tinhvan.hd.news.dao.impl;

import com.tinhvan.hd.news.dao.NewsLogDao;
import com.tinhvan.hd.news.entity.NewsLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class NewsLogDaoImpl implements NewsLogDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void insert(NewsLog log) {
        try{
            entityManager.persist(log);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
