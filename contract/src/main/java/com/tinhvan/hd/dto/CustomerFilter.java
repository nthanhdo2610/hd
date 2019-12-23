package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class CustomerFilter implements HDPayload {

    private String contractCode;

    private String identityNumber;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    @Override
    public void validatePayload() {

    }
}
