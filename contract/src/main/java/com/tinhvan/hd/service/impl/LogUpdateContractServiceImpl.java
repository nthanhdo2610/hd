package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.LogUpdateContractWhenLogin;
import com.tinhvan.hd.repository.LogUpdateContractRepository;
import com.tinhvan.hd.service.LogUpdateContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogUpdateContractServiceImpl implements LogUpdateContractService {

    @Autowired
    private LogUpdateContractRepository updateContractRepository;

    @Override
    public void saveOrUpdate(LogUpdateContractWhenLogin updateContractWhenLogin) {
        updateContractRepository.save(updateContractWhenLogin);
    }
}
