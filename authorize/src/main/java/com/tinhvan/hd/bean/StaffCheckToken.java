package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class StaffCheckToken implements HDPayload {
    private String token;
    private UUID uuid;

    public StaffCheckToken(String token, UUID uuid) {
        this.token = token;
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
