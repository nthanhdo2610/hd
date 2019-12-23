package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.ContractEditInfo;
import com.tinhvan.hd.repository.ContractEditInfoRepository;
import com.tinhvan.hd.service.ContractEditInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractEditInfoServiceImpl implements ContractEditInfoService {

    @Autowired
    private ContractEditInfoRepository contractEditInfoRepository;

    @Override
    public void saveOrUpdate(ContractEditInfo contractEditInfo) {
        contractEditInfoRepository.save(contractEditInfo);
    }

    @Override
    public ContractEditInfo getContractEditInfoByContractCode(String contractCode) {
        return contractEditInfoRepository.findByContractCode(contractCode);
    }

    @Override
    public List<ContractEditInfo> getByIsUpdateMonthlyDueDate(Integer value) {
        return contractEditInfoRepository.findAllByIsUpdateMonthlyDueDate(value);
    }

    @Override
    public List<ContractEditInfo> getByIsUpdateChassinoEnginerno(Integer value) {
        return contractEditInfoRepository.findAllByIsUpdateChassinoEnginerno(value);
    }

    @Override
    public List<ContractEditInfo> getByIsUpdateConprintToDocveri(Integer value) {
        return contractEditInfoRepository.findAllByIsUpdateConprintToDocveri(value);
    }

    @Override
    public void saveOrUpdateAll(List<ContractEditInfo> contractEditInfos) {
        contractEditInfoRepository.saveAll(contractEditInfos);
    }
}
