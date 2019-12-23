package com.tinhvan.hd.dao.impl;
import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.NotificationGroupQueueDao;
import com.tinhvan.hd.entity.NotificationGroupQueue;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationGroupQueueDaoImpl implements NotificationGroupQueueDao {



    @Override
    public void saveNotificationQueue(NotificationGroupQueue notificationGroupQueue) {
        DAO.query((em) -> {
            em.persist(notificationGroupQueue);
        });
    }

    @Override
    public List<NotificationGroupQueue> getListNotificationQueueByStatus(Integer status) {
        List<NotificationGroupQueue> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationGroupQueue where status = :status ");
        DAO.query((em) -> {
            Query query = em.createQuery(queryBuilder.toString());
            query.setParameter("status", status);
            ls.addAll(query.getResultList());

        });

        return ls;
    }

    @Override
    public void updateNotificationQueue(NotificationGroupQueue notificationGroupQueue) {
        DAO.query((em) -> {
            em.merge(notificationGroupQueue);
        });
    }
}
