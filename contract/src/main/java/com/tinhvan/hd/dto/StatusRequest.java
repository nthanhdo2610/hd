package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class StatusRequest implements HDPayload {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void validatePayload() {

        if (HDUtil.isNullOrEmpty(status)) {
            throw new BadRequestException(1422,"Status is null or empty");
        }
    }
}
