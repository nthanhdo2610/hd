package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.Banks;
import com.tinhvan.hd.repository.BanksRepository;
import com.tinhvan.hd.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BanksRepository banksRepository;

    @Override
    public void saveOrUpdate(Banks banks) {
        banksRepository.save(banks);
    }

    @Override
    public void saveAll(List<Banks> banks) {
        banksRepository.saveAll(banks);
    }

    @Override
    public List<Banks> getAllBankByStatus(Integer status) {
        return banksRepository.findAllByStatus(status);
    }
}
