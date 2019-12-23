package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class UuidRequest implements HDPayload {

    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void validatePayload() {

    }
}
