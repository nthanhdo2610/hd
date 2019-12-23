package com.tinhvan.hd.service.impl;


import com.tinhvan.hd.dao.NotificationGroupQueueDao;
import com.tinhvan.hd.entity.NotificationGroupQueue;
import com.tinhvan.hd.service.NotificationGroupQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NotificationGroupQueueServiceImpl implements NotificationGroupQueueService {

    @Autowired
    NotificationGroupQueueDao notificationGroupQueueDao;

    @Override
    public void saveNotificationQueue(NotificationGroupQueue notificationGroupQueue) {
        notificationGroupQueueDao.saveNotificationQueue(notificationGroupQueue);
    }

    @Override
    public List<NotificationGroupQueue> getListNotificationQueueByStatus(Integer status) {
        return notificationGroupQueueDao.getListNotificationQueueByStatus(status);
    }

    @Override
    public void updateNotificationQueue(NotificationGroupQueue notificationGroupQueue) {
        notificationGroupQueueDao.updateNotificationQueue(notificationGroupQueue);
    }
}
