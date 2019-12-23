package com.tinhvan.hd.customer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.customer.model.Customer;

public class AuthResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("encrypted")
    private String encrypted;

    public AuthResponse(String token, Customer customer, String encrypted) {
        this.token = token;
        this.customer = customer;
        this.encrypted = encrypted;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                ", customer=" + customer +
                ", encrypted='" + encrypted + '\'' +
                '}';
    }
}
