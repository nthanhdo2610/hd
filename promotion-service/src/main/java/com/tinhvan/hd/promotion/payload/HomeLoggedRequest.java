package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.promotion.entity.Promotion;

import java.util.UUID;

public class HomeLoggedRequest implements HDPayload {
    private String customerUuid;
    private int limit;
    private int access;

    @Override
    public void validatePayload() {
        if (access != Promotion.ACCESS.GENERAL && access != Promotion.ACCESS.INDIVIDUAL)
            access = 0;
        if (limit < 0)
            limit = 0;
        if (!HDUtil.isNullOrEmpty(customerUuid)) {
            try {
                UUID.fromString(customerUuid);
            } catch (Exception e) {
                throw new BadRequestException(1106, "invalid id");
            }
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
                ", limit=" + limit +
                ", access=" + access +
                '}';
    }
}
