package com.tinhvan.hd.promotion.service.impl;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.promotion.dao.PromotionCustomerDao;
import com.tinhvan.hd.promotion.entity.PromotionCustomer;
import com.tinhvan.hd.promotion.repository.PromotionCustomerRepository;
import com.tinhvan.hd.promotion.service.PromotionCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Service
public class PromotionCustomerServiceImpl implements PromotionCustomerService {

    @Autowired
    private PromotionCustomerDao PromotionCustomerDao;

    @Autowired
    private PromotionCustomerRepository promotionCustomerRepository;


    @Override
    public void insert(PromotionCustomer PromotionCustomer) {
        promotionCustomerRepository.save(PromotionCustomer);
    }

    @Override
    public void update(PromotionCustomer PromotionCustomer) {
        promotionCustomerRepository.save(PromotionCustomer);
    }

    @Override
    public void delete(PromotionCustomer PromotionCustomer) {
        promotionCustomerRepository.delete(PromotionCustomer);
    }

    @Override
    public PromotionCustomer findById(UUID id) {
        return PromotionCustomerDao.findById(id);
    }

    @Override
    public List<PromotionCustomer> getListPromotionCustomerByCustomer(UUID customerUuid) {
        return PromotionCustomerDao.getListPromotionCustomerByCustomer(customerUuid);
    }

    @Override
    public List<PromotionCustomer> getListPromotionCustomerByPromotionId(UUID promotionId) {
        return PromotionCustomerDao.getListPromotionCustomerByPromotionId(promotionId);
    }

    @Override
    public PromotionCustomer find(UUID promotionId, UUID customerUuid) {
        return PromotionCustomerDao.find(promotionId, customerUuid);
    }

    @Override
    public void saveAll(List<PromotionCustomer> list) {
        promotionCustomerRepository.saveAll(list);
    }

    @Override
    public List<PromotionCustomer> findCustomerAndSendNotification() {
        return PromotionCustomerDao.findCustomerAndSendNotification();
    }

    @Override
    public boolean validateSendNotification(PromotionCustomer promotionCustomer) {
        int count = PromotionCustomerDao.validateSendNotification(promotionCustomer);
        if(count==0)
            return true;
        return false;
    }
}
