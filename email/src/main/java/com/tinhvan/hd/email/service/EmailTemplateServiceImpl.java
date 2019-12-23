/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.service;

import com.tinhvan.hd.email.model.EmailTemplate;
import java.util.List;
import java.util.UUID;

import com.tinhvan.hd.email.repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tinhvan.hd.email.dao.EmailTemplateDAO;

/**
 *
 * @author LUUBI
 */
@Service
public class EmailTemplateServiceImpl implements EmailTemplateService{
    @Autowired
    EmailTemplateDAO mailTemplateDAO;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Override
    public void create(EmailTemplate object) {
        emailTemplateRepository.save(object);
    }

    @Override
    public void update(EmailTemplate object) {
        emailTemplateRepository.save(object);
    }

    @Override
    public EmailTemplate findByTypeAndLangCode(int type, String langCode) {
        return mailTemplateDAO.findByTypeAndLangCode(type, langCode);
    }

    @Override
    public List<EmailTemplate> list() {
        return mailTemplateDAO.list();
    }


    @Override
    public EmailTemplate findByUUID(UUID uuid) {
        return mailTemplateDAO.findByUUID(uuid);
    }

    @Override
    public EmailTemplate findByEmailTypeAndAndLangCodeAndStatus(String emailType, String langCode, int status) {
        return emailTemplateRepository.findByEmailTypeAndAndLangCodeAndStatus(emailType, langCode, status).orElse(null);
    }

}
