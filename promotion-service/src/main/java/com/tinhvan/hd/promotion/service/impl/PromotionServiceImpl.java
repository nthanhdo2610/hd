package com.tinhvan.hd.promotion.service.impl;

import com.tinhvan.hd.promotion.dao.PromotionCustomerDao;
import com.tinhvan.hd.promotion.dao.PromotionDao;
import com.tinhvan.hd.promotion.dao.PromotionLogDao;
import com.tinhvan.hd.promotion.entity.Promotion;
import com.tinhvan.hd.promotion.entity.PromotionLog;
import com.tinhvan.hd.promotion.payload.MenuRequest;
import com.tinhvan.hd.promotion.payload.PromotionSearchRequest;
import com.tinhvan.hd.promotion.repository.PromotionLogRepository;
import com.tinhvan.hd.promotion.repository.PromotionRepository;
import com.tinhvan.hd.promotion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionDao PromotionDao;
    @Autowired
    private PromotionLogDao PromotionLogDao;
    @Autowired
    private PromotionCustomerDao promotionCustomerDao;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionLogRepository promotionLogRepository;

    @Override
    public void postPromotion(Promotion Promotion) {
        promotionRepository.save(Promotion);
        PromotionLog log = new PromotionLog(Promotion);
        promotionLogRepository.save(log);
    }

    @Override
    public void updatePromotion(Promotion Promotion) {
        promotionRepository.save(Promotion);
        PromotionLog log = new PromotionLog(Promotion);
        promotionLogRepository.save(log);
    }

    @Override
    public Promotion findById(UUID id) {
        return PromotionDao.findById(id);
    }

    @Override
    public List<Promotion> getListPromotionByStatus(Integer status) {
        return PromotionDao.getListPromotionByStatus(status);
    }

    @Override
    public List<Promotion> getListFeatured(String type) {
        return PromotionDao.getListFeatured(type);
    }

    @Override
    public List<Promotion> findSendNotification() {
        List<Promotion> result = new ArrayList<>();
        List<Promotion> lst = PromotionDao.findSendNotification();
        if(lst!=null){
            for (Promotion promotion : lst) {
                if (promotionCustomerDao.countListPromotionCustomerByPromotionId(promotion.getId()) > 0)
                    continue;
                if (promotion.getAccess() ==Promotion.ACCESS.GENERAL && promotion.getStatusNotification()==Promotion.STATUS_NOTIFICATION.NOT_SEND)
                    continue;
                result.add(promotion);
            }
        }
        return result;
    }

    @Override
    public List<Promotion> find(PromotionSearchRequest searchRequest) {
        return PromotionDao.find(searchRequest);
    }

    @Override
    public int count(PromotionSearchRequest searchRequest) {
        return PromotionDao.count(searchRequest);
    }

    @Override
    public List<Promotion> findIndividual(UUID customerUuid) {
        return PromotionDao.findIndividual(customerUuid);
    }

    @Override
    public List<Promotion> findGeneral() {
        return PromotionDao.findGeneral();
    }

    @Override
    public List<Promotion> findHome(int limit) {
        return PromotionDao.findHome(limit);
    }

    @Override
    public List<Promotion> findHomeLogged(UUID customerUuid, int access, int limit) {
        return PromotionDao.findHomeLogged(customerUuid, access, limit);
    }

    @Override
    public List<Promotion> findMenu(MenuRequest menuRequest) {
        return PromotionDao.findMenu(menuRequest);
    }

    @Override
    public int countMenu(MenuRequest menuRequest) {
        return PromotionDao.countMenu(menuRequest);
    }
}
