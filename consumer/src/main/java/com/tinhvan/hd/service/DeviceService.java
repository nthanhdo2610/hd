package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.Device;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public interface DeviceService {

    void saveDevice(Device device);

    public void deleteDeviceByUserId(UUID customerUuid);

    public List<Device> getAllDeviceByStatus(Integer status);

    Device getDeviceByCustomerUuid(UUID customerUuid);

}
