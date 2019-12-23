package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractAdjustmentInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContractAdjustmentInfoRepository extends CrudRepository<ContractAdjustmentInfo,Long> {

    ContractAdjustmentInfo getContractAdjustmentInfoByContractCodeAndKey(String contractCode,String key);

    List<ContractAdjustmentInfo> findAllByContractCode(String contractCode);

    List<ContractAdjustmentInfo> findAllByContractCodeIn(List<String> contractCodes);
}
