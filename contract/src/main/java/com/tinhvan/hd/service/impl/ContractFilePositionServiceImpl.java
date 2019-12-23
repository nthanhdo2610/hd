package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractFilePositionDao;
import com.tinhvan.hd.entity.ContractFilePosition;
import com.tinhvan.hd.repository.ContractFilePositionRepository;
import com.tinhvan.hd.service.ContractFilePositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractFilePositionServiceImpl implements ContractFilePositionService {
    @Autowired
    private ContractFilePositionDao contractFilePositionDao;
    @Autowired
    private ContractFilePositionRepository contractFilePositionRepository;

    @Override
    public List<ContractFilePosition> getPositionByFile(String fileTemplate) {
        return contractFilePositionDao.getPositionByFile(fileTemplate);
    }

    @Override
    public void saveOrUpdate(ContractFilePosition contractFilePosition) {
        contractFilePositionRepository.save(contractFilePosition);
    }

    @Override
    public void delete(ContractFilePosition contractFilePosition) {
        contractFilePositionRepository.delete(contractFilePosition);
    }
}
