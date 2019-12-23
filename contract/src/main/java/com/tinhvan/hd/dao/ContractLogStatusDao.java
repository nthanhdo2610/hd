package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractLogStatus;

public interface ContractLogStatusDao {
    void create(ContractLogStatus contractLogStatus);

    void update(ContractLogStatus contractLogStatus);

    void delete(ContractLogStatus contractLogStatus);

    ContractLogStatus getById(Integer id);
}
