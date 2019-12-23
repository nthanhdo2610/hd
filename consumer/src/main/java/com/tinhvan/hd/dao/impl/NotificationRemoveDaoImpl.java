package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.NotificationRemoveDao;
import com.tinhvan.hd.entity.NotificationRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

@Repository
public class NotificationRemoveDaoImpl implements NotificationRemoveDao {

    @Autowired
    EntityManager entityManager;


    @Override
    @Transactional
    public void saveNotification(NotificationRemove notificationRemove) {
        entityManager.persist(notificationRemove);
    }
}
