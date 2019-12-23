package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractEsignedFile;

import java.util.List;
import java.util.UUID;

public interface ContractEsignedFileService {
    void create(ContractEsignedFile contractEsignedFile);

    void update(ContractEsignedFile contractEsignedFile);

    void delete(ContractEsignedFile contractEsignedFile);

    ContractEsignedFile getById(Integer id);
    List<String> getFile(UUID contractUuid, String fileType);
}
