package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class RequestConnectLDap implements HDPayload {

    private String email;

    private String password;

    private String ous;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOus() {
        return ous;
    }

    public void setOus(String ous) {
        this.ous = ous;
    }

    @Override
    public String toString() {
        return "RequestConnectLDap{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", ous='" + ous + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {

        if (HDUtil.isNullOrEmpty(email)) {
            throw new BadRequestException(5001,"Email is null or empty");
        }

        if (HDUtil.isNullOrEmpty(password)) {
            throw new BadRequestException(5002,"Password is null or empty");
        }

        if (HDUtil.isNullOrEmpty(ous)) {
            throw new BadRequestException(5003,"Ous is null or empty");
        }

    }
}
