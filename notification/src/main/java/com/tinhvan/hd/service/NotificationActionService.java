package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.NotificationAction;
import com.tinhvan.hd.payload.NotificationQueueDTO;

import java.util.List;
import java.util.UUID;

public interface NotificationActionService {

    void save(NotificationAction notificationAction);
    NotificationAction find(UUID customerUuid, int notificationId);

    List<NotificationAction> getByCustomerUuid(UUID customerUuid);

    void saveAll(List<NotificationAction> notificationActions);

    void update(NotificationQueueDTO queueDTO);

}
