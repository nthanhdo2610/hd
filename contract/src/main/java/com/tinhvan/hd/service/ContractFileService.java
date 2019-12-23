package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractFile;

import java.util.List;
import java.util.UUID;

public interface ContractFileService {

    void create(ContractFile contractFile);

    void update(ContractFile contractFile);

    void delete(ContractFile contractFile);

    ContractFile getById(Integer id);

    List<ContractFile> findByContractUuid(UUID contractUuid, String type);

    List<String> findFilesByContractUuid(UUID contractUuid, String type);
}
