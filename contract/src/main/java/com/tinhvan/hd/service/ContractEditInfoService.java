package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractEditInfo;

import java.util.List;

public interface ContractEditInfoService {

    void saveOrUpdate(ContractEditInfo contractEditInfo);

    ContractEditInfo getContractEditInfoByContractCode(String contractCode);

    List<ContractEditInfo> getByIsUpdateMonthlyDueDate(Integer value);

    List<ContractEditInfo> getByIsUpdateChassinoEnginerno(Integer value);

    List<ContractEditInfo> getByIsUpdateConprintToDocveri(Integer value);

    void saveOrUpdateAll(List<ContractEditInfo> contractEditInfos);
}
