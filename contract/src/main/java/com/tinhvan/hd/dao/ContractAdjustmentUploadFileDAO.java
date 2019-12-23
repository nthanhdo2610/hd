package com.tinhvan.hd.dao;

import com.tinhvan.hd.dto.AdjustmentUploadFileSearch;
import com.tinhvan.hd.entity.ContractAdjustmentUploadFile;

import java.util.List;

public interface ContractAdjustmentUploadFileDAO {
//    void create(ContractAdjustmentUploadFile adjustmentUploadFile);
//    void update(ContractAdjustmentUploadFile adjustmentUploadFile);
    void delete(int id);
    ContractAdjustmentUploadFile findById(int id);
    List<ContractAdjustmentUploadFile> find(AdjustmentUploadFileSearch searchRequest);
    int count(AdjustmentUploadFileSearch searchRequest);
    List<ContractAdjustmentUploadFile> findSendMail();
}
