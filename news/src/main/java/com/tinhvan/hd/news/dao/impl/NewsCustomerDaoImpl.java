package com.tinhvan.hd.news.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.news.dao.NewsCustomerDao;
import com.tinhvan.hd.news.entity.NewsCustomer;
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
public class NewsCustomerDaoImpl implements NewsCustomerDao {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public void insert(NewsCustomer newsCustomer) {
//        try {
//            entityManager.persist(newsCustomer);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void update(NewsCustomer newsCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(newsCustomer);
//        });
//    }
//
//    @Override
//    public void delete(NewsCustomer newsCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.remove(newsCustomer);
//        });
//    }

    @Override
    public NewsCustomer findById(UUID id) {
        List<NewsCustomer> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        ls.add(entityManager.find(NewsCustomer.class, id));
        if (ls.size() > 0)
            return ls.get(0);
        return null;
    }

    @Override
    public List<NewsCustomer> getListNewsCustomerByCustomer(UUID customerUuid) {
        List<NewsCustomer> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NewsCustomer where customerId = :customerId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("customerId", customerUuid);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<NewsCustomer> getListNewsCustomerByNewsId(UUID newsId) {
        List<NewsCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NewsCustomer where newsId = :newsId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("newsId", newsId);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public int countListNewsCustomerByNewsId(UUID newsId) {
        int count = 0;
        List<NewsCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from NewsCustomer where newsId = :newsId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("newsId", newsId);
        ls.addAll(query.getResultList());
        if (ls != null)
            count = ls.size();
        return count;
    }

    @Override
    public NewsCustomer find(UUID newsId, UUID customerUuid) {
        List<NewsCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NewsCustomer where newsId = :newsId and customerId = :customerUuid");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("newsId", newsId);
        query.setParameter("customerUuid", customerUuid);
        ls.addAll(query.getResultList());
        if (ls != null && ls.size() > 0)
            return ls.get(0);
        return null;
    }
}
