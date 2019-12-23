package com.tinhvan.hd.dao.impl;


import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.NotificationFilterCategoryDao;
import com.tinhvan.hd.entity.NotificationFilterCategory;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationFilterCategoryDaoImpl implements NotificationFilterCategoryDao {



    @Override
    public void saveNotificationFilterCategory(NotificationFilterCategory notificationFilterCategory) {
        DAO.query((em) -> {
            em.persist(notificationFilterCategory);
        });
    }
}
