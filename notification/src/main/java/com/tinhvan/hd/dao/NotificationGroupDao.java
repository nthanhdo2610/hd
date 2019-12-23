package com.tinhvan.hd.dao;


import com.tinhvan.hd.entity.NotificationGroup;
import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.vo.NotificationGroupFilterVO;

import java.util.List;
import java.util.UUID;

public interface NotificationGroupDao {

//    void saveNotificationGroup(NotificationGroup notificationGroup);
//
//
//    void updateNotificationGroup(NotificationGroup notificationGroup);
//
//    void deleteNotificationGroup(NotificationGroup notificationGroup);
//
//    NotificationGroup getNotificationGroupById(Integer groupId);

    List<NotificationGroup> getListNotificationGroupModifiedBy(NotificationGroupFilterVO groupFilterVO);

    long countNotificationGroup(NotificationGroupFilterVO groupFilterVO);

}
