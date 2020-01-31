package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class VerifyOTPRegisterByPhone implements HDPayload {

    private String codeOTP;

    private String otpType;

    private String deviceId;

    public String getCodeOTP() {
        return codeOTP;
    }

    public void setCodeOTP(String codeOTP) {
        this.codeOTP = codeOTP;
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
        return "VerifyOTPRegisterByPhone{" +
                "codeOTP='" + codeOTP + '\'' +
                ", otpType='" + otpType + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {

        if (HDUtil.isNullOrEmpty(otpType)) {
            throw new BadRequestException(1231, "otp type is null or empty");
        }

        //validate code otp
        if (HDUtil.isNullOrEmpty(codeOTP)) {
            throw new BadRequestException(1218, "code otp is null");
        }

        if (!HDUtil.isNullOrEmpty(deviceId) && deviceId.length() > 300) {
            throw new BadRequestException(1131, "device id too long");
        }
    }
}
