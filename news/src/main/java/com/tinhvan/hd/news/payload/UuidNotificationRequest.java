package com.tinhvan.hd.news.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class UuidNotificationRequest implements HDPayload {

    private UUID newsId;

    public UUID getNewsId() {
        return newsId;
    }

    public void setNewsId(UUID newsId) {
        this.newsId = newsId;
    }

    @Override
    public void validatePayload() {

    }
}
