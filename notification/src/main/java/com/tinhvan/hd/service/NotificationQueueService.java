package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.NotificationQueue;
import com.tinhvan.hd.payload.NotificationQueueDTO;

import java.util.List;

public interface NotificationQueueService {
    void insert(NotificationQueue queue);

    void update(NotificationQueue queue);

    void delete(NotificationQueue queue);

    List<NotificationQueue> findPushNotification();

    void update(NotificationQueueDTO queueDTO);
}
