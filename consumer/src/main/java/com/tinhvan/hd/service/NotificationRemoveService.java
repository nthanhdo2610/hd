package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationRemove;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationRemoveService {

    void saveNotificationRemove(NotificationRemove notificationRemove);

}
