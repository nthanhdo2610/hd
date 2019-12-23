/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.controller;

import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.Invoker;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.sms.bean.SMSTemplateFind;
import com.tinhvan.hd.sms.bean.SMSTemplateList;
import com.tinhvan.hd.sms.bean.SMSTemplateListRespon;
import com.tinhvan.hd.sms.bean.SMSTemplateUpdate;
import com.tinhvan.hd.sms.model.SMSTemplate;
import com.tinhvan.hd.sms.service.SMSTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author LUUBI
 */
@RestController
@RequestMapping("api/v1/sms/template")
public class SMSTemplateController extends HDController {

    @Autowired
    SMSTemplateService sMSTemplateService;

    private Invoker invoker = new Invoker();
    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    /**
     * Created smsTemplate by values request
     *
     * @param request object SMSTemplate contain information created
     *
     * @return SMSTemplate create
     */
    @PostMapping("/create")
    public ResponseEntity<?> createSmsTemplate(@RequestBody RequestDTO<SMSTemplate> request) {
        SMSTemplate sMSTemplate = request.init();
        Date date = request.now();
        UUID uuid = null;
        if (request.jwt() != null) {
            uuid = request.jwt().getUuid();
        }
        sMSTemplate.init();
        sMSTemplate.setStatus(1);
        sMSTemplate.setCreatedAt(date);
        sMSTemplate.setCreatedBy(uuid);
        sMSTemplate.setModifiedAt(date);
        sMSTemplate.setModifiedBy(uuid);
        sMSTemplateService.create(sMSTemplate);
        return ok(sMSTemplate);
    }

    /**
     * Update smsTemplate by values request
     *
     * @param request object SMSTemplateUpdate contain information update
     *
     * @return SMSTemplate update
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateSmsTemplate(@RequestBody RequestDTO<SMSTemplateUpdate> request) {
        SMSTemplateUpdate sMSTemplateUpdate = request.init();
        SMSTemplate sMSTemplate = sMSTemplateService.findByUUID(sMSTemplateUpdate.getUuid());
        UUID uuid = null;
        if (request.jwt() != null) {
            uuid = request.jwt().getUuid();
        }
        if (sMSTemplate != null) {
            sMSTemplate.setSMSTemplateUpdate(sMSTemplateUpdate);
            sMSTemplate.setModifiedAt(request.now());
            sMSTemplate.setModifiedBy(uuid);
            sMSTemplateService.update(sMSTemplate);
        }
        return ok(sMSTemplate);
    }

    /**
     * Update smsTemplate by values request
     *
     * @param request object SMSTemplateUpdate contain information find
     *
     * @return SMSTemplate find
     */
    @PostMapping("/find")
    public ResponseEntity<?> findByUUID(@RequestBody RequestDTO<SMSTemplateFind> request) {
        SMSTemplateFind smsTemplateFind = request.init();
        return ok(sMSTemplateService.findByUUID(smsTemplateFind.getUuid()));
    }

    /**
     * get list smsTemplate by values request
     *
     * @param request object SMSTemplateList contain information get list
     *
     * @return SMSTemplate list
     */
    @PostMapping("/list")
    public ResponseEntity<?> list(@RequestBody RequestDTO<SMSTemplateList> request) {
        SMSTemplateList smsTemplateList = request.init();
        SMSTemplateListRespon staffSearchRespon = sMSTemplateService.getList(smsTemplateList);
        return ok(staffSearchRespon);
    }

//    private void invokeCheckToken(IdPayload idPayload) {
//
//        ResponseDTO<String> rs = invoker.call(urlStaffRequest+"/check_token", idPayload, new ParameterizedTypeReference<ResponseDTO<String>>() {
//        });
//        if (rs.getCode() != 200) {
//            throw new BadRequestException(401, "unauthorized");
//        }
//    }

}
