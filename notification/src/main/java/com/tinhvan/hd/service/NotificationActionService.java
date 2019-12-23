package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.NotificationAction;

import java.util.UUID;

public interface NotificationActionService {

    void save(NotificationAction notificationAction);
    NotificationAction find(UUID customerUuid, int notificationId);

}
