package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.payload.NotificationQueueDTO;
import com.tinhvan.hd.payload.ReadDetailNotificationRequest;
import com.tinhvan.hd.payload.UuidNotificationRequest;
import com.tinhvan.hd.payload.NotificationSearchRequest;

import java.util.List;
import java.util.UUID;

public interface NotificationDao {

//    void saveNotification(Notification notification);
//
//    void deleteNotification(Notification notification);
//
//    Notification getById(Integer id);
//
//    void updateNotification(Notification notification);

    List<Notification> getListNotificationByIsSentAndCustomer(NotificationSearchRequest searchRequest);

    int countNotification(NotificationSearchRequest searchRequest);

    List<Notification> getAll(NotificationSearchRequest notificationSearchRequest);

    List<Notification> getListNotificationByCustomerUuidAndType (NotificationSearchRequest notificationSearchRequest);

    List<Notification> getNotReadByCustomerUuid(UUID customerUuid);
    int countNotReadByCustomerUuid(UUID customerUuid);

    List<Notification> findByUuid(UuidNotificationRequest uuidNotificationRequest);

    void update(NotificationQueueDTO queueDTO);

    Notification findForReadDetail(ReadDetailNotificationRequest request);

    boolean validNotification(UUID notificationUuid, int type, UUID customerId);
}
