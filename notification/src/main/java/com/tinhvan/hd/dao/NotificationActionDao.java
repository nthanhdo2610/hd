package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.NotificationAction;
import com.tinhvan.hd.payload.NotificationQueueDTO;

import java.util.UUID;

public interface NotificationActionDao {

    NotificationAction find(UUID customerUuid, int notificationId);

    void update(NotificationQueueDTO queueDTO);
}
