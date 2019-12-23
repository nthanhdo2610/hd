/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.sms.model.OTP;

/**
 *
 * @author LUUBI
 */
public class OTPMQ {
    private OTP otp;
    private String message;

    public OTPMQ() {
    }

    public OTPMQ(OTP otp, String message) {
        this.otp = otp;
        this.message = message;
    }

    public OTP getOtp() {
        return otp;
    }

    public void setOtp(OTP otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
