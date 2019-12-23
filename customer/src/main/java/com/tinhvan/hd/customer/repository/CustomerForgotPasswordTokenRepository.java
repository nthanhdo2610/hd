package com.tinhvan.hd.customer.repository;

import com.tinhvan.hd.customer.model.CustomerForgotPasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerForgotPasswordTokenRepository extends CrudRepository<CustomerForgotPasswordToken,Long> {

    CustomerForgotPasswordToken findCustomerForgotPasswordTokenByToken(String token);
}
