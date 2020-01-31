package com.tinhvan.hd.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NotificationQueueDTO implements HDPayload {


    private List<UUID> customerUuids;

    private String title;

    private String content;

    private String newsId;

    private String promotionId;

    private int type;

    private int access;

    private String langCode;

    private Date endDate;

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(langCode))
            langCode = "vi";
        if (customerUuids == null)
            customerUuids = new ArrayList<>();
        try {
            if (!HDUtil.isNullOrEmpty(newsId))
                UUID.fromString(newsId);
            if (!HDUtil.isNullOrEmpty(promotionId))
                UUID.fromString(promotionId);
        } catch (Exception e) {
            throw new BadRequestException(1106);
        }
        if (HDUtil.isNullOrEmpty(title))
            title = "";
        if (!HDUtil.isNullOrEmpty(title) && title.length() > 128)
            throw new BadRequestException(1301);

        if (HDUtil.isNullOrEmpty(content))
            content = "";
    }

    public List<UUID> getCustomerUuids() {
        return customerUuids;
    }

    public void setCustomerUuids(List<UUID> customerUuids) {
        this.customerUuids = customerUuids;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
