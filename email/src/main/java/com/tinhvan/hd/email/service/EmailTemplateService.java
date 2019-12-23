/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.service;

import com.tinhvan.hd.email.model.EmailTemplate;

import java.util.List;
import java.util.UUID;

/**
 * @author LUUBI
 */
public interface EmailTemplateService {

    void create(EmailTemplate object);

    void update(EmailTemplate object);

    EmailTemplate findByTypeAndLangCode(int type, String langCode);

    List<EmailTemplate> list();

    EmailTemplate findByUUID(UUID uuid);

    EmailTemplate findByEmailTypeAndAndLangCodeAndStatus(String emailType, String langCode, int status);
}
