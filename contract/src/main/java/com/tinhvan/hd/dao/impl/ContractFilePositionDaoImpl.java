package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.ContractFilePositionDao;
import com.tinhvan.hd.entity.ContractFilePosition;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractFilePositionDaoImpl implements ContractFilePositionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ContractFilePosition> getPositionByFile(String fileTemplate) {
        List<ContractFilePosition> ls = new ArrayList<>();
        Query query = entityManager.createQuery("from ContractFilePosition where file = :fileTemplate");
        query.setParameter("fileTemplate", fileTemplate);
        ls.addAll(query.getResultList());
        return ls;
    }
}
