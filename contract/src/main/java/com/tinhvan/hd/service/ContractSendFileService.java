package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractSendFile;

import java.util.List;
import java.util.UUID;

public interface ContractSendFileService {
    void create(ContractSendFile contractSendFile);

    void update(ContractSendFile contractSendFile);

    List<ContractSendFile> findByContract(UUID id);

    List<ContractSendFile> findByCustoner(UUID id);

    List<ContractSendFile> findSend();
}
