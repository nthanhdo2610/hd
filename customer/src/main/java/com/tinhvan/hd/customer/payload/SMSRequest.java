package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.HDPayload;

import java.util.Arrays;

public class SMSRequest implements HDPayload {
    private String[] param;
    private String smsType;
    private String langCode;
    private String phoneNumber;

    @Override
    public String toString() {
        return "SMSRequest{" +
                "param=" + Arrays.toString(param) +
                ", smsType='" + smsType + '\'' +
                ", langCode='" + langCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

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

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void validatePayload() {

    }
}
