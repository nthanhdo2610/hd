package com.tinhvan.hd.customer.dao;

import com.tinhvan.hd.customer.model.CustomerToken;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CustomerTokenDAO {

//    void insert(CustomerToken customerToken);
    CustomerToken findByToken(String token);
    void disable(UUID customerUuid, String environment, Date deletedAt);
    void disableAllByCustomer(UUID customerUuid, Date deletedAt);
    List<CustomerToken> getCustomerTokenByCustomerUuidAndEnvironments(UUID customerUuid, String environment);
}
