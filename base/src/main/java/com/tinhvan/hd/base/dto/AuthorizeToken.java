package com.tinhvan.hd.base.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class AuthorizeToken implements HDPayload {
    private String token;
    private String api;
    private UUID userId;

    @Override
    public void validatePayload() {

    }

    public AuthorizeToken(String token, String api, UUID userId) {
        this.token = token;
        this.api = api;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
