package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.SendMailLogAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendMailLogActionRepository extends CrudRepository<SendMailLogAction, Integer> {
}
