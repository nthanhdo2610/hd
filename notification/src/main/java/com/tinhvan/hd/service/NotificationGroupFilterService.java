package com.tinhvan.hd.service;


import com.tinhvan.hd.entity.NotificationGroupFilter;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NotificationGroupFilterService {

    void saveNotificationGroupFilter(NotificationGroupFilter notificationGroupFilter);

    List<NotificationGroupFilter> getListFilterByGroupId(Integer groupId);

}
