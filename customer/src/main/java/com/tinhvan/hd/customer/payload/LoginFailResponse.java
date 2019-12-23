package com.tinhvan.hd.customer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginFailResponse {
    @JsonProperty("seconds")
    private int seconds;
    private int countLoginFails;

    public LoginFailResponse(int seconds, int count) {
        this.seconds = seconds;
        this.countLoginFails = count;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getCountLoginFails() {
        return countLoginFails;
    }

    public void setCountLoginFails(int countLoginFails) {
        this.countLoginFails = countLoginFails;
    }

    @Override
    public String toString() {
        return "LoginFailResponse{" +
                "seconds=" + seconds +
                ", countLoginFails=" + countLoginFails +
                '}';
    }
}
