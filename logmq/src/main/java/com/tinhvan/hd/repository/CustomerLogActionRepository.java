package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.CustomerLogAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLogActionRepository extends CrudRepository<CustomerLogAction,Long> {
}
