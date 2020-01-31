package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class UuidNotificationRequest implements HDPayload {

    private UUID promotionId;

    public UUID getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
    }

    @Override
    public void validatePayload() {

    }
}
