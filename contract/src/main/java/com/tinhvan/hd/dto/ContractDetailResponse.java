package com.tinhvan.hd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ContractDetailResponse {
    @JsonProperty("contractInfo")
    private ContractInfo contractInfo;
    @JsonProperty("attachments")
    private List<String> attachments;

    public ContractDetailResponse(ContractInfo contractInfo, List<String> attachments) {
        this.contractInfo = contractInfo;
        this.attachments = attachments;
    }

    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }
}
