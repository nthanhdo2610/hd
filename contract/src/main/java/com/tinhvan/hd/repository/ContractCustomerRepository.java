package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContractCustomerRepository extends CrudRepository<ContractCustomer,Long> {

    List<ContractCustomer> findAllByContractUuidAndStatus(UUID contractUuid,Integer status);

    List<ContractCustomer> findAllByCustomerUuidAndStatus(UUID customerUuid,Integer status);

    List<ContractCustomer> findAllByCustomerUuid(UUID customerUuid);

    ContractCustomer findByContractCodeAndCustomerUuid(String contractCode,UUID customerUuid);

    List<ContractCustomer> findByContractCodeAndStatus(String contractCode,Integer status);
}
