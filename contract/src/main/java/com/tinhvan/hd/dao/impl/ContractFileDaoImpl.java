package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.ContractFileDao;
import com.tinhvan.hd.entity.ContractFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ContractFileDaoImpl implements ContractFileDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ContractFile> findByContractUuid(UUID contractUuid, String type) {
        List<ContractFile> ls = new ArrayList<>();
//        DAO.query((em) -> {
//
//
//        });
        Query query = entityManager.createQuery("from ContractFile where contractUuid = :contractUuid and fileType = :type");
        query.setParameter("contractUuid", contractUuid);
        query.setParameter("type", type);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<String> findFilesByContractUuid(UUID contractUuid, String type) {
        List<String> ls = new ArrayList<>();
//        DAO.query((em) -> {
//
//
//        });
        Query query = entityManager.createQuery("select fileName from ContractFile where contractUuid = :contractUuid and fileType = :type");
        query.setParameter("contractUuid", contractUuid);
        query.setParameter("type", type);
        ls.addAll(query.getResultList());
        return ls;
    }
}
