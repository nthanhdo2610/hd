package com.tinhvan.hd.sms.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContractResponse {

    @JsonProperty("contractCode")
    private String contractCode;
    @JsonProperty("status")
    private String status;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("identifyId")
    private String identifyId;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    @Override
    public String toString() {
        return "ContractResponse{" +
                "contractCode='" + contractCode + '\'' +
                ", status='" + status + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", identifyId='" + identifyId + '\'' +
                '}';
    }
}
