package com.tinhvan.hd.customer.repository;

import com.tinhvan.hd.customer.model.CustomerLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLogRepository extends CrudRepository<CustomerLog,Long> {
}
