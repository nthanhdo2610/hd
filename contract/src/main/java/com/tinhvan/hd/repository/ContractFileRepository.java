package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractFileRepository extends CrudRepository<ContractFile,Long> {
}
