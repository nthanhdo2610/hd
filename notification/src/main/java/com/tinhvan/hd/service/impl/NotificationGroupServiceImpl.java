package com.tinhvan.hd.service.impl;


import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.dao.NotificationGroupDao;
import com.tinhvan.hd.entity.NotificationGroup;
import com.tinhvan.hd.repository.NotificationGroupRepository;
import com.tinhvan.hd.service.NotificationGroupService;
import com.tinhvan.hd.vo.NotificationGroupFilterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class NotificationGroupServiceImpl implements NotificationGroupService {

    @Autowired
    NotificationGroupDao notificationGroupDao;

    @Autowired
    private NotificationGroupRepository notificationGroupRepository;


    @Override
    public void saveNotificationGroup(NotificationGroup notificationGroup) {
        notificationGroupRepository.save(notificationGroup);
    }


    @Override
    public void updateNotificationGroup(NotificationGroup notificationGroup) {
        notificationGroupRepository.save(notificationGroup);
    }

    @Override
    public void deleteNotificationGroup(NotificationGroup notificationGroup) {
        notificationGroupRepository.delete(notificationGroup);
    }

    @Override
    public NotificationGroup getNotificationGroupById(Integer groupId) {
        return notificationGroupRepository.findById(Long.valueOf(groupId)).orElse(null);
    }

    @Override
    public List<NotificationGroup> getListNotificationGroupModifiedBy(NotificationGroupFilterVO groupFilterVO) {
        return notificationGroupDao.getListNotificationGroupModifiedBy(groupFilterVO);
    }

    @Override
    public long countNotificationGroup(NotificationGroupFilterVO groupFilterVO) {
        return notificationGroupDao.countNotificationGroup(groupFilterVO);
    }
}
