package com.tinhvan.hd.service.impl;


import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.dao.NotificationGroupQueueDao;
import com.tinhvan.hd.entity.NotificationGroupQueue;
import com.tinhvan.hd.repository.NotificationGroupQueueRepository;
import com.tinhvan.hd.service.NotificationGroupQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class NotificationGroupQueueServiceImpl implements NotificationGroupQueueService {

//    @Autowired
//    NotificationGroupQueueDao notificationQueueDao;

    @Autowired
    private NotificationGroupQueueRepository notificationGroupQueueRepository;

    @Override
    public void saveNotificationQueue(NotificationGroupQueue notificationGroupQueue) {
        notificationGroupQueueRepository.save(notificationGroupQueue);
    }

    @Override
    public List<NotificationGroupQueue> getListNotificationQueueByStatus(Integer status) {
        return notificationGroupQueueRepository.findAllByStatus(status);
    }

    @Override
    public void updateNotificationQueue(NotificationGroupQueue notificationGroupQueue) {
        notificationGroupQueueRepository.save(notificationGroupQueue);
    }
}
