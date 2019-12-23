package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.vo.NotificationTemplateFilterVO;
import com.tinhvan.hd.vo.NotificationTemplateList;
import com.tinhvan.hd.vo.NotificationTemplateListRespon;

import java.util.List;
import java.util.UUID;

public interface NotificationTemplateDao {

    List<NotificationTemplate> getAllNotificationTemplateIsActive(Integer isDelete);

//    void saveNotificationTemplate(NotificationTemplate notificationTemplate);
//
//    void deleteNotificationTemplate(NotificationTemplate notificationTemplate);
//
//    NotificationTemplate getById(Integer id);

    NotificationTemplate getByType(Integer type, String langcode);

    //NotificationTemplate createOrUpdate(NotificationTemplate notificationTemplate);

    List<NotificationTemplate> getListNotificationTemplateModifiedBy(NotificationTemplateFilterVO templateFilterVO);

    NotificationTemplateListRespon getList(NotificationTemplateList notificationTemplateList);

    long countNotificationTemplateModifiedBy(NotificationTemplateFilterVO templateFilterVO);
}
