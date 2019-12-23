package com.tinhvan.hd.promotion.repository;

import com.tinhvan.hd.promotion.entity.PromotionCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionCustomerRepository extends CrudRepository<PromotionCustomer,Long> {
}
