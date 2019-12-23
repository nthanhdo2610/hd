package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class IndividualPromotionRequest implements HDPayload {

    private String customerUuid;

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(customerUuid))
            throw new BadRequestException(1106, "invalid id");
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
}
