package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.customer.model.CustomerDevice;
import com.tinhvan.hd.customer.payload.CustomerDeviceRequest;

import java.util.List;
import java.util.UUID;

public interface CustomerDeviceService {
    void unsubscribe();
    void insert(CustomerDeviceRequest deviceRequest);
    void disableByUuidOrToken(UUID uuid, String fcmToken);
    List<CustomerDevice> findByUuid(UUID uuid);
    void insert(CustomerDevice device);
}
