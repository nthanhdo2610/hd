package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class SmsOtpDTORequest implements HDPayload {
    private String customerUUID;
    private String phoneNumber;
    private String codeOTP;
    private int optType;

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(customerUUID))
            throw new BadRequestException(1106);
        try {
            UUID.fromString(this.customerUUID);
        } catch (Exception e) {
            throw new BadRequestException(1106);
        }
        if(HDUtil.isNullOrEmpty(codeOTP))
            throw new BadRequestException(1218);
    }

    public String getCustomerUUID() {
        return customerUUID;
    }

    public void setCustomerUUID(String customerUUID) {
        this.customerUUID = customerUUID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCodeOTP() {
        return codeOTP;
    }

    public void setCodeOTP(String codeOTP) {
        this.codeOTP = codeOTP;
    }

    public int getOptType() {
        return optType;
    }

    public void setOptType(int optType) {
        this.optType = optType;
    }

    @Override
    public String toString() {
        return "SmsOtpDTORequest{" +
                "customerUUID='" + customerUUID + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", codeOTP='" + codeOTP + '\'' +
                ", optType=" + optType +
                '}';
    }
}
