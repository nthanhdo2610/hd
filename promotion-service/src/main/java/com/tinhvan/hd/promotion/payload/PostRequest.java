package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class PostRequest implements HDPayload {
    private PromotionRequest promotion;
    List<FilterCustomerRequest> filters;

    @Override
    public void validatePayload() {
        if (promotion == null)
            throw new BadRequestException(1117, "invalid promotion");
        promotion.validatePayload();
        if(filters!=null&&filters.size()>0)
            filters.forEach(filterCustomer -> filterCustomer.validatePayload());
    }

    public PromotionRequest getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionRequest promotion) {
        this.promotion = promotion;
    }

    public List<FilterCustomerRequest> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterCustomerRequest> filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "PostRequest{" +
                "promotion=" + promotion +
                ", filters=" + filters +
                '}';
    }
}
