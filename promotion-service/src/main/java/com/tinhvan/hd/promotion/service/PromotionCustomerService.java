package com.tinhvan.hd.promotion.service;

import com.tinhvan.hd.promotion.entity.PromotionCustomer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Service
public interface PromotionCustomerService {
    void insert(PromotionCustomer PromotionCustomer);

    void update(PromotionCustomer PromotionCustomer);

    void delete(PromotionCustomer PromotionCustomer);

    PromotionCustomer findById(UUID id);

    List<PromotionCustomer> getListPromotionCustomerByCustomer(UUID customerUuid);

    List<PromotionCustomer> getListPromotionCustomerByPromotionId(UUID promotionId);

    PromotionCustomer find(UUID promotionId, UUID customerUuid);

    void saveAll(List<PromotionCustomer> list);

    List<PromotionCustomer> findCustomerAndSendNotification();

    boolean validateSendNotification(PromotionCustomer promotionCustomer);
}
