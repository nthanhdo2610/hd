package com.tinhvan.hd.dto;

public class ContractByCustomer {

    private String contractUuid;

    private String contractCode;

    private String status;

    private String type;

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContractByCustomer(String contractUuid, String contractCode, String status, String type) {
        this.contractUuid = contractUuid;
        this.contractCode = contractCode;
        this.status = status;
        this.type = type;
    }
}
