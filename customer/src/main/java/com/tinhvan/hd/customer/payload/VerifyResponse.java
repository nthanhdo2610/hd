package com.tinhvan.hd.customer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class VerifyResponse {

    @JsonProperty("customerUuid")
    private UUID customerUuid;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("userName")
    private String userName;

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "VerifyResponse{" +
                "customerUuid=" + customerUuid +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
