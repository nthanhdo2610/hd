package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.NotificationTemplate;

import java.util.List;
import java.util.UUID;

public interface NotificationTemplateDao {

    List<NotificationTemplate> getAllNotificationTemplateIsActive(Integer isDelete);

    void saveNotificationTemplate(NotificationTemplate notificationTemplate);

    void deleteNotificationTemplate(NotificationTemplate notificationTemplate);

    NotificationTemplate getById(Integer id);

    NotificationTemplate getByType(Integer type, String langcode);

    void updateNotificationTemplate(NotificationTemplate notificationTemplate);

    List<NotificationTemplate> getListNotificationTemplateModifiedBy(UUID modifiedBy);
}
