/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.dao;

import com.tinhvan.hd.email.model.EmailTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author LUUBI
 */
@Repository
public class EmailTemplateDAOImpl implements EmailTemplateDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void create(EmailTemplate object) {
//        DAO.query((HDQuery) entityManager -> entityManager.persist(object));
//    }
//
//    @Override
//    public void update(EmailTemplate object) {
//        DAO.query((HDQuery) entityManager -> entityManager.merge(object));
//    }

    @Override
    public EmailTemplate findByTypeAndLangCode(int type, String langCode) {

        List<EmailTemplate> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        String hql = "FROM email_template WHERE type = :type AND lang_code = :langCode";
        Query query = entityManager.createQuery(hql);
        query.setParameter("type", type);
        query.setParameter("langCode", langCode);
        resultList.addAll(query.getResultList());
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public EmailTemplate findByUUID(UUID uuid) {
        List<EmailTemplate> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        String hql = "FROM EmailTemplate WHERE uuid = :uuid";
        Query query = entityManager.createQuery(hql);
        query.setParameter("uuid", uuid);
        resultList.addAll(query.getResultList());
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public List<EmailTemplate> list() {
        List<EmailTemplate> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        String hql = "FROM EmailTemplate";
        Query query = entityManager.createQuery(hql);
        resultList.addAll(query.getResultList());

        return resultList;
    }
}
