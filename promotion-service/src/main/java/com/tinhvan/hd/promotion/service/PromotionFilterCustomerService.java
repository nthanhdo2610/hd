package com.tinhvan.hd.promotion.service;

import com.tinhvan.hd.promotion.entity.PromotionFilterCustomer;

import java.util.List;
import java.util.UUID;

public interface PromotionFilterCustomerService {
    void insert(PromotionFilterCustomer filterCustomer);
    void update(PromotionFilterCustomer filterCustomer);
    void delete(int id);
    List<PromotionFilterCustomer> findList(UUID PromotionId);
}
