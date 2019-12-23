package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.NotificationGroupFilter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationGroupFilterRepository extends CrudRepository<NotificationGroupFilter,Long> {
    List<NotificationGroupFilter> findAllByNotificationGroupId(Integer notificationGroupId);
}
