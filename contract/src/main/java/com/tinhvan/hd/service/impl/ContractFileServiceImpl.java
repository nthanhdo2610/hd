package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractFileDao;
import com.tinhvan.hd.entity.ContractFile;
import com.tinhvan.hd.repository.ContractFileRepository;
import com.tinhvan.hd.service.ContractFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContractFileServiceImpl implements ContractFileService {

    @Autowired
    private ContractFileDao contractFileDao;

    @Autowired
    private ContractFileRepository contractFileRepository;

    @Override
    public void create(ContractFile contractFile) {
        contractFileRepository.save(contractFile);
    }

    @Override
    public void update(ContractFile contractFile) {
        contractFileRepository.save(contractFile);
    }

    @Override
    public void delete(ContractFile contractFile) {
        contractFileRepository.delete(contractFile);
    }

    @Override
    public ContractFile getById(Integer id) {
        return contractFileRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public List<ContractFile> findByContractUuid(UUID contractUuid, String type) {
        return contractFileDao.findByContractUuid(contractUuid, type);
    }

    @Override
    public List<String> findFilesByContractUuid(UUID contractUuid, String type) {
        return contractFileDao.findFilesByContractUuid(contractUuid, type);
    }
}
