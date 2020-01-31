package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.customer.model.CustomerToken;

import java.util.Date;
import java.util.UUID;

public interface CustomerTokenService {
    void insert(CustomerToken customerToken);
    CustomerToken findByToken(String token);
    void disable(UUID customerUuid, String environment, Date deletedAt);
    void disableAllByCustomer(UUID customerUuid, Date deletedAt);

    CustomerToken getCustomerTokenByCustomerUuidAndStatus(UUID customerUuid,Integer status);
}
