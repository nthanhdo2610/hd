package com.tinhvan.hd.dao.impl;
import com.tinhvan.hd.dao.NotificationGroupQueueDao;
import com.tinhvan.hd.entity.NotificationGroupQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class NotificationGroupQueueDaoImpl implements NotificationGroupQueueDao {

    @Autowired
    EntityManager entityManager;


    @Override
    @Transactional
    public void saveNotificationQueue(NotificationGroupQueue notificationGroupQueue) {
        entityManager.persist(notificationGroupQueue);
    }

    @Override
    public List<NotificationGroupQueue> getListNotificationQueueByStatus(Integer status) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationGroupQueue where status = :status ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateNotificationQueue(NotificationGroupQueue notificationGroupQueue) {
        entityManager.merge(notificationGroupQueue);
    }
}
