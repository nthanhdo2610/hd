package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.NotificationGroupQueue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationGroupQueueRepository extends CrudRepository<NotificationGroupQueue,Long> {
    List<NotificationGroupQueue> findAllByStatus(Integer status);
}
