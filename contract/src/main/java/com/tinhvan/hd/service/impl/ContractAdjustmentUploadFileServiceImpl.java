package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractAdjustmentUploadFileDAO;
import com.tinhvan.hd.dto.AdjustmentUploadFileSearch;
import com.tinhvan.hd.entity.ContractAdjustmentUploadFile;
import com.tinhvan.hd.repository.ContractAdjustmentUploadFileRepository;
import com.tinhvan.hd.service.ContractAdjustmentUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractAdjustmentUploadFileServiceImpl implements ContractAdjustmentUploadFileService {

    @Autowired
    private ContractAdjustmentUploadFileDAO contractAdjustmentUploadFileDAO;

    @Autowired
    private ContractAdjustmentUploadFileRepository adjustmentUploadFileRepository;

    @Override
    public void create(ContractAdjustmentUploadFile adjustmentUploadFile) {
        adjustmentUploadFileRepository.save(adjustmentUploadFile);
    }

    @Override
    public void update(ContractAdjustmentUploadFile adjustmentUploadFile) {
        adjustmentUploadFileRepository.save(adjustmentUploadFile);
    }

    @Override
    public void delete(int id) {

        contractAdjustmentUploadFileDAO.delete(id);
    }

    @Override
    public ContractAdjustmentUploadFile findById(int id) {
        return adjustmentUploadFileRepository.findById(id).orElse(null);
    }

    @Override
    public List<ContractAdjustmentUploadFile> find(AdjustmentUploadFileSearch searchRequest) {
        return contractAdjustmentUploadFileDAO.find(searchRequest);
    }

    @Override
    public int count(AdjustmentUploadFileSearch searchRequest) {
        return contractAdjustmentUploadFileDAO.count(searchRequest);
    }

    @Override
    public List<ContractAdjustmentUploadFile> findSendMail() {
        return contractAdjustmentUploadFileDAO.findSendMail();
    }
}
