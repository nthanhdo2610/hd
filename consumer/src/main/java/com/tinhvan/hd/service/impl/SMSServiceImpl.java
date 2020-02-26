/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.SMSDAO;
import com.tinhvan.hd.entity.SMS;
import com.tinhvan.hd.repository.SmsRepository;
import com.tinhvan.hd.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
@Service
public class SMSServiceImpl implements SMSService {

    @Autowired
    SMSDAO smsdao;

    @Autowired
    private SmsRepository smsRepository;

    @Override
    public SMS createOrUpdate(SMS object) {
       return smsRepository.save(object);
    }

    @Override
    public SMS findByUUID(UUID uuid) {
        return smsdao.findByUUID(uuid);
    }

    @Override
    public List<SMS> getList(int size) {
        return smsdao.getList(size);
    }

    @Override
    public List<SMS> getListUpdateSMSLogs() {
        return smsRepository.findTop20ByMessageIdIsNotNullAndSentAtIsNullOrderByCreatedAtDesc();
    }

}
