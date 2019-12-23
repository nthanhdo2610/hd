package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class AddLoanRequest implements HDPayload {

    private String contractCode;

    private String identifyId;

    private UUID customerUuid;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(contractCode)){
            throw new BadRequestException(1402,"Contract code is null or empty !");
        }

        if(HDUtil.isNullOrEmpty(identifyId)){
            throw new BadRequestException(1403,"identifyId code is null or empty !");
        }

        if (customerUuid == null || HDUtil.isNullOrEmpty(customerUuid.toString())) {
            throw new BadRequestException(1407,"Customer uuid is null or empty");
        }
    }
}
