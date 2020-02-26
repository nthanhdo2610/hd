package com.tinhvan.hd.promotion.dao;

import com.tinhvan.hd.promotion.entity.Promotion;
import com.tinhvan.hd.promotion.entity.PromotionCustomer;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
public interface PromotionCustomerDao {

//    void insert(PromotionCustomer PromotionCustomer);
//
//    void update(PromotionCustomer PromotionCustomer);
//
//    void delete(PromotionCustomer PromotionCustomer);

    PromotionCustomer findById(UUID id);

    List<PromotionCustomer> getListPromotionCustomerByCustomer(UUID customerUuid);

    List<PromotionCustomer> getListPromotionCustomerByPromotionId(UUID promotionId);

    int countListPromotionCustomerByPromotionId(UUID promotionId);

    PromotionCustomer find(UUID promotionId, UUID customerUuid);

    List<PromotionCustomer> findCustomerAndSendNotification();

    int validateSendNotification(PromotionCustomer promotionCustomer);

    void updateByPromotion(Promotion promotion);

    void deleteByPromotionId(UUID promotionId);

}
