package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.vo.NotificationVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface NotificationService {

    List<Notification> getAllNotification();

    void saveNotification(Notification notification);

    void deleteNotification(Notification notification);

    Notification getById(Integer id);

    void updateNotification(Notification notification);

    List<Notification> getListNotificationByIsSentAndCustomer(Integer isSent, UUID customerUuid);

    void sendNotificationToQueue(NotificationVO vo);


}
