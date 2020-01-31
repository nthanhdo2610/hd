package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class RegisterByPhonePayload implements HDPayload {

    private String phone;

    private String deviceId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "RegisterByPhonePayload{" +
                "phone='" + phone + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {
        //validate phone_number
        if (!HDUtil.isPhoneNumber(phone)) {
            throw new BadRequestException(1217, "invalid phone");
        }

        if (!HDUtil.isNullOrEmpty(deviceId) && deviceId.length() > 300) {
            throw new BadRequestException(1131, "device id too long");
        }
    }
}
