package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.LogMail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMailRepository extends CrudRepository<LogMail, Integer> {
}
