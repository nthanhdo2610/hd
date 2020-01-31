package com.tinhvan.hd.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class UuidNotificationRequest implements HDPayload {

    private UUID newsId;
    private UUID promotionId;

    public UUID getNewsId() {
        return newsId;
    }

    public void setNewsId(UUID newsId) {
        this.newsId = newsId;
    }

    public UUID getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
    }

    @Override
    public void validatePayload() {
        if (newsId == null && promotionId == null)
            throw new BadRequestException();
    }
}
