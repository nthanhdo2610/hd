package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.model.ConfigContractTypeBackground;
import com.tinhvan.hd.repository.ConfigContractTypeBackgroundRepository;
import com.tinhvan.hd.service.ConfigContractTypeBackgroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.tinhvan.hd.dao.ConfigContractTypeBackgroundDao;
//import com.tinhvan.hd.model.ConfigStaff;
//import com.tinhvan.hd.service.ConfigStaffService;

import java.util.List;
@Service
public class ConfigContractTypeBackgroundServiceImpl implements ConfigContractTypeBackgroundService {
//    @Autowired
//    ConfigContractTypeBackgroundDao configContractTypeBackgroundDao;

    @Autowired
    private ConfigContractTypeBackgroundRepository contractTypeBackgroundRepository;


    @Override
    public ConfigContractTypeBackground insertOrUpdate(ConfigContractTypeBackground object) {
        return contractTypeBackgroundRepository.save(object);
    }

    @Override
    public List<ConfigContractTypeBackground> list() {
        return contractTypeBackgroundRepository.findAll();
    }


    @Override
    public ConfigContractTypeBackground findById(int id) {
        return contractTypeBackgroundRepository.findById(id).orElse(null);
    }

    @Override
    public ConfigContractTypeBackground findByContractType(String contractType) {
        return contractTypeBackgroundRepository.findByContractType(contractType).orElse(null);
    }
}
