package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class CustomerSignUp implements HDPayload {


    private String phoneNumber;

    private String username;

    private String userNameFm;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserNameFm() {
        return userNameFm;
    }

    public void setUserNameFm(String userNameFm) {
        this.userNameFm = userNameFm;
    }

    @Override
    public void validatePayload() {

    }
}
