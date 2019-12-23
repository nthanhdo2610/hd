package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class RemoveContract implements HDPayload {

    private UUID contractUuid;

    private UUID customerUuid;

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    @Override
    public void validatePayload() {
        if (contractUuid == null || HDUtil.isNullOrEmpty(contractUuid.toString())) {
            throw new BadRequestException(1402,"Contract code is null or empty");
        }

        if (customerUuid == null || HDUtil.isNullOrEmpty(customerUuid.toString())) {
            throw new BadRequestException(1407,"Customer uuid is null or empty");
        }
    }
}
