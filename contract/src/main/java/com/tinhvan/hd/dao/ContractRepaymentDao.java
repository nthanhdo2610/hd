package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractRepayment;

public interface ContractRepaymentDao {

    void create(ContractRepayment contractRepayment);

    void update(ContractRepayment contractRepayment);

    void delete(ContractRepayment contractRepayment);

    ContractRepayment getById(Integer id);
}
