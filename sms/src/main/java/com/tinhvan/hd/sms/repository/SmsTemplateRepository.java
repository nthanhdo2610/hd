package com.tinhvan.hd.sms.repository;

import com.tinhvan.hd.sms.model.SMSTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsTemplateRepository extends CrudRepository<SMSTemplate,Long> {

    Optional<SMSTemplate> findBySmsTypeAndLangCode(String smsType, String langCode);

}
