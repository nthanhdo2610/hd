package com.tinhvan.hd.sms.repository;

import com.tinhvan.hd.sms.model.SMS;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends CrudRepository<SMS,Long> {
}
