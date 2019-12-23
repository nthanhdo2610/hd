package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface NotificationTemplatService {

    List<NotificationTemplate> getAllNotificationTemplateIsActive(Integer isDelete);

    void saveNotificationTemplate(NotificationTemplate notificationTemplate);

    void deleteNotificationTemplate(NotificationTemplate notificationTemplate);

    NotificationTemplate getById(Integer id);

    NotificationTemplate getByType(Integer type, String langcode);

    void updateNotificationTemplate(NotificationTemplate notificationTemplate);

    List<NotificationTemplate> getListNotificationTemplateModifiedBy(UUID modifiedBy);
}
