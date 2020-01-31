package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Arrays;

public class GetOtpByRegisterPhone implements HDPayload {

    private String phoneNumber;

    private String deviceId;

    private String otpType;


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "GetOtpByRegisterPhone{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", otpType='" + otpType + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {
        //validate phone_number
        if (!HDUtil.isPhoneNumber(phoneNumber)) {
            throw new BadRequestException(1217, "invalid phone");
        }
        if (!HDUtil.isNullOrEmpty(deviceId) && deviceId.length() > 300) {
            throw new BadRequestException(1131, "device id too long");
        }
    }
}
