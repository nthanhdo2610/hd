package com.tinhvan.hd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ContractDetailMobileResponse {
    @JsonProperty("detailMobile")
    private DetailMobile detailMobile;
    @JsonProperty("attachments")
    private List<String> attachments;

    public ContractDetailMobileResponse(DetailMobile detailMobile, List<String> attachments) {
        this.detailMobile = detailMobile;
        this.attachments = attachments;
    }

    public DetailMobile getDetailMobile() {
        return detailMobile;
    }

    public void setDetailMobile(DetailMobile detailMobile) {
        this.detailMobile = detailMobile;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }
}
