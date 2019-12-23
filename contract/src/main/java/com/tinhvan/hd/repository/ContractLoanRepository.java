package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractLoan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractLoanRepository extends CrudRepository<ContractLoan,Long> {
}
