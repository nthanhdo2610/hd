package com.tinhvan.hd.customer.repository;


import com.tinhvan.hd.customer.model.RegisterByPhone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterByPhoneRepository extends CrudRepository<RegisterByPhone,Long> {

    RegisterByPhone findByPhoneAndStatus(String phone,Integer status);

    List<RegisterByPhone> findAllByPhone(String phone);
}
