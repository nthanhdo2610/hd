package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractLoan;

public interface ContractLoanService {
    void create(ContractLoan contractLoan);

    void update(ContractLoan contractLoan);

    void delete(ContractLoan contractLoan);

    ContractLoan getById(Integer id);
}
