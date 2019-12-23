package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.DeviceDao;
import com.tinhvan.hd.entity.Device;
import com.tinhvan.hd.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    DeviceDao deviceDao;

    @Override
    public void saveDevice(Device device) {
        deviceDao.saveDevice(device);
    }

    @Override
    public void deleteDeviceByUserId(UUID customerUuid) {
        deviceDao.deleteDeviceByUserId(customerUuid);
    }

    @Override
    public List<Device> getAllDeviceByStatus(Integer status) {
        return deviceDao.getAllDeviceByStatus(status);
    }

    @Override
    public Device getDeviceByCustomerUuid(UUID customerUuid) {
        return deviceDao.getDeviceByCustomerUuid(customerUuid);
    }


}
