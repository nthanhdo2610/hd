package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.customer.model.RegisterByPhone;
import com.tinhvan.hd.customer.repository.RegisterByPhoneRepository;
import com.tinhvan.hd.customer.service.RegisterByPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterByPhoneServiceImpl implements RegisterByPhoneService {

    @Autowired
    private RegisterByPhoneRepository registerByPhoneRepository;

    @Override
    public void saveOrUpdate(RegisterByPhone registerByPhone) {
        registerByPhoneRepository.save(registerByPhone);
    }

    @Override
    public void saveOrUpdateAll(List<RegisterByPhone> registerByPhones) {
        registerByPhoneRepository.saveAll(registerByPhones);
    }

    @Override
    public RegisterByPhone getByPhoneAndStatus(String phone,Integer status) {
        return registerByPhoneRepository.findByPhoneAndStatus(phone,1);
    }

    @Override
    public List<RegisterByPhone> getListByPhone(String phone) {
        return registerByPhoneRepository.findAllByPhone(phone);
    }
}
