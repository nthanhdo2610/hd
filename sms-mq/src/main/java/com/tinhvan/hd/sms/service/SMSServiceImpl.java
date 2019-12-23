/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.service;

import com.tinhvan.hd.sms.dao.SMSDAO;
import com.tinhvan.hd.sms.model.SMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author LUUBI
 */
@Service
public class SMSServiceImpl implements SMSService{

    @Autowired
    SMSDAO smsdao;

    @Override
    public SMS createOrUpdate(SMS object) {
       return smsdao.createOrUpdate(object);
    }

}
