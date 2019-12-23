package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRepository extends CrudRepository<Contract,Long> {

    List<Contract> findAllByIdentifyId(String identifyId);

    Contract getContractByContractUuid(UUID contractUuid);

    Contract getContractByLendingCoreContractId(String lendingCoreContractId);

    List<Contract> findAllByLendingCoreContractIdIn(List<String> contractCodes);

    Page<Contract> findByLendingCoreContractIdContaining(String contractCode,Pageable pageable);

    Page<Contract> findAll(Pageable pageable);

    List<Contract> findAllByStatusIn(List<String> status);

    int countAllByLendingCoreContractIdContaining(String lendingCoreContractId);


    
}
