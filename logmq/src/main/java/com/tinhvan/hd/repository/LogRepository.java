package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.LogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends CrudRepository<LogEntity,Long> {
}
