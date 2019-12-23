package com.tinhvan.hd.staff.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class StaffFind implements HDPayload {
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void validatePayload() {
        //validate uuid
        if (uuid == null) {
            throw new BadRequestException(1200, "empty uuid");
        }
    }
}
