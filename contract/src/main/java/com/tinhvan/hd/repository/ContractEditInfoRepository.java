package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractEditInfo;
import io.swagger.models.auth.In;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractEditInfoRepository extends CrudRepository<ContractEditInfo,Long> {

    ContractEditInfo findByContractCode(String contractCode);

    List<ContractEditInfo> findAllByIsUpdateMonthlyDueDate(Integer isUpdateMonthlyDueDate);

    List<ContractEditInfo> findAllByIsUpdateChassinoEnginerno(Integer isUpdateChassinoEnginerno);

    List<ContractEditInfo> findAllByIsUpdateAdjustment(Integer isUpdateAdjustment);

    List<ContractEditInfo> findAllByIsUpdateConprintToDocveri(Integer isUpdateConprintToDocveri);

}
