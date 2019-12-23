package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractDisbursementInfoDao;
import com.tinhvan.hd.entity.ContractDisbursementInfo;
import com.tinhvan.hd.repository.ContractDisbursementInfoRepository;
import com.tinhvan.hd.service.ContractDisbursementInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractDisbursementInfoServiceImpl implements ContractDisbursementInfoService {


    @Autowired
    private ContractDisbursementInfoRepository disbursementInfoRepository;

    @Override
    public void create(ContractDisbursementInfo contractDisbursementInfo) {
        disbursementInfoRepository.save(contractDisbursementInfo);
    }

    @Override
    public void update(ContractDisbursementInfo contractDisbursementInfo) {
        disbursementInfoRepository.save(contractDisbursementInfo);
    }

    @Override
    public void delete(ContractDisbursementInfo contractDisbursementInfo) {
        disbursementInfoRepository.delete(contractDisbursementInfo);
    }

    @Override
    public ContractDisbursementInfo getById(Integer id) {
        return disbursementInfoRepository.findById(Long.valueOf(0)).orElse(null);
    }
}
