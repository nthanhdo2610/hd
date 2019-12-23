package com.tinhvan.hd.customer.repository;

import com.tinhvan.hd.customer.model.CustomerImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerImageRepository extends CrudRepository<CustomerImage,Long> {
}
