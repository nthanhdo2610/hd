/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

/**
 * @author LUUBI
 */
public class SMSVerifyOTP implements HDPayload {

    private String codeOTP;
    private String otpType;
    private UUID customerUUID;
    private UUID contractUUID;
    private String contractCode;

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }

    public UUID getCustomerUUID() {
        return customerUUID;
    }

    public void setCustomerUUID(UUID customerUUID) {
        this.customerUUID = customerUUID;
    }

    public UUID getContractUUID() {
        return contractUUID;
    }

    public void setContractUUID(UUID contractUUID) {
        this.contractUUID = contractUUID;
    }

    public String getCodeOTP() {
        return codeOTP;
    }

    public void setCodeOTP(String codeOTP) {
        this.codeOTP = codeOTP;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Override
    public String toString() {
        return
                "codeOTP='" + codeOTP + '\'' +
                ", otpType='" + otpType + '\'' +
                ", customerUUID=" + customerUUID +
                ", contractUUID=" + contractUUID +
                ", contractCode='" + contractCode;
    }

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(otpType)) {
            throw new BadRequestException(1231, "otp type is null or empty");
        }
//        if (!otpType.equals("AccVeri") && !otpType.equals("AccResPass") && !otpType.equals("SignOTP")
//                && !otpType.equals("Sign.OTP.Appendix") && !otpType.equals("Sign.Conf.Appendix")) {
//            throw new BadRequestException(1230, "invalid otp type");
//        }
        //validate code otp
        if (HDUtil.isNullOrEmpty(codeOTP)) {
            throw new BadRequestException(1218, "code otp is null");
        }
        //validate uuid
        if (customerUUID == null) {
            throw new BadRequestException(1200, "empty uuid");
        }
    }


}
