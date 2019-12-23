package com.tinhvan.hd.dto;

import java.util.UUID;

public class ContractsByCustomerUuid {

    private String contractUuid;

    private String contractCode;

    private String status;

    private Short contractCustomerStatus;

    public ContractsByCustomerUuid(String contractUuid, String contractCode, String status, Short contractCustomerStatus) {
        this.contractUuid = contractUuid;
        this.contractCode = contractCode;
        this.status = status;
        this.contractCustomerStatus = contractCustomerStatus;
    }

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

    public Short getContractCustomerStatus() {
        return contractCustomerStatus;
    }

    public void setContractCustomerStatus(Short contractCustomerStatus) {
        this.contractCustomerStatus = contractCustomerStatus;
    }
}
