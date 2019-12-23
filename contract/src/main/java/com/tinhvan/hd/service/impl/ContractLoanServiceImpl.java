package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractLoanDao;
import com.tinhvan.hd.entity.ContractLoan;
import com.tinhvan.hd.repository.ContractLoanRepository;
import com.tinhvan.hd.service.ContractLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractLoanServiceImpl implements ContractLoanService {

//    @Autowired
//    private ContractLoanDao contractLoanDao;

    @Autowired
    private ContractLoanRepository contractLoanRepository;

    @Override
    public void create(ContractLoan contractLoan) {
        contractLoanRepository.save(contractLoan);
    }

    @Override
    public void update(ContractLoan contractLoan) {
        contractLoanRepository.save(contractLoan);
    }

    @Override
    public void delete(ContractLoan contractLoan) {
        contractLoanRepository.delete(contractLoan);
    }

    @Override
    public ContractLoan getById(Integer id) {
        return contractLoanRepository.findById(Long.valueOf(id)).orElse(null);
    }
}
