package com.tinhvan.hd.promotion.service.impl;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.promotion.dao.PromotionFilterCustomerDAO;
import com.tinhvan.hd.promotion.entity.PromotionFilterCustomer;
import com.tinhvan.hd.promotion.repository.PromotionFilterCustomerRepository;
import com.tinhvan.hd.promotion.service.PromotionFilterCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PromotionFilterCustomerServiceImpl implements PromotionFilterCustomerService {

    @Autowired
    private PromotionFilterCustomerDAO PromotionFilterCustomerDAO;

    @Autowired
    private PromotionFilterCustomerRepository promotionFilterCustomerRepository;

    @Override
    public void insert(PromotionFilterCustomer filterCustomer) {
        promotionFilterCustomerRepository.save(filterCustomer);
    }

    @Override
    public void update(PromotionFilterCustomer filterCustomer) {
        promotionFilterCustomerRepository.save(filterCustomer);
    }

    @Override
    public void delete(int id) {
        promotionFilterCustomerRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public List<PromotionFilterCustomer> findList(UUID PromotionId) {
        return PromotionFilterCustomerDAO.findList(PromotionId);
    }
}
