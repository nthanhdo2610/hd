package com.tinhvan.hd.service;


import com.tinhvan.hd.entity.NotificationGroupQueue;

import java.util.List;


public interface NotificationGroupQueueService {

    void saveNotificationQueue(NotificationGroupQueue notificationGroupQueue);

    List<NotificationGroupQueue> getListNotificationQueueByStatus(Integer status);

    void updateNotificationQueue(NotificationGroupQueue notificationGroupQueue);
}
