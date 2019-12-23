package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationTemplate;

import java.util.List;
import java.util.UUID;

public interface NotificationDao {

    List<Notification> getAllNotification();

    void saveNotification(Notification notification);

    void deleteNotification(Notification notification);

    Notification getById(Integer id);

    void updateNotification(Notification notification);

    List<Notification> getListNotificationByIsSentAndCustomer(Integer isSent, UUID customerUuid);


}
