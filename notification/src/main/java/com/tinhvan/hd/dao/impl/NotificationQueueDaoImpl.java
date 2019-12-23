package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.dao.NotificationQueueDao;
import com.tinhvan.hd.entity.NotificationQueue;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class NotificationQueueDaoImpl implements NotificationQueueDao {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public void insert(NotificationQueue queue) {
        DAO.query((entityManager) -> {
            entityManager.persist(queue);
        });
    }

    @Override
    public void update(NotificationQueue queue) {
        try {
            entityManager.merge(queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(NotificationQueue queue) {
        DAO.query((entityManager) -> {
            entityManager.remove(queue);
        });
    }

    @Override
    public List<NotificationQueue> findPushNotification() {
        List<NotificationQueue> ls = new ArrayList<>();
        try {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add("from NotificationQueue where status =:status0 or status = :status1");
            Query query = entityManager.createQuery(joiner.toString());
            query.setParameter("status0", NotificationQueue.STATUS.NEW);
            query.setParameter("status1", NotificationQueue.STATUS.FAIL);
            ls.addAll(query.getResultList());
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        return ls;
    }
}
