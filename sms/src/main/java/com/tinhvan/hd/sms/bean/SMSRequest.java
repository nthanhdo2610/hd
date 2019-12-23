/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Arrays;

/**
 *
 * @author LUUBI
 */
public class SMSRequest implements HDPayload {

    private String[] param;
    private String smsType;
    private String phoneNumber;
    private String langCode;

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    @Override
    public void validatePayload() {
        //validate phone_number
//        if (HDUtil.isNullOrEmpty(phoneNumber)) {
//            throw new BadRequestException(1236, "empty or null telephone");
//        }
//        if (!HDUtil.isPhoneNumber(phoneNumber)) {
//            throw new BadRequestException(1237, "invalid telephone");
//        }
//        //validate phone_number
//        if (HDUtil.isNullOrEmpty(content)) {
//            throw new BadRequestException(1201, "empty content");
//        }


    }

    @Override
    public String toString() {
        return "SMSRequest{" +
                "param=" + Arrays.toString(param) +
                ", smsType='" + smsType + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", langCode='" + langCode + '\'' +
                '}';
    }
}
