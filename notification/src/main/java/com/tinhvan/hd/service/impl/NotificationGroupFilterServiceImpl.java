package com.tinhvan.hd.service.impl;


import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.dao.NotificationGroupFilterDao;
import com.tinhvan.hd.entity.NotificationGroupFilter;
import com.tinhvan.hd.repository.NotificationGroupFilterRepository;
import com.tinhvan.hd.service.NotificationGroupFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class NotificationGroupFilterServiceImpl implements NotificationGroupFilterService {

//    @Autowired
//    NotificationGroupFilterDao notificationGroupFilterDao;

    @Autowired
    private NotificationGroupFilterRepository groupFilterRepository;


    @Override
    public void saveNotificationGroupFilter(NotificationGroupFilter notificationGroupFilter) {
        groupFilterRepository.save(notificationGroupFilter);
    }

    @Override
    public List<NotificationGroupFilter> getListFilterByGroupId(Integer groupId) {
        return groupFilterRepository.findAllByNotificationGroupId(groupId);
    }
}
