/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.service;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.sms.bean.CustomerLogAction;
import com.tinhvan.hd.sms.dao.SMSDAO;
import com.tinhvan.hd.sms.invoke.ContractEsignedRequest;
import com.tinhvan.hd.sms.model.SMS;

import java.util.List;

import com.tinhvan.hd.sms.config.RabbitConfig;
import com.tinhvan.hd.sms.repository.SmsRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LUUBI
 */
@Service
@Transactional(rollbackFor = InternalServerErrorException.class)
public class SMSServiceImpl implements SMSService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SMSServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    SMSDAO smsdao;

    @Autowired
    private SmsRepository smsRepository;

    @Override
    public void create(SMS object) {
        smsRepository.save(object);
    }

    @Override
    public void update(SMS object) {
        smsRepository.save(object);
    }

    @Override
    public void mqSendSMS() {
        String test = "test";
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_SMS_QUEUE, test);
    }

    @Override
    public void mqVerifyOTPTypeEsign(ContractEsignedRequest object) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_VERIFY_OTP_QUEUE, object);

    }

    @Override
    public void createMQ(CustomerLogAction customerLogAction) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_CUSTOMER_LOG_ACTION, customerLogAction);
    }

}
