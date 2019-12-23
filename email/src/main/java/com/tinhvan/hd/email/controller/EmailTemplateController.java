/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.controller;

import com.tinhvan.hd.base.Log;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.email.bean.EmailTemplateDelete;
import com.tinhvan.hd.email.bean.EmailTemplateUpdate;
import com.tinhvan.hd.email.model.EmailTemplate;
import com.tinhvan.hd.email.service.EmailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.ResponseEntity.ok;

/**
 *
 * @author LUUBI
 */
@RestController
@RequestMapping("api/v1/email/template")
public class EmailTemplateController {
    @Autowired
    EmailTemplateService emailTemplateService;

    /**
     * Create EmailTemplate
     *
     * @param request object EmailTemplate contain send email
     * @return http status code and EmailTemplate
     */
    @PostMapping("/create")
    public ResponseEntity<?> createEmailTemplate(@RequestBody RequestDTO<EmailTemplate> request) {
        EmailTemplate emailTemplate = request.init();
        emailTemplate.setCreateAt(request.now());
        emailTemplate.setCreateBy(request.jwt().getUuid());
        emailTemplateService.create(emailTemplate);
        return ok(emailTemplate);
    }

    /*
    @PostMapping("/create")
    public ResponseEntity<?> createEmailTemplate(@RequestBody RequestDTO<EmailTemplate> request) {
        EmailTemplate emailTemplate = request.init();
        emailTemplate.setCreateAt(request.now());
        emailTemplate.setCreateBy(request.jwt().getUuid());
        emailTemplateService.create(emailTemplate);
        return ok(emailTemplate);
    }



    @PostMapping("/delete")
    public ResponseEntity<?> deleteEmailTemplate(@RequestBody RequestDTO<EmailTemplateDelete> request) {
        EmailTemplateDelete emailTemplateDelete = request.init();
        EmailTemplate emailTemplate = emailTemplateService.findByUUID(emailTemplateDelete.getUuid());
        if (emailTemplate != null) {
            emailTemplate.setStatus(0);
            emailTemplateService.update(emailTemplate);
        }
        return ok(emailTemplate);
    }

    @PostMapping("/list")
    public ResponseEntity<?> listEamilTemplate() {
        return ok(emailTemplateService.list());
    }

    @PostMapping("/details")
    public ResponseEntity<?> getEmailTemplateDetails(@RequestBody RequestDTO<EmailTemplateDelete> request) {
        EmailTemplateDelete emailTemplateDelete = request.init();
        return ok(emailTemplateService.findByUUID(emailTemplateDelete.getUuid()));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateEmailTemplate(@RequestBody RequestDTO<EmailTemplateUpdate> request) {
        EmailTemplateUpdate emailTemplateUpdate = request.init();
        EmailTemplate emailTemplate = emailTemplateService.findByUUID(emailTemplateUpdate.getId());
        if (emailTemplate != null) {
            emailTemplate.setEmailTemplateUpdate(emailTemplateUpdate);
            emailTemplate.setModifiedAt(request.now());
            emailTemplate.setModifiedBy(request.jwt().getUuid());
            emailTemplateService.update(emailTemplate);
        }
        return ok(emailTemplateUpdate);
    }
    */


}
