package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.NotificationQueueDao;
import com.tinhvan.hd.entity.NotificationQueue;
import com.tinhvan.hd.payload.NotificationQueueDTO;
import com.tinhvan.hd.repository.NotificationQueueRepository;
import com.tinhvan.hd.service.NotificationQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationQueueServiceImpl implements NotificationQueueService {

    @Autowired
    private NotificationQueueRepository notificationQueueRepository;

    @Autowired
    private NotificationQueueDao notificationQueueDao;

    @Override
    public void insert(NotificationQueue queue) {
        notificationQueueRepository.save(queue);
    }

    @Override
    public void update(NotificationQueue queue) {
        notificationQueueRepository.save(queue);
    }

    @Override
    public void delete(NotificationQueue queue) {
        notificationQueueRepository.delete(queue);
    }

    @Override
    public List<NotificationQueue> findPushNotification() {
        return (List<NotificationQueue>) notificationQueueDao.findPushNotification();
    }

    @Override
    public void update(NotificationQueueDTO queueDTO) {
        notificationQueueDao.update(queueDTO);
    }
}
