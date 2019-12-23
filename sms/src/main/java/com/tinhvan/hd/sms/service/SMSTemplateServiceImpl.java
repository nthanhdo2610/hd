/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.service;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.sms.bean.SMSTemplateList;
import com.tinhvan.hd.sms.bean.SMSTemplateListRespon;
import com.tinhvan.hd.sms.dao.SMSTemplateDAO;
import com.tinhvan.hd.sms.model.SMSTemplate;
import com.tinhvan.hd.sms.repository.SmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
@Service
@Transactional(rollbackFor = InternalServerErrorException.class)
public class SMSTemplateServiceImpl implements SMSTemplateService {

    @Autowired
    SMSTemplateDAO sMSTemplateDAO;

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Override
    public void create(SMSTemplate object) {
        smsTemplateRepository.save(object);
    }

    @Override
    public void update(SMSTemplate object) {
        smsTemplateRepository.save(object);
    }

    @Override
    public SMSTemplate findByUUID(UUID uuid) {
        return sMSTemplateDAO.findByUUID(uuid);
    }

    @Override
    public SMSTemplate findByTypeAndLangCode(String smsType, String langCode) {
//       return sMSTemplateDAO.findByTypeAndLangCode(smsType, langCode);
        return smsTemplateRepository.findBySmsTypeAndLangCode(smsType, langCode).orElse(null);
    }

    @Override
    public SMSTemplateListRespon getList(SMSTemplateList smsTemplateList) {
        return sMSTemplateDAO.getList(smsTemplateList);
    }


}
