package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.NotificationActionDao;
import com.tinhvan.hd.entity.NotificationAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
public class NotificationActionDaoImpl implements NotificationActionDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public NotificationAction find(UUID customerUuid, int notificationId) {
        Query query = entityManager.createQuery("from NotificationAction where customerUuid = :customerUuid and notificationId = :notificationId");
        query.setParameter("customerUuid", customerUuid);
        query.setParameter("notificationId", notificationId);
        List<NotificationAction> notificationActions = query.getResultList();
        if (notificationActions != null && notificationActions.size() > 0)
            return notificationActions.get(0);
        return null;
    }
}
