package com.tinhvan.hd.customer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class SignInRequest implements HDPayload {

    private String username;
    private String password;
    @JsonProperty
    private boolean encryptPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(boolean encryptPassword) {
        this.encryptPassword = encryptPassword;
    }

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(username))
            throw new BadRequestException(1110, "invalid username or password");
        if(HDUtil.isNullOrEmpty(password))
            throw new BadRequestException(1110, "invalid username or password");
    }

    @Override
    public String toString() {
        return "SignInRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", encryptPassword=" + encryptPassword +
                '}';
    }
}
