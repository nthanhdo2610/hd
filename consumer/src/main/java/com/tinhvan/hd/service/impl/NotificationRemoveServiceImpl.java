package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.NotificationRemoveDao;
import com.tinhvan.hd.entity.NotificationRemove;
import com.tinhvan.hd.service.NotificationRemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationRemoveServiceImpl implements NotificationRemoveService{

    @Autowired
    NotificationRemoveDao notificationRemoveDao;


    @Override
    public void saveNotificationRemove(NotificationRemove notificationRemove) {
        notificationRemoveDao.saveNotification(notificationRemove);
    }

}
