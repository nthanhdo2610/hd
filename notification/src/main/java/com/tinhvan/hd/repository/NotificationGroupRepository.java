package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.NotificationGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationGroupRepository extends CrudRepository<NotificationGroup,Long> {

}
