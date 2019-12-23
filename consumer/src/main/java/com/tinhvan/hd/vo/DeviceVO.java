package com.tinhvan.hd.vo;


import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class DeviceVO  implements HDPayload {
    private UUID customer_uuid;
    private String fcm_token;

    public UUID getCustomer_uuid() {
        return customer_uuid;
    }

    public void setCustomer_uuid(UUID customer_uuid) {
        this.customer_uuid = customer_uuid;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }


    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(fcm_token)) {
            throw new BadRequestException(400, "fcm_token is empty");
        }
        if (customer_uuid == null || customer_uuid.toString().equals("")){
            throw new BadRequestException(400, "customer_uuid is empty");
        }
    }
}
