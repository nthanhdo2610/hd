package com.tinhvan.hd.dao;


import com.tinhvan.hd.entity.NotificationGroupQueue;

import java.util.List;

public interface NotificationGroupQueueDao {

    void saveNotificationQueue(NotificationGroupQueue notificationGroupQueue);

    List<NotificationGroupQueue> getListNotificationQueueByStatus(Integer status);

    void updateNotificationQueue(NotificationGroupQueue notificationGroupQueue);
}
