package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.NotificationTemplateDao;
import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.service.NotificationTemplatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationTemplatServiceImpl implements NotificationTemplatService{

    @Autowired
    NotificationTemplateDao notificationTemplateDao;

    @Override
    public List<NotificationTemplate> getAllNotificationTemplateIsActive(Integer isDelete) {
        return notificationTemplateDao.getAllNotificationTemplateIsActive(isDelete);
    }

    @Override
    public void saveNotificationTemplate(NotificationTemplate notificationTemplate) {
        notificationTemplateDao.saveNotificationTemplate(notificationTemplate);
    }

    @Override
    public void deleteNotificationTemplate(NotificationTemplate notificationTemplate) {
        notificationTemplateDao.deleteNotificationTemplate(notificationTemplate);
    }

    @Override
    public NotificationTemplate getById(Integer id) {
        return notificationTemplateDao.getById(id);
    }

    @Override
    public NotificationTemplate getByType(Integer type,String langcode) {
        return notificationTemplateDao.getByType(type,langcode);
    }

    @Override
    public void updateNotificationTemplate(NotificationTemplate notificationTemplate) {
        notificationTemplateDao.updateNotificationTemplate(notificationTemplate);
    }

    @Override
    public List<NotificationTemplate> getListNotificationTemplateModifiedBy(UUID modifiedBy) {
        return notificationTemplateDao.getListNotificationTemplateModifiedBy(modifiedBy);
    }
}
