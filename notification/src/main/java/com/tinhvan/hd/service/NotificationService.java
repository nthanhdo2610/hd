package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.payload.NotificationQueueDTO;
import com.tinhvan.hd.payload.ReadDetailNotificationRequest;
import com.tinhvan.hd.payload.UuidNotificationRequest;
import com.tinhvan.hd.payload.NotificationSearchRequest;
import com.tinhvan.hd.vo.NotificationQueueVO;
import com.tinhvan.hd.vo.NotificationVO;
import com.tinhvan.hd.vo.Pagination;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    void saveNotification(Notification notification);

    void deleteNotification(Notification notification);

    Notification getById(Integer id);

    void updateNotification(Notification notification);

    List<Notification> getListNotificationByIsSentAndCustomer(NotificationSearchRequest searchRequest);

    List<Notification> getListNotificationByCustomerUuidAndType (NotificationSearchRequest notificationSearchRequest);

    void sendNotificationToQueue(NotificationVO vo);

    int countNotification(NotificationSearchRequest searchRequest);

    List<Notification> findNotificationByFilter(Pagination pageRequest, UUID customerUuid, Integer isSent);

    void sendNotificationQueue(NotificationQueueVO vo);

    List<Notification> getNotReadByCustomerUuid(UUID customerUuid);
    int countNotReadByCustomerUuid(UUID customerUuid);

    void saveOrUpdateAll(List<Notification> notifications);

    List<Notification> findByUuid(UuidNotificationRequest uuidNotificationRequest);

    void saveAll(List<Notification> notifications);

    void update(NotificationQueueDTO queueDTO);

    Notification findForReadDetail(ReadDetailNotificationRequest request);

    boolean validNotification(UUID notificationUuid, int type, UUID customerId);
}
