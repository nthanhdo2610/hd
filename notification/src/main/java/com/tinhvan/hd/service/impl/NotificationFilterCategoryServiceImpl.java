package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.NotificationFilterCategoryDao;
import com.tinhvan.hd.entity.NotificationFilterCategory;
import com.tinhvan.hd.repository.NotificationFilterCategoryRepository;
import com.tinhvan.hd.service.NotificationFilterCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationFilterCategoryServiceImpl implements NotificationFilterCategoryService {

//    @Autowired
//    NotificationFilterCategoryDao notificationFilterCategoryDao;

    @Autowired
    private NotificationFilterCategoryRepository filterCategoryRepository;


    @Override
    public void saveNotificationFilterCategory(NotificationFilterCategory notificationFilterCategory) {
        filterCategoryRepository.save(notificationFilterCategory);
    }
}
