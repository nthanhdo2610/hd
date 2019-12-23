package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractAdjustmentUploadFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractAdjustmentUploadFileRepository extends CrudRepository<ContractAdjustmentUploadFile,Integer> {
}
