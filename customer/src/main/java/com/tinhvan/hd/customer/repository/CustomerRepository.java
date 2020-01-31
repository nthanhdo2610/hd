package com.tinhvan.hd.customer.repository;

import com.tinhvan.hd.customer.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {

    Customer findCustomerByUsername(String username);

    Customer findCustomerByEmail(String email);

    Customer findCustomerByPhoneNumberOriginAndRegisterType(String phoneNumber,Integer registerType);

    Integer countAllByStatusNot(Integer status);

    Integer countAllByStatus(Integer status);
}
