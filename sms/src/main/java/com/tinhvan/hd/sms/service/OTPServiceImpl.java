/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.service;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.sms.bean.OTPLimitRespon;
import com.tinhvan.hd.sms.bean.OTPMQ;
import com.tinhvan.hd.sms.bean.OTPVerifyResult;
import com.tinhvan.hd.sms.bean.SMSVerifyOTP;
import com.tinhvan.hd.sms.dao.OTPDAO;
import com.tinhvan.hd.sms.model.OTP;
//import com.tinhvan.hd.sms.config.RabbitConfig;
import com.tinhvan.hd.sms.repository.OtpRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
@Service
@Transactional(rollbackFor = InternalServerErrorException.class)
public class OTPServiceImpl implements OTPService {

    @Autowired
    OTPDAO otpdao;

    @Autowired
    private OtpRepository otpRepository;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OTPServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void create(OTP object) {
        otpRepository.save(object);
    }

    @Override
    public void update(OTP object) {
        otpRepository.save(object);
    }

//    @Override
//    public void callOTPMQ(OTPMQ object) {
//         rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_SMS_OTP, object);
//    }

    @Override
    public List<OTPVerifyResult> verifyOTP(SMSVerifyOTP object) {
        return otpdao.verifyOTP(object);
    }

    @Override
    public Date getTimeNow() {
        return otpdao.getTimeNow();
    }

    @Override
    public OTP findByUUID(UUID uuid) {
        return otpRepository.findByUuid(uuid).orElse(null);
    }

    @Override
    public OTPLimitRespon getLimitOTP(String customerUUID) {
        return otpdao.getLimitOTP(customerUUID);
    }

    @Override
    public String getPhoneNumber(SMSVerifyOTP object) {
        return otpdao.getPhoneNumber(object);
    }

}
