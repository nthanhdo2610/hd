package com.tinhvan.hd.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.UUID;

public class ReadDetailNotificationRequest implements HDPayload {

    private UUID customerUuid;
    private UUID notificationUuid;

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public UUID getNotificationUuid() {
        return notificationUuid;
    }

    public void setNotificationUuid(UUID notificationUuid) {
        this.notificationUuid = notificationUuid;
    }

    public ReadDetailNotificationRequest(UUID customerUuid, UUID notificationUuid) {
        this.customerUuid = customerUuid;
        this.notificationUuid = notificationUuid;
    }

    @Override
    public void validatePayload() {
        if (customerUuid == null || notificationUuid == null)
            throw new BadRequestException();
    }

    @Override
    public String toString() {
        return "ReadDetailNotificationRequest{" +
                "customerUuid=" + customerUuid +
                ", notificationUuid=" + notificationUuid +
                '}';
    }
}
