package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ContractEsignedDao;
import com.tinhvan.hd.entity.ContractEsigned;
import com.tinhvan.hd.repository.ContractEsignedRepository;
import com.tinhvan.hd.service.ContractEsignedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContractEsignedServiceImpl implements ContractEsignedService {


    @Autowired
    private ContractEsignedDao contractEsignedDao;


    @Autowired
    private ContractEsignedRepository contractEsignedRepository;

    @Override
    public void create(ContractEsigned contractEsigned) {
        contractEsignedRepository.save(contractEsigned);
    }

    @Override
    public void update(ContractEsigned contractEsigned) {
        contractEsignedRepository.save(contractEsigned);
    }

    @Override
    public void delete(ContractEsigned contractEsigned) {
        contractEsignedRepository.delete(contractEsigned);
    }

    @Override
    public ContractEsigned getById(Integer id) {
        return contractEsignedRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public ContractEsigned findByContractId(UUID contractUuid) {
        return contractEsignedRepository.findByContractUuidAndIsSignedAdjustment(contractUuid,1);
    }

    @Override
    public List<ContractEsigned> getByCustomerUuidAndContractUuid(UUID customerUuid, UUID contractUuid) {
        return contractEsignedRepository.findAllByCustomerUuidAndContractUuid(customerUuid,contractUuid);
    }
}
