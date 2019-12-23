package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractAdjustmentInfoDao;
import com.tinhvan.hd.dto.AdjustmentInfoMapper;
import com.tinhvan.hd.dto.ContractAdjustmentDetail;
import com.tinhvan.hd.dto.PageSearch;
import com.tinhvan.hd.entity.ContractAdjustmentInfo;
import com.tinhvan.hd.repository.ContractAdjustmentInfoRepository;
import com.tinhvan.hd.service.ContractAdjustmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractAdjustmentInfoServiceImpl implements ContractAdjustmentInfoService {


    @Autowired
    private ContractAdjustmentInfoDao adjustmentInfoDao;

    @Autowired
    private ContractAdjustmentInfoRepository adjustmentInfoRepository;

    @Override
    public void create(ContractAdjustmentInfo contractAdjustmentInfo) {
        adjustmentInfoRepository.save(contractAdjustmentInfo);
    }

    @Override
    public void update(ContractAdjustmentInfo contractAdjustmentInfo) {
        adjustmentInfoRepository.save(contractAdjustmentInfo);
    }

    @Override
    public void delete(ContractAdjustmentInfo contractAdjustmentInfo) {
        adjustmentInfoRepository.delete(contractAdjustmentInfo);
    }

    @Override
    public ContractAdjustmentInfo getById(Integer id) {
        return adjustmentInfoRepository.findById(Long.valueOf(id)).orElse(new ContractAdjustmentInfo());
    }

    @Override
    public List<AdjustmentInfoMapper> getListContractAdjustmentInfo(String contractCode, Integer isConfirm, PageSearch pageSearch) {
        return adjustmentInfoDao.getListContractAdjustmentInfo(contractCode,isConfirm,pageSearch);
    }

    @Override
    public List<ContractAdjustmentInfo> getListContractAdjustmentInfoByContractCode(String contractCode) {
        return adjustmentInfoRepository.findAllByContractCode(contractCode);
    }

    @Override
    public ContractAdjustmentInfo getContractAdjustmentByContractCodeAndKey(String contractCode, String key) {
        return adjustmentInfoRepository.getContractAdjustmentInfoByContractCodeAndKey(contractCode,key);
    }

    @Override
    public List<ContractAdjustmentDetail> getListContractAdjustmentInfoByContractCodeMobile(String contractCode) {
        return adjustmentInfoDao.getListContractAdjustmentInfoByContractCodeMobile(contractCode);
    }

    @Override
    public List<ContractAdjustmentInfo> getByContractCodes(List<String> contractCodes) {
        return adjustmentInfoRepository.findAllByContractCodeIn(contractCodes);
    }

    @Override
    public void updateAll(List<ContractAdjustmentInfo> contractAdjustmentInfos) {
        adjustmentInfoRepository.saveAll(contractAdjustmentInfos);
    }
}
