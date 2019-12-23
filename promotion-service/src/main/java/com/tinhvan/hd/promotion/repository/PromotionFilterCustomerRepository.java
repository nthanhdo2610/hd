package com.tinhvan.hd.promotion.repository;

import com.tinhvan.hd.promotion.entity.PromotionFilterCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionFilterCustomerRepository extends CrudRepository<PromotionFilterCustomer,Long> {
}
