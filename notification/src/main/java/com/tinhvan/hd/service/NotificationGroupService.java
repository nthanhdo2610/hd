package com.tinhvan.hd.service;


import com.tinhvan.hd.entity.NotificationGroup;
import com.tinhvan.hd.vo.NotificationGroupFilterVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface NotificationGroupService {

    void saveNotificationGroup(NotificationGroup notificationGroup);

    void updateNotificationGroup(NotificationGroup notificationGroup);

    void deleteNotificationGroup(NotificationGroup notificationGroup);

    NotificationGroup getNotificationGroupById(Integer groupId);

    List<NotificationGroup> getListNotificationGroupModifiedBy(NotificationGroupFilterVO groupFilterVO);

    long countNotificationGroup(NotificationGroupFilterVO groupFilterVO);

}
