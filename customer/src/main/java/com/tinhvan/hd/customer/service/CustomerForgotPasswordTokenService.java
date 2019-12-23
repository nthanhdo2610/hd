package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.customer.model.CustomerForgotPasswordToken;

public interface CustomerForgotPasswordTokenService {
    void insert(CustomerForgotPasswordToken forgotPasswordToken);
    CustomerForgotPasswordToken findActive(String token);
    void update(CustomerForgotPasswordToken forgotPasswordToken);
}
