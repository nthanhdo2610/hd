package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.dao.NotificationTemplateDao;
import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.repository.NotificationTemplateRepository;
import com.tinhvan.hd.service.NotificationTemplatService;
import com.tinhvan.hd.vo.NotificationTemplateFilterVO;
import com.tinhvan.hd.vo.NotificationTemplateList;
import com.tinhvan.hd.vo.NotificationTemplateListRespon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationTemplatServiceImpl implements NotificationTemplatService{

    @Autowired
    NotificationTemplateDao notificationTemplateDao;

    @Autowired
    NotificationTemplateRepository notificationTemplateRepository;

    @Override
    public List<NotificationTemplate> getAllNotificationTemplateIsActive(Integer isDelete) {
        return notificationTemplateDao.getAllNotificationTemplateIsActive(isDelete);
    }

    @Override
    public void saveNotificationTemplate(NotificationTemplate notificationTemplate) {
        notificationTemplateRepository.save(notificationTemplate);
    }

    @Override
    public void deleteNotificationTemplate(NotificationTemplate notificationTemplate) {
        notificationTemplateRepository.delete(notificationTemplate);
    }

    @Override
    public NotificationTemplate getById(Integer id) {
        return notificationTemplateRepository.findById(id).orElse(null);
    }

    @Override
    public NotificationTemplate getByType(Integer type,String langcode) {
        return notificationTemplateDao.getByType(type,langcode);
    }

    @Override
    public NotificationTemplate createOrUpdate(NotificationTemplate notificationTemplate) {
        return notificationTemplateRepository.save(notificationTemplate);
    }

    @Override
    public List<NotificationTemplate> getListNotificationTemplateModifiedBy(NotificationTemplateFilterVO templateFilterVO) {
        return notificationTemplateDao.getListNotificationTemplateModifiedBy(templateFilterVO);
    }

    @Override
    public NotificationTemplateListRespon getList(NotificationTemplateList notificationTemplateList) {
        return notificationTemplateDao.getList(notificationTemplateList);
    }

    @Override
    public long countNotificationTemplateModifiedBy(NotificationTemplateFilterVO templateFilterVO) {
        return notificationTemplateDao.countNotificationTemplateModifiedBy(templateFilterVO);
    }
}
