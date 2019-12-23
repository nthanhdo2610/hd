package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractEsignedFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractEsignedFileRepository extends CrudRepository<ContractEsignedFile,Long> {
}
