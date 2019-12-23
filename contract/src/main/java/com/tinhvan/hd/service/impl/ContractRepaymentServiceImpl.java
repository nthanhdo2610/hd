package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractRepaymentDao;
import com.tinhvan.hd.entity.ContractRepayment;
import com.tinhvan.hd.repository.ContractRepaymentRepository;
import com.tinhvan.hd.service.ContractRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContractRepaymentServiceImpl implements ContractRepaymentService {

//    @Autowired
//    private ContractRepaymentDao contractRepaymentDao;

    @Autowired
    private ContractRepaymentRepository contractRepaymentRepository;

    @Override
    public void create(ContractRepayment ContractRepayment) {
        contractRepaymentRepository.save(ContractRepayment);
    }

    @Override
    public void update(ContractRepayment ContractRepayment) {
        contractRepaymentRepository.save(ContractRepayment);
    }

    @Override
    public void delete(ContractRepayment ContractRepayment) {
        contractRepaymentRepository.delete(ContractRepayment);
    }

    @Override
    public ContractRepayment getById(Integer id) {
        return contractRepaymentRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public ContractRepayment getByContractCodeAndPaidDate(String contractCode, Date paidDate) {
        return contractRepaymentRepository.findByContractCodeAndAndPaidDate(contractCode,paidDate);
    }

    @Override
    public void saveAll(List<ContractRepayment> contractRepayments) {
        contractRepaymentRepository.saveAll(contractRepayments);
    }
}
