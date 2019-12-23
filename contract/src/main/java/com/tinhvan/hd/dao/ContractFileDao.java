package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractFile;

import java.util.List;
import java.util.UUID;

public interface ContractFileDao {


    List<ContractFile> findByContractUuid(UUID contractUuid, String type);

    List<String> findFilesByContractUuid(UUID contractUuid, String type);
}
