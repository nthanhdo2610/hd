package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.HDPayload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NotificationDTO implements HDPayload {

    private List<UUID> customerUuids;

    private String title;

    private String content;

    private String promotionId;

    private int access;

    private int type;

    private Date endDate;

    @Override
    public void validatePayload() {
        if (customerUuids == null)
            customerUuids = new ArrayList<>();
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

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
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
