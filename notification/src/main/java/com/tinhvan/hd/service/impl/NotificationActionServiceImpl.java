package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.NotificationActionDao;
import com.tinhvan.hd.entity.NotificationAction;
import com.tinhvan.hd.repository.NotificationRemoverepository;
import com.tinhvan.hd.service.NotificationActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationActionServiceImpl implements NotificationActionService {

    @Autowired
    private NotificationActionDao notificationActionDao;

    @Autowired
    private NotificationRemoverepository notificationRemoverepository;


    @Override
    public void save(NotificationAction notificationAction) {
        notificationRemoverepository.save(notificationAction);
    }

    @Override
    public NotificationAction find(UUID customerUuid, int notificationId) {
        return notificationActionDao.find(customerUuid, notificationId);
    }
}
