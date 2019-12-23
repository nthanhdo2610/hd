package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractEsignedFileDao;
import com.tinhvan.hd.entity.ContractEsignedFile;
import com.tinhvan.hd.repository.ContractEsignedFileRepository;
import com.tinhvan.hd.service.ContractEsignedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContractEsignedFileServiceImpl implements ContractEsignedFileService {

    @Autowired
    private ContractEsignedFileRepository contractEsignedFileRepository;

    @Autowired
    private ContractEsignedFileDao esignedFileDao;

    @Override
    public void create(ContractEsignedFile contractEsignedFile) {
        contractEsignedFileRepository.save(contractEsignedFile);
    }

    @Override
    public void update(ContractEsignedFile contractEsignedFile) {
        contractEsignedFileRepository.save(contractEsignedFile);
    }

    @Override
    public void delete(ContractEsignedFile contractEsignedFile) {
        contractEsignedFileRepository.delete(contractEsignedFile);
    }

    @Override
    public ContractEsignedFile getById(Integer id) {
        return contractEsignedFileRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public List<String> getFile(UUID id, String fileType) {
        return esignedFileDao.getFile(id, fileType);
    }
}
