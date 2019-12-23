/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.vo;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

/**
 * @author LUUBI
 */
public class SMSVerifyOTP {

    private String codeOTP;
    private String otpType;
    private UUID customerUUID;
    private UUID contractUUID;

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


}
