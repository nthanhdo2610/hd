package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.ContractEsignedDao;
import com.tinhvan.hd.entity.ContractEsigned;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Repository
public class ContractEsignedDaoImpl implements ContractEsignedDao {
//    @Override
//    public void create(ContractEsigned contractEsigned) {
//        DAO.query((em) -> {
//            em.persist(contractEsigned);
//        });
//    }
//
//    @Override
//    public void update(ContractEsigned contractEsigned) {
//        DAO.query((em) -> {
//            em.merge(contractEsigned);
//        });
//    }
//
//    @Override
//    public void delete(ContractEsigned contractEsigned) {
//        DAO.query((em) -> {
//            em.remove(contractEsigned);
//        });
//    }
//
//    @Override
//    public ContractEsigned getById(Integer id) {
//        List<ContractEsigned> ls = new ArrayList<>();
//        DAO.query((em) -> {
//            ls.add(em.find(ContractEsigned.class, id));
//
//        });
//        return ls.get(0);
//    }

//    @Override
//    public ContractEsigned findByContractId(UUID contractUuid) {
//        List<ContractEsigned> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//            StringJoiner joiner = new StringJoiner(" ");
//            joiner.add("from ContractEsigned where contractUuid=:contractUuid");
//            Query query = entityManager.createQuery(joiner.toString());
//            query.setParameter("contractUuid", contractUuid);
//            lst.addAll(query.getResultList());
//        });
//        if (lst.size() > 0)
//            return lst.get(0);
//        else return null;
//    }
}
