package com.tinhvan.hd.customer.repository;

import com.tinhvan.hd.customer.model.CustomerToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTokenRepository extends CrudRepository<CustomerToken,Long> {

    CustomerToken findByToken(String token);
}
