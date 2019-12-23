package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.ContractSendFileDAO;
import com.tinhvan.hd.entity.ContractSendFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ContractSendFileDAOImpl implements ContractSendFileDAO {
    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public void create(ContractSendFile contractSendFile) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(contractSendFile);
//        });
//    }
//
//    @Override
//    public void update(ContractSendFile contractSendFile) {
//        entityManager.merge(contractSendFile);
//    }

    @Override
    public List<ContractSendFile> findByContract(UUID id) {
        List<ContractSendFile> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from ContractSendFile where contractUuid = :contractUuid");
        query.setParameter("contractUuid", id);
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public List<ContractSendFile> findByCustoner(UUID id) {
        List<ContractSendFile> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from ContractSendFile where customerUuid = :customerUuid");
        query.setParameter("customerUuid", id);
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public List<ContractSendFile> findSend() {
        List<ContractSendFile> lst = new ArrayList<>();
        Query query = entityManager.createQuery("from ContractSendFile where status = :status");
        query.setParameter("status", 0);
        lst.addAll(query.getResultList());
        return lst;
    }
}
