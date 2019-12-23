package com.tinhvan.hd.dto;

import java.util.UUID;

public class ListContractByCustomer {

    private String contractCode;

    private UUID contractUuid;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }
}
