package com.tinhvan.hd.vo;

import java.util.UUID;

public class NotificationFilter {

    private UUID customerUuid;

    private Integer isSent;

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public Integer getIsSent() {
        return isSent;
    }

    public void setIsSent(Integer isSent) {
        this.isSent = isSent;
    }
}
