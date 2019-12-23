package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ServicesDao;
import com.tinhvan.hd.model.Services;
import com.tinhvan.hd.repository.ServicesRepository;
import com.tinhvan.hd.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesServiceImpl implements ServicesService {
    @Autowired
    private ServicesDao servicesDao;

    @Autowired
    private ServicesRepository servicesRepository;

    @Override
    public void insertServices(Services object) {
        servicesRepository.save(object);
    }

    @Override
    public void update(Services object) {
        servicesRepository.save(object);
    }

    @Override
    public List<Services> list() {
        return (List<Services>) servicesRepository.findAll();
    }
}
