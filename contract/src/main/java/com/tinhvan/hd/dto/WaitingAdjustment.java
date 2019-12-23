package com.tinhvan.hd.dto;

public class WaitingAdjustment {

    private String contractCode;

    private String contractUuid;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public WaitingAdjustment(String contractCode, String contractUuid) {
        this.contractCode = contractCode;
        this.contractUuid = contractUuid;
    }
}
