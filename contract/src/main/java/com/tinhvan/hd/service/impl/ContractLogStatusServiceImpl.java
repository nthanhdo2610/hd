package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractLogStatusDao;
import com.tinhvan.hd.entity.ContractLogStatus;
import com.tinhvan.hd.repository.ContractLogStatusRepository;
import com.tinhvan.hd.service.ContractLogStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractLogStatusServiceImpl implements ContractLogStatusService {

//    @Autowired
//    private ContractLogStatusDao contractLogStatusDao;

    @Autowired
    private ContractLogStatusRepository contractLogStatusRepository;

    @Override
    public void create(ContractLogStatus contractLogStatus) {
        contractLogStatusRepository.save(contractLogStatus);
    }

    @Override
    public void update(ContractLogStatus contractLogStatus) {
        contractLogStatusRepository.save(contractLogStatus);
    }

    @Override
    public void delete(ContractLogStatus contractLogStatus) {
        contractLogStatusRepository.delete(contractLogStatus);
    }

    @Override
    public ContractLogStatus getById(Integer id) {
        return contractLogStatusRepository.findById(Long.valueOf(id)).orElse(null);
    }
}
