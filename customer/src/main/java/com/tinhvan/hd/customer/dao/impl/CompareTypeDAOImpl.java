package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.customer.dao.CompareTypeDAO;
import com.tinhvan.hd.customer.model.CompareType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CompareTypeDAOImpl implements CompareTypeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CompareType> find() {
        List<CompareType> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from CompareType where active = " + HDConstant.STATUS.ENABLE);
        lst.addAll(query.getResultList());
        return lst;
    }
}
