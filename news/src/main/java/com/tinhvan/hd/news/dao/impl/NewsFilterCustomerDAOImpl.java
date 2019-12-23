package com.tinhvan.hd.news.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.news.dao.NewsFilterCustomerDAO;
import com.tinhvan.hd.news.entity.NewsFilterCustomer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class NewsFilterCustomerDAOImpl implements NewsFilterCustomerDAO {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public void insert(NewsFilterCustomer filterCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(filterCustomer);
//        });
//    }
//
//    @Override
//    public void update(NewsFilterCustomer filterCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(filterCustomer);
//        });
//    }
//
//    @Override
//    public void delete(int id) {
//        DAO.query((entityManager) -> {
//            NewsFilterCustomer filterCustomer = entityManager.find(NewsFilterCustomer.class,id);
//            entityManager.remove(filterCustomer);
//        });
//    }

    @Override
    public List<NewsFilterCustomer> findList(UUID newsId) {
        List<NewsFilterCustomer> lst = new ArrayList<>();
        try {
            Query query = entityManager.createQuery("from NewsFilterCustomer where newsId = :newsId");
            query.setParameter("newsId", newsId);
            lst.addAll(query.getResultList());
        } catch (Exception e){
            e.printStackTrace();
        }
        return  lst;
    }
}
