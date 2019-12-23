package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.ContractRepaymentDao;
import com.tinhvan.hd.entity.ContractRepayment;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractRepaymentDaoImpl implements ContractRepaymentDao {


    @Override
    public void create(ContractRepayment contractRepayment) {
        DAO.query((em) -> {
            em.persist(contractRepayment);
        });
    }

    @Override
    public void update(ContractRepayment contractRepayment) {
        DAO.query((em) -> {
            em.merge(contractRepayment);
        });
    }

    @Override
    public void delete(ContractRepayment contractRepayment) {
        DAO.query((em) -> {
            em.remove(contractRepayment);
        });
    }

    @Override
    public ContractRepayment getById(Integer id) {
        List<ContractRepayment> ls = new ArrayList<>();
        DAO.query((em) -> {
            ls.add(em.find(ContractRepayment.class,id));

        });
        return ls.get(0);
    }
}
