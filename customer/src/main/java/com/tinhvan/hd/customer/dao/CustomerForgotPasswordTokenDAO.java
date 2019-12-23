package com.tinhvan.hd.customer.dao;

import com.tinhvan.hd.customer.model.CustomerForgotPasswordToken;

import java.util.List;
import java.util.UUID;

public interface CustomerForgotPasswordTokenDAO {
//    void insert(CustomerForgotPasswordToken forgotPasswordToken);
    void delete(CustomerForgotPasswordToken forgotPasswordToken);
//    void update(CustomerForgotPasswordToken forgotPasswordToken);
    CustomerForgotPasswordToken findActive(String token);

    List<CustomerForgotPasswordToken> getForgotPasswordByCustomerUuidAndStatus (UUID customerUuid);
}
