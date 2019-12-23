package com.tinhvan.hd.vo;

import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class NotificationGroupFilterVO implements HDPayload {

    private UUID modifiedBy;


    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public void validatePayload() {

    }
}
