package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.NotificationAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationActionRepository extends CrudRepository<NotificationAction,Long> {
    List<NotificationAction> findAllByCustomerUuidAndIsRead(UUID customerUuid,Integer isRead);
}
