package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractEsigned;

import java.util.List;
import java.util.UUID;

public interface ContractEsignedService {
    void create(ContractEsigned contractEsigned);
    void update(ContractEsigned contractEsigned);
    void delete(ContractEsigned contractEsigned);
    ContractEsigned getById(Integer id);
    ContractEsigned findByContractId(UUID contractUuid);

    List<ContractEsigned> getByCustomerUuidAndContractUuid(UUID customerUuid, UUID contractUuid);
}
