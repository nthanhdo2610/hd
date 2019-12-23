package com.tinhvan.hd.service.impl;
import com.tinhvan.hd.dao.ContractSendFileDAO;
import com.tinhvan.hd.entity.ContractSendFile;
import com.tinhvan.hd.repository.ContractSendFileRepository;
import com.tinhvan.hd.service.ContractSendFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class ContractSendFileImpl implements ContractSendFileService {

    @Autowired
    private ContractSendFileDAO contractSendFileDAO;

    @Autowired
    private ContractSendFileRepository contractSendFileRepository;

    @Override
    public void create(ContractSendFile contractSendFile) {
        contractSendFileRepository.save(contractSendFile);
    }

    @Override
    public void update(ContractSendFile contractSendFile) {
        contractSendFileRepository.save(contractSendFile);
    }

    @Override
    public List<ContractSendFile> findByContract(UUID id) {
        return contractSendFileDAO.findByContract(id);
    }

    @Override
    public List<ContractSendFile> findByCustoner(UUID id) {
        return contractSendFileDAO.findByCustoner(id);
    }

    @Override
    public List<ContractSendFile> findSend() {
        return contractSendFileDAO.findSend();
    }
}
