package com.tinhvan.hd.customer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateCustomerResponse {
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("status")
    private int status;

    public ValidateCustomerResponse(String userName, int status) {
        this.userName = userName;
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ValidateCustomerResponse{" +
                "userName='" + userName + '\'' +
                ", status=" + status +
                '}';
    }
}
