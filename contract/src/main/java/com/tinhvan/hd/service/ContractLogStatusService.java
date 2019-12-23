package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractLogStatus;

public interface ContractLogStatusService {
    void create(ContractLogStatus contractLogStatus);

    void update(ContractLogStatus contractLogStatus);

    void delete(ContractLogStatus contractLogStatus);

    ContractLogStatus getById(Integer id);
}
