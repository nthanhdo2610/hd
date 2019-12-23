package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractLoan;

public interface ContractLoanDao {
    void create(ContractLoan contractLoan);

    void update(ContractLoan contractLoan);

    void delete(ContractLoan contractLoan);

    ContractLoan getById(Integer id);
}
