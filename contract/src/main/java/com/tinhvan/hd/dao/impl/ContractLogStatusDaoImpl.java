package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.ContractLogStatusDao;
import com.tinhvan.hd.entity.ContractLogStatus;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractLogStatusDaoImpl implements ContractLogStatusDao {

    @Override
    public void create(ContractLogStatus contractLogStatus) {
        DAO.query((em) -> {
            em.persist(contractLogStatus);
        });
    }

    @Override
    public void update(ContractLogStatus contractLogStatus) {
        DAO.query((em) -> {
            em.merge(contractLogStatus);
        });
    }

    @Override
    public void delete(ContractLogStatus contractLogStatus) {
        DAO.query((em) -> {
            em.remove(contractLogStatus);
        });
    }

    @Override
    public ContractLogStatus getById(Integer id) {
        List<ContractLogStatus> ls = new ArrayList<>();
        DAO.query((em) -> {
            ls.add(em.find(ContractLogStatus.class,id));

        });
        return ls.get(0);
    }
}
