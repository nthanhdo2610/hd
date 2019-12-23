package com.tinhvan.hd.customer.repository;

import com.tinhvan.hd.customer.model.CustomerDevice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDeviceRepository extends CrudRepository<CustomerDevice,Long> {
}
