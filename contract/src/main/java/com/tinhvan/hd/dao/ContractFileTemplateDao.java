package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractFileTemplate;

import java.util.List;

public interface ContractFileTemplateDao {
    List<ContractFileTemplate> findByType(String type);
}
