package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractRepayment;

import java.util.Date;
import java.util.List;

public interface ContractRepaymentService {
    void create(ContractRepayment ContractRepayment);

    void update(ContractRepayment ContractRepayment);

    void delete(ContractRepayment ContractRepayment);

    ContractRepayment getById(Integer id);

    ContractRepayment getByContractCodeAndPaidDate(String contractCode, Date paidDate);

    void saveAll(List<ContractRepayment> contractRepayments);
}
