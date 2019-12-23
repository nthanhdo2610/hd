package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.HDPayload;

public class ContractSearchRequest implements HDPayload {
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

    public ContractSearchRequest(String contractCode, String identityNumber) {
        this.contractCode = contractCode;
        this.identityNumber = identityNumber;
    }

    @Override
    public void validatePayload() {
        if (contractCode == null)
            contractCode = "";
        if (identityNumber == null)
            identityNumber = "";
    }

    @Override
    public String toString() {
        return "ContractSearchRequest{" +
                "contractCode='" + contractCode + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                '}';
    }
}
