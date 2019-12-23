package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.NotificationAction;

import java.util.UUID;

public interface NotificationActionDao {
    NotificationAction find(UUID customerUuid, int notificationId);
}
