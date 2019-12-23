package com.tinhvan.hd.customer.dao;

import com.tinhvan.hd.customer.model.CustomerDevice;

import java.util.List;
import java.util.UUID;

public interface CustomerDeviceDAO {
//    void insert(CustomerDevice device);
//    void update(CustomerDevice device);
    void disableByUuidOrToken(UUID uuid, String token);
    CustomerDevice findByUuidAndToken(UUID uuid, String token);
    List<CustomerDevice> findByUuid(UUID uuid);
    List<CustomerDevice> findByUuidOrToken(UUID uuid, String fcmToken);
    List<CustomerDevice> findByFcmToken(String fcmToken);
}
