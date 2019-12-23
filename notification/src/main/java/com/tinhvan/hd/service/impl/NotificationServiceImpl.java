package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.dao.NotificationDao;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.repository.NotificationRepository;
import com.tinhvan.hd.service.NotificationService;
import com.tinhvan.hd.payload.NotificationSearchRequest;
import com.tinhvan.hd.vo.NotificationQueueVO;
import com.tinhvan.hd.vo.NotificationVO;
import com.tinhvan.hd.vo.Pagination;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private NotificationRepository notificationRepository;



    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    @Override
    public Notification getById(Integer id) {
        return notificationRepository.findById(id);
    }

    @Override
    public void updateNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getListNotificationByIsSentAndCustomer(NotificationSearchRequest searchRequest) {
        return notificationDao.getListNotificationByIsSentAndCustomer(searchRequest);
    }

    @Override
    public List<Notification> getListNotificationByCustomerUuidAndType(NotificationSearchRequest notificationSearchRequest) {
        return notificationDao.getListNotificationByCustomerUuidAndType(notificationSearchRequest);
    }

    @Override
    public void sendNotificationToQueue(NotificationVO vo) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_NOTIFICATIONS, vo);
    }

    @Override
    public int countNotification(NotificationSearchRequest searchRequest) {
        return notificationDao.countNotification(searchRequest);
    }

    @Override
    public List<Notification> findNotificationByFilter(Pagination pagination, UUID customerUuid,Integer isSent) {

        Sort sortOrder = Sort.by("createdAt");

        Pageable paging = PageRequest.of(pagination.getPageNo(), pagination.getPageSize(),sortOrder);


        Page<Notification> pagedResult = notificationRepository.findAllByCustomerUuidAndIsSent(customerUuid,isSent,paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void sendNotificationQueue(NotificationQueueVO vo){
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_NOTIFICATION_QUEUE, vo);
    }
}
