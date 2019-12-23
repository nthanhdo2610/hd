package com.tinhvan.hd.news.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.news.entity.News;

import java.util.UUID;

public class HomeLoggedRequest implements HDPayload {
    private String customerUuid;
    int type;
    private int limit;
    int access;

    @Override
    public void validatePayload() {
        if (access != News.ACCESS.GENERAL && access != News.ACCESS.INDIVIDUAL)
            access = 0;
        if (limit < 0)
            limit = 0;
        if (type < 0)
            type = 0;
        if (!HDUtil.isNullOrEmpty(customerUuid))
            try {
                UUID.fromString(customerUuid);
            } catch (Exception e) {
                throw new BadRequestException(1106, "invalid id");
            }
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return "HomeLoggedRequest{" +
                "customerUuid='" + customerUuid + '\'' +
                ", type=" + type +
                ", limit=" + limit +
                ", access=" + access +
                '}';
    }
}
