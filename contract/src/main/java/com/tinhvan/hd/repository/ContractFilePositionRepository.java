package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractFilePosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractFilePositionRepository extends CrudRepository<ContractFilePosition, Long> {
}
