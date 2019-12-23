package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractDisbursementInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractDisbursementInfoRepository extends CrudRepository<ContractDisbursementInfo,Long> {
}
