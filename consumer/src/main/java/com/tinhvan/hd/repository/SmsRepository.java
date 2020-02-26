package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.SMS;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsRepository extends CrudRepository<SMS,Long> {
List<SMS> findTop20ByMessageIdIsNotNullAndSentAtIsNullOrderByCreatedAtDesc();
}
