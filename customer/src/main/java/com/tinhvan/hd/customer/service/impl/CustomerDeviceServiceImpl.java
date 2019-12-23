package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.customer.dao.CustomerDeviceDAO;
import com.tinhvan.hd.customer.model.CustomerDevice;
import com.tinhvan.hd.customer.payload.CustomerDeviceRequest;
import com.tinhvan.hd.customer.rabbitmq.FirebaseRequest;
import com.tinhvan.hd.customer.rabbitmq.RabbitConfig;
import com.tinhvan.hd.customer.repository.CustomerDeviceRepository;
import com.tinhvan.hd.customer.service.CustomerDeviceService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class CustomerDeviceServiceImpl implements CustomerDeviceService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CustomerDeviceServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    private CustomerDeviceDAO customerDeviceDAO;

    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;

    @Override
    public void insert(CustomerDeviceRequest request) {
        CustomerDevice currentDevice = null;
        //disable all records old
        List<CustomerDevice> devices = null;
        if (!HDUtil.isNullOrEmpty(request.getUuid())) {
            devices = customerDeviceDAO.findByUuidOrToken(UUID.fromString(request.getUuid()), request.getFcmToken());
        } else {
            devices = customerDeviceDAO.findByFcmToken(request.getFcmToken());
        }
        if (devices != null && devices.size() > 0) {
            for (CustomerDevice device : devices) {
                device.setStatus(HDConstant.STATUS.DISABLE);
                if (device.getFcmToken().equals(request.getFcmToken())) {
                    if (HDUtil.isNullOrEmpty(request.getUuid()) && device.getCustomerUuid() == null) {
                        currentDevice = device;
                        device.setCreateAt(new Date());
                        device.setStatus(HDConstant.STATUS.ENABLE);
                    }
                    if (!HDUtil.isNullOrEmpty(request.getUuid()) && device.getCustomerUuid() != null
                            && device.getCustomerUuid().toString().equals(request.getUuid())) {
                        currentDevice = device;
                        device.setCreateAt(new Date());
                        device.setStatus(HDConstant.STATUS.ENABLE);
                    }
                }
            }
            customerDeviceRepository.saveAll(devices);
        }

        //insert new fcm token
        if (currentDevice == null) {
            currentDevice = new CustomerDevice();
            if (!HDUtil.isNullOrEmpty(request.getUuid()))
                currentDevice.setCustomerUuid(UUID.fromString(request.getUuid()));
            currentDevice.setFcmToken(request.getFcmToken());
            currentDevice.setPreferLanguage(request.getPreferLanguage());
            currentDevice.setCreateAt(new Date());
            currentDevice.setStatus(HDConstant.STATUS.ENABLE);
            customerDeviceRepository.save(currentDevice);
        }

        //subscribe rabbit mq
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SUBSCRIBE_TOPIC_FIREBASE, new FirebaseRequest("", Arrays.asList(request.getFcmToken())));
    }

    @Override
    public void disableByUuidOrToken(UUID uuid, String fcmToken) {
        List<CustomerDevice> devices = findByUuid(uuid);
        if (devices != null) {
            devices.forEach(device -> {
                device.setStatus(HDConstant.STATUS.DISABLE);
            });
            customerDeviceRepository.saveAll(devices);
        }
 /*
        List<String> tokens = new ArrayList<>();
        List<CustomerDevice> devices = customerDeviceDAO.findByUuidOrToken(uuid, fcmToken);

        devices.forEach(device -> tokens.add(device.getFcmToken()));
        if (tokens != null && tokens.size() > 0) {
            //rabbit mq
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_UNSUBSCRIBE_TOPIC_FIREBASE, new FirebaseRequest("", tokens));
        }

        List<CustomerDevice> deviceUpdate = new ArrayList<>();
        for (CustomerDevice customerDevice : devices) {
            customerDevice.setStatus(HDConstant.STATUS.DISABLE);
            deviceUpdate.add(customerDevice);
        }

        customerDeviceRepository.saveAll(deviceUpdate);
*/
    }

    @Override
    public List<CustomerDevice> findByUuid(UUID uuid) {
        return customerDeviceDAO.findByUuid(uuid);
    }

    @Override
    public void insert(CustomerDevice device) {
        customerDeviceRepository.save(device);
    }
}
