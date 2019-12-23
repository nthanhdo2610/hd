package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.NotificationDao;
import com.tinhvan.hd.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    @Autowired
    EntityManager entityManager;


    @Override
    public List<Notification> getAllNotification() {
        return null;
    }

    @Override
    @Transactional
    public void saveNotification(Notification notification) {
        entityManager.persist(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Notification notification) {
        entityManager.remove(notification);
    }

    @Override
    public Notification getById(Integer id) {
        return entityManager.find(Notification.class,id);
    }

    @Override
    @Transactional
    public void updateNotification(Notification notification) {
        entityManager.merge(notification);
    }

    @Override
    public List<Notification> getListNotificationByIsSentAndCustomer(Integer isSent, UUID customerUuid) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Notification where isSent = :isSent and customerUuid =:customerUuid ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("isSent", isSent);
        query.setParameter("customerUuid",customerUuid);
        return query.getResultList();
    }


}
