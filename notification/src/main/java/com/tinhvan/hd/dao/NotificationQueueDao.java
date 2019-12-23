package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.NotificationQueue;

import java.util.List;

public interface NotificationQueueDao {
    void insert(NotificationQueue queue);

    void update(NotificationQueue queue);

    void delete(NotificationQueue queue);

    List<NotificationQueue> findPushNotification();
}
