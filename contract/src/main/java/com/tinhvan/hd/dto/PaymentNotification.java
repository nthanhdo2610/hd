package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class PaymentNotification implements HDPayload {

    private UUID customerUuid;

    private String contractCode;

    private Integer isNotification;

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getIsNotification() {
        return isNotification;
    }

    public void setIsNotification(Integer isNotification) {
        this.isNotification = isNotification;
    }

    @Override
    public void validatePayload() {

    }
}
