package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractDisbursementInfo;

public interface ContractDisbursementInfoService {

    void create(ContractDisbursementInfo contractDisbursementInfo);

    void update(ContractDisbursementInfo contractDisbursementInfo);

    void delete(ContractDisbursementInfo contractDisbursementInfo);

    ContractDisbursementInfo getById(Integer id);
}
