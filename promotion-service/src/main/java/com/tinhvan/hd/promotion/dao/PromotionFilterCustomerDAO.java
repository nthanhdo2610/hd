package com.tinhvan.hd.promotion.dao;

import com.tinhvan.hd.promotion.entity.PromotionFilterCustomer;

import java.util.List;
import java.util.UUID;

public interface PromotionFilterCustomerDAO {
//    void insert(PromotionFilterCustomer filterCustomer);
//    void update(PromotionFilterCustomer filterCustomer);
//    void delete(int id);
    List<PromotionFilterCustomer> findList(UUID PromotionId);
}
