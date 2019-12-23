package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.base.HDPayload;

public class ContractInfoRequest implements HDPayload {

    private String customerUuid;

    private String contractCode;

    public ContractInfoRequest() {
    }

    public ContractInfoRequest(String customerUuid, String contractCode) {
        this.customerUuid = customerUuid;
        this.contractCode = contractCode;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Override
    public void validatePayload() {

    }

    @Override
    public String toString() {
        return "ContractInfoRequest{" +
                "customerUuid='" + customerUuid + '\'' +
                ", contractCode='" + contractCode + '\'' +
                '}';
    }
}
