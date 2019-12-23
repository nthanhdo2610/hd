package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class ContractFilter implements HDPayload {

    private UUID customerUuid;

    private Integer status;

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public void validatePayload() {

    }
}
