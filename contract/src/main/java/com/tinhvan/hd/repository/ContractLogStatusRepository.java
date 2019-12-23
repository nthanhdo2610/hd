package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractLogStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractLogStatusRepository extends CrudRepository<ContractLogStatus,Long> {
}
