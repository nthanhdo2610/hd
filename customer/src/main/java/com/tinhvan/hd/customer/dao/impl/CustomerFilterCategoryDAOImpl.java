package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.customer.dao.CustomerFilterCategoryDAO;
import com.tinhvan.hd.customer.model.CustomerFilterCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerFilterCategoryDAOImpl implements CustomerFilterCategoryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CustomerFilterCategory> find() {
        List<CustomerFilterCategory> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from CustomerFilterCategory where active = " + HDConstant.STATUS.ENABLE);
        lst.addAll(query.getResultList());
        return lst;
    }
}
