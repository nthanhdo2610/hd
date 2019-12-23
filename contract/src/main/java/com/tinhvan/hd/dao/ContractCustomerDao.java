package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractCustomer;

import java.util.List;
import java.util.UUID;

public interface ContractCustomerDao {

    List<ContractCustomer> findAllByCustomerUuidAndStatus(UUID customerUuid, Integer status);

    List<ContractCustomer> findAllByContractUuidAndStatus(UUID contractUuid,Integer status);
}
