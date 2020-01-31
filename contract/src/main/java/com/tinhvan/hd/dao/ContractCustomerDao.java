package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.ContractCustomer;

import java.util.List;
import java.util.UUID;

public interface ContractCustomerDao {

    List<String> getCustomerUuidsByContractCode(String contractCode);
}
