package com.tinhvan.hd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.dao.NotificationDao;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.service.NotificationService;
import com.tinhvan.hd.vo.NotificationVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public NotificationServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    NotificationDao notificationDao;


    @Override
    public List<Notification> getAllNotification() {
        return notificationDao.getAllNotification();
    }

    @Override
    public void saveNotification(Notification notification) {
        notificationDao.saveNotification(notification);
    }

    @Override
    public void deleteNotification(Notification notification) {
        notificationDao.deleteNotification(notification);
    }

    @Override
    public Notification getById(Integer id) {
        return notificationDao.getById(id);
    }

    @Override
    public void updateNotification(Notification notification) {
        notificationDao.updateNotification(notification);
    }

    @Override
    public List<Notification> getListNotificationByIsSentAndCustomer(Integer isSent, UUID customerUuid) {
        return notificationDao.getListNotificationByIsSentAndCustomer(isSent,customerUuid);
    }

    @Override
    public void sendNotificationToQueue(NotificationVO vo) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_NOTIFICATIONS, vo);
    }

}
