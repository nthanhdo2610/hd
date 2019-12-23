package com.tinhvan.hd.promotion.service.impl;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.promotion.dao.PromotionLogDao;
import com.tinhvan.hd.promotion.entity.PromotionLog;
import com.tinhvan.hd.promotion.repository.PromotionLogRepository;
import com.tinhvan.hd.promotion.service.PromotionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionLogServiceImpl implements PromotionLogService {

//    @Autowired
//    private PromotionLogDao PromotionLogDao;

    @Autowired
    private PromotionLogRepository promotionLogRepository;

    @Override
    public void insert(PromotionLog log) {
        promotionLogRepository.save(log);
    }
}
