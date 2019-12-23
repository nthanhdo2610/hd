package com.tinhvan.hd.customer.repository;

import com.tinhvan.hd.base.enities.CustomerLogAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLogActionRepository extends CrudRepository<CustomerLogAction,Long> {
}
