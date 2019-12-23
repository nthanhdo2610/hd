package com.tinhvan.hd.dao;


import com.tinhvan.hd.entity.Device;

import java.util.List;
import java.util.UUID;

public interface DeviceDao{

    void saveDevice(Device device);

    public void deleteDeviceByUserId(UUID customerUuid);

    public List<Device> getAllDeviceByStatus(Integer status);

    Device getDeviceByCustomerUuid(UUID customerUuid);
}
