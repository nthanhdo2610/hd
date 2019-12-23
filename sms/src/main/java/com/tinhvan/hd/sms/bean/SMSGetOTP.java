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
 *
 * @author LUUBI
 */
public class SMSGetOTP implements HDPayload {

    private String phoneNumber;
    private UUID customerUUID;
    private UUID contractUUID;
    private String otpType;
    private String contractCode;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(otpType)) {
            throw new BadRequestException(1216, "invalid type");
        }
        //validate phone_number
        if (!HDUtil.isPhoneNumber(phoneNumber)) {
            throw new BadRequestException(1217, "invalid phone");
        }
        //validate uuid
        if (customerUUID == null) {
            throw new BadRequestException(1200, "empty uuid");
        }
    }

    @Override
    public String toString() {
        return "phoneNumber='" + phoneNumber + '\'' +
                ", customerUUID=" + customerUUID +
                ", contractUUID=" + contractUUID +
                ", otpType='" + otpType + '\'' +
                ", contractCode='" + contractCode;
    }
}
