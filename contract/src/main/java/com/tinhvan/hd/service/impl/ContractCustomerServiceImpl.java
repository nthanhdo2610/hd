package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractCustomerDao;
import com.tinhvan.hd.entity.ContractCustomer;
import com.tinhvan.hd.repository.ContractCustomerRepository;
import com.tinhvan.hd.service.ContractCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContractCustomerServiceImpl implements ContractCustomerService {

    @Autowired
    private ContractCustomerRepository contractCustomerRepository;

    @Autowired
    private ContractCustomerDao contractCustomerDao;


    @Override
    public List<ContractCustomer> getListContractCustomerByContractUuid(UUID contractUuid) {
        return contractCustomerRepository.findAllByContractUuidAndStatus(contractUuid,1);
    }

    @Override
    public void insertContractCustomer(ContractCustomer contractCustomer) {
        contractCustomerRepository.save(contractCustomer);
    }

    @Override
    public List<ContractCustomer> getListContractCustomerByCustomerUuidAndStatus(UUID customerUuid,Integer status) {
        return contractCustomerRepository.findAllByCustomerUuidAndStatus(customerUuid,status);
    }

    @Override
    public List<ContractCustomer> getListContractCustomerByCustomerUuid(UUID customerUuid) {
        return contractCustomerRepository.findAllByCustomerUuid(customerUuid);
    }

    @Override
    public void updateContractCustomer(ContractCustomer contractCustomer) {
        contractCustomerRepository.save(contractCustomer);
    }

    @Override
    public ContractCustomer getListContractCustomerByContractUuidAndStatus(UUID contractUuid, Integer status) {
        List<ContractCustomer> contractCustomers = contractCustomerRepository.findAllByContractUuidAndStatus(contractUuid,status);

        if (contractCustomers != null && contractCustomers.size() > 0) {
            return contractCustomers.get(0);
        }
        return null;
    }

    @Override
    public ContractCustomer getByContractCodeAndCustomerUuid(String contractCode, UUID customerUuid) {
        return contractCustomerRepository.findByContractCodeAndCustomerUuid(contractCode,customerUuid);
    }

    @Override
    public List<String> getCustomerUuidsByContractCode(String contractCode) {
        return contractCustomerDao.getCustomerUuidsByContractCode(contractCode);
    }

    @Override
    public List<ContractCustomer> getListByContractCode(String contractCode) {
        return contractCustomerRepository.findByContractCodeAndStatus(contractCode,1);
    }
}
