package com.tinhvan.hd.customer.repository;

import com.tinhvan.hd.customer.model.CustomerToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerTokenRepository extends CrudRepository<CustomerToken,Long> {

    CustomerToken findByToken(String token);

    CustomerToken findByCustomerUuidAndStatus(UUID customerUuid,Integer status);
}
