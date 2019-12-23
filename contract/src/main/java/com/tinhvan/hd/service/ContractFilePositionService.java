package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractFilePosition;

import java.util.List;


public interface ContractFilePositionService {
    List<ContractFilePosition> getPositionByFile(String fileTemplate);
    void saveOrUpdate(ContractFilePosition contractFilePosition);
    void delete(ContractFilePosition contractFilePosition);
}
