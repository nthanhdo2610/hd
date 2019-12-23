package com.tinhvan.hd.email.repository;

import com.tinhvan.hd.email.model.EmailTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends CrudRepository<EmailTemplate,Integer> {

    Optional<EmailTemplate> findByEmailTypeAndAndLangCodeAndStatus(String emailType, String langCode, int status);
}
