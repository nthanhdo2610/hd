package com.tinhvan.hd.dao;

import com.tinhvan.hd.dto.AdjustmentInfoMapper;
import com.tinhvan.hd.dto.ContractAdjustmentDetail;
import com.tinhvan.hd.dto.PageSearch;
import com.tinhvan.hd.entity.ContractAdjustmentInfo;

import java.util.List;

public interface ContractAdjustmentInfoDao {


    List<AdjustmentInfoMapper> getListContractAdjustmentInfo(String contractCode, Integer isConfirm, PageSearch pageSearch);

//    List<ContractAdjustmentInfo> getListContractAdjustmentInfoByContractCode(String contractCode);

//    ContractAdjustmentInfo getContractAdjustmentByContractCodeAndKey(String contractCode,String key);

    List<ContractAdjustmentDetail> getListContractAdjustmentInfoByContractCodeMobile(String contractCode);
}
