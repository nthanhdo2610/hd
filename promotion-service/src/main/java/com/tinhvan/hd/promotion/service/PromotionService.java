package com.tinhvan.hd.promotion.service;

import com.tinhvan.hd.promotion.entity.Promotion;
import com.tinhvan.hd.promotion.payload.MenuRequest;
import com.tinhvan.hd.promotion.payload.PromotionSearchRequest;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
public interface PromotionService {

    void postPromotion(Promotion Promotion);
    void updatePromotion(Promotion Promotion);
    Promotion findById(UUID id);
    List<Promotion> getListPromotionByStatus(Integer status);
    List<Promotion> getListFeatured(String type);
    List<Promotion> findSendNotification();
    List<Promotion> find(PromotionSearchRequest searchRequest);
    int count(PromotionSearchRequest searchRequest);
    List<Promotion> findIndividual(UUID customerUuid);
    List<Promotion> findGeneral();
    List<Promotion> findHome(int limit);
    List<Promotion> findHomeLogged(UUID customerUuid, int access, int limit);
    List<Promotion> findMenu(MenuRequest menuRequest);
    int countMenu(MenuRequest menuRequest);
}