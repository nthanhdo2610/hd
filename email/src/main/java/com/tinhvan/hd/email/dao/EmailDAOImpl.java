/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.dao;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDQuery;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.email.model.Email;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.tinhvan.hd.email.model.EmailTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author LUUBI
 */
@Repository
public class EmailDAOImpl implements EmailDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void create(Email object) {
//        DAO.query((HDQuery) entityManager -> entityManager.persist(object));
//    }
//
//    @Override
//    public void update(Email object) {
//        DAO.query((HDQuery) entityManager -> entityManager.merge(object));
//    }

    @Override
    public Email existEmail(String mail) {
        List<Email> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        String hql = "FROM Email WHERE email = :email AND status = 1";
        Query query = entityManager.createQuery(hql);
        query.setParameter("email", mail);
        resultList.addAll(query.getResultList());
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public Email findByUUID(UUID uuid) {
        List<Email> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        String hql = "FROM Email WHERE uuid = :uuid";
        Query query = entityManager.createQuery(hql);
        query.setParameter("uuid", uuid);
        resultList.addAll(query.getResultList());
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

}
