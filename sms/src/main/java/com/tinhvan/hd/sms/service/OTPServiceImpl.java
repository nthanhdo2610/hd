/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.service;

import com.tinhvan.hd.sms.bean.OTPLimitRespon;
import com.tinhvan.hd.sms.bean.OTPVerifyResult;
import com.tinhvan.hd.sms.bean.SMSVerifyOTP;
import com.tinhvan.hd.sms.dao.OTPDAO;
import com.tinhvan.hd.sms.model.OTP;
import com.tinhvan.hd.sms.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author LUUBI
 */
@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    OTPDAO otpdao;

    @Autowired
    private OtpRepository otpRepository;


    @Override
    public void create(OTP object) {
        otpRepository.save(object);
    }

    @Override
    public void update(OTP object) {
        otpRepository.save(object);
    }

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

    @Override
    public List<OTP> getListOtpByCode(String otpCode) {
        List<Integer> status = Arrays.asList(0, 2);
        return otpRepository.findAllByOtpCodeAndStatusInOrderByCreatedAtDesc(otpCode, status);
    }

    @Override
    public boolean checkLimitSendOtpRegisterByPhone(String deviceId, String phone) {
        return otpdao.checkLimitSendOtpRegisterByPhone(deviceId, phone);
//        String status = otpRepository.outputStatus(fcmToken,phone);
//        if (status.equals("true")) {
//            return true;
//        }else {
//            return false;
//        }
    }

    @Override
    public int updateCustomerLogAction(String customerId, String contractCode) {
       return otpdao.updateCustomerLogAction(customerId, contractCode);
    }
}
