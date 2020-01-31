package com.tinhvan.hd.dto;

import java.util.UUID;

public class Uuids {
    private UUID customerUuid;

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public Uuids(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    @Override
    public String toString() {
        return "Uuids{" +
                "customerUuid=" + customerUuid +
                '}';
    }
}
