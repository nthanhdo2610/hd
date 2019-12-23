package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class CustomerDeviceRequest implements HDPayload {

    private String uuid;
    private String fcmToken;
    private String preferLanguage;

    @Override
    public void validatePayload() {
        if (!HDUtil.isNullOrEmpty(uuid)) {
            try {
                UUID.fromString(this.uuid);
            } catch (Exception e) {
                throw new BadRequestException(1106, "invalid id");
            }
        }
        if(HDUtil.isNullOrEmpty(fcmToken))
            throw new BadRequestException(1129, "invalid fcmToken");
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getPreferLanguage() {
        return preferLanguage;
    }

    public void setPreferLanguage(String preferLanguage) {
        this.preferLanguage = preferLanguage;
    }

    @Override
    public String toString() {
        return "CustomerDeviceRequest{" +
                "uuid='" + uuid + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", preferLanguage='" + preferLanguage + '\'' +
                '}';
    }
}
