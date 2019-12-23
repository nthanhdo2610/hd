package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDEntityManager;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.customer.dao.CustomerLogDAO;
import com.tinhvan.hd.customer.model.CustomerLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CustomerLogDAOImpl implements CustomerLogDAO {

//    public void insert(CustomerLog log) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(log);
//        });
//    }
}
