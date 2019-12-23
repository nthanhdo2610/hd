package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractEsignedFile;

import java.util.List;
import java.util.UUID;

public interface ContractEsignedFileDao {
//    void create(ContractEsignedFile contractEsignedFile);
//
//    void update(ContractEsignedFile contractEsignedFile);
//
//    void delete(ContractEsignedFile contractEsignedFile);
//
//    ContractEsignedFile getById(Integer id);

    List<String> getFile(UUID contractUuid, String fileType);

}
