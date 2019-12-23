package com.tinhvan.hd.service;


import com.tinhvan.hd.entity.NotificationGroupQueue;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface NotificationGroupQueueService {

    void saveNotificationQueue(NotificationGroupQueue notificationGroupQueue);

    List<NotificationGroupQueue> getListNotificationQueueByStatus(Integer status);

    void updateNotificationQueue(NotificationGroupQueue notificationGroupQueue);
}
