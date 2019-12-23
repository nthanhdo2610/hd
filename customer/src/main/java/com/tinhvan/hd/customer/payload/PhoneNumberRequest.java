package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class PhoneNumberRequest implements HDPayload {

    private String customerUuid;
    private String phoneNumber;   //check format
    private String type;

    @Override
    public void validatePayload() {
        try {
            UUID.fromString(customerUuid);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
        if (HDUtil.isNullOrEmpty(type) || type.equals("unlock"))
            type = "Acc.Temp.Pass";
        else
            type = "Acc.Reset.Pass";
        /*if(HDUtil.isNullOrEmpty(phoneNumber))
            throw new BadRequestException(1103, "empty telephone");
       */
        if (!HDUtil.isNullOrEmpty(phoneNumber))
            if (!HDUtil.isPhoneNumber(phoneNumber))
                throw new BadRequestException(1109, "invalid telephone");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PhoneNumberRequest{" +
                "customerUuid='" + customerUuid + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
