package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.vo.NotificationTemplateFilterVO;
import com.tinhvan.hd.vo.NotificationTemplateList;
import com.tinhvan.hd.vo.NotificationTemplateListRespon;

import java.util.List;

public interface NotificationTemplatService {

    List<NotificationTemplate> getAllNotificationTemplateIsActive(Integer isDelete);

    void saveNotificationTemplate(NotificationTemplate notificationTemplate);

    void deleteNotificationTemplate(NotificationTemplate notificationTemplate);

    NotificationTemplate getById(Integer id);

    NotificationTemplate getByType(Integer type,String langcode);

    NotificationTemplate createOrUpdate(NotificationTemplate notificationTemplate);

    List<NotificationTemplate> getListNotificationTemplateModifiedBy(NotificationTemplateFilterVO templateFilterVO);

    NotificationTemplateListRespon getList(NotificationTemplateList notificationTemplateList);

    long countNotificationTemplateModifiedBy(NotificationTemplateFilterVO templateFilterVO);
}
