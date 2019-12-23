package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractFilePosition;

import java.util.List;

public interface ContractFilePositionDao {
    List<ContractFilePosition> getPositionByFile(String fileTemplate);
}
