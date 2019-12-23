package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.NotificationFilterCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationFilterCategoryRepository extends CrudRepository<NotificationFilterCategory,Long> {
}
