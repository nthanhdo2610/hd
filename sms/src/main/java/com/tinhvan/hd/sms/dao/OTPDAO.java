/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.dao;

import com.tinhvan.hd.sms.bean.OTPLimitRespon;
import com.tinhvan.hd.sms.bean.OTPVerifyResult;
import com.tinhvan.hd.sms.bean.SMSVerifyOTP;
import com.tinhvan.hd.sms.model.OTP;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author LUUBI
 */
public interface OTPDAO {

//    void create(OTP object);
//
//    void update(OTP object);

    List<OTPVerifyResult> verifyOTP(SMSVerifyOTP object);

    Date getTimeNow();

    OTP findByUUID(UUID uuid);

    OTPLimitRespon getLimitOTP(String customerUUID);

    String getPhoneNumber(SMSVerifyOTP object);

}
