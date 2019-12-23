package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractRepayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ContractRepaymentRepository extends CrudRepository<ContractRepayment,Long> {

    ContractRepayment findByContractCodeAndAndPaidDate(String contractCode, Date paidDate);
}
