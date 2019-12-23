package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.Banks;

import java.util.List;

public interface BankService {

    void saveOrUpdate(Banks banks);

    void saveAll(List<Banks> banks);

    List<Banks> getAllBankByStatus(Integer status);
}
