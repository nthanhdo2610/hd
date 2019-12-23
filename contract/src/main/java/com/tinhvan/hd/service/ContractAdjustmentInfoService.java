package com.tinhvan.hd.service;

import com.tinhvan.hd.dto.AdjustmentInfoMapper;
import com.tinhvan.hd.dto.ContractAdjustmentDetail;
import com.tinhvan.hd.dto.PageSearch;
import com.tinhvan.hd.entity.ContractAdjustmentInfo;
import java.util.List;

public interface ContractAdjustmentInfoService {

    void create(ContractAdjustmentInfo contractAdjustmentInfo);

    void update(ContractAdjustmentInfo contractAdjustmentInfo);

    void delete(ContractAdjustmentInfo contractAdjustmentInfo);

    ContractAdjustmentInfo getById(Integer id);

    List<AdjustmentInfoMapper> getListContractAdjustmentInfo(String contractCode, Integer isConfirm, PageSearch pageSearch);

    List<ContractAdjustmentInfo> getListContractAdjustmentInfoByContractCode(String contractCode);

    ContractAdjustmentInfo getContractAdjustmentByContractCodeAndKey(String contractCode,String key);

    List<ContractAdjustmentDetail> getListContractAdjustmentInfoByContractCodeMobile(String contractCode);

    List<ContractAdjustmentInfo> getByContractCodes(List<String> contractCodes);

    void updateAll(List<ContractAdjustmentInfo> contractAdjustmentInfos);

}
