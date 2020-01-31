package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractCustomer;

import java.util.List;
import java.util.UUID;

public interface ContractCustomerService {
    List<ContractCustomer> getListContractCustomerByContractUuid(UUID contractUuid);

    void insertContractCustomer(ContractCustomer contractCustomer);

    List<ContractCustomer> getListContractCustomerByCustomerUuidAndStatus(UUID customerUuid,Integer status);

    List<ContractCustomer> getListContractCustomerByCustomerUuid(UUID customerUuid);

    void updateContractCustomer(ContractCustomer contractCustomer);

    ContractCustomer getListContractCustomerByContractUuidAndStatus(UUID contractUuid,Integer status);

    ContractCustomer getByContractCodeAndCustomerUuid(String contractCode,UUID customerUuid);

    List<String> getCustomerUuidsByContractCode(String contractCode);

    List<ContractCustomer> getListByContractCode(String contractCode);
}
