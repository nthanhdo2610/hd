package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.ContractLoanDao;
import com.tinhvan.hd.entity.ContractLoan;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractLoanDaoImpl implements ContractLoanDao {

    @Override
    public void create(ContractLoan contractLoan) {
        DAO.query((em) -> {
            em.persist(contractLoan);
        });
    }

    @Override
    public void update(ContractLoan contractLoan) {
        DAO.query((em) -> {
            em.merge(contractLoan);
        });
    }

    @Override
    public void delete(ContractLoan contractLoan) {
        DAO.query((em) -> {
            em.remove(contractLoan);
        });
    }

    @Override
    public ContractLoan getById(Integer id) {
        List<ContractLoan> ls = new ArrayList<>();
        DAO.query((em) -> {
            ls.add(em.find(ContractLoan.class,id));

        });
        return ls.get(0);
    }
}
