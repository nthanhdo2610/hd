package com.tinhvan.hd.dao;


import com.tinhvan.hd.entity.NotificationGroupFilter;

import java.util.List;

public interface NotificationGroupFilterDao {

    void saveNotificationGroupFilter(NotificationGroupFilter notificationGroupFilter);

    List<NotificationGroupFilter> getListFilterByGroupId(Integer groupId);
}
