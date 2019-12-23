package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class SearchDisbursementInfo implements HDPayload {

    private Integer isSent;

    private String customerUuid;

    public Integer getIsSent() {
        return isSent;
    }

    public void setIsSent(Integer isSent) {
        this.isSent = isSent;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    @Override
    public void validatePayload() {

    }
}
