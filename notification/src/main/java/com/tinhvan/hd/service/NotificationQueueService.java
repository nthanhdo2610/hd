package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.NotificationQueue;

import java.util.List;

public interface NotificationQueueService {
    void insert(NotificationQueue queue);

    void update(NotificationQueue queue);

    void delete(NotificationQueue queue);

    List<NotificationQueue> findPushNotification();
}
