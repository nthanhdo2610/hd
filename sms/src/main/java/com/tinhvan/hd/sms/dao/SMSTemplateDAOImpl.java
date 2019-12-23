/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.dao;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDQuery;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.sms.bean.SMSTemplateList;
import com.tinhvan.hd.sms.bean.SMSTemplateListRespon;
//import com.tinhvan.hd.sms.filter.SMSTemplateFilter;
import com.tinhvan.hd.sms.model.SMSTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 * @author LUUBI
 */
@Repository
public class SMSTemplateDAOImpl implements SMSTemplateDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void create(SMSTemplate object) {
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//                entityManager.persist(object);
//            }
//        });
//    }
//
//    @Override
//    public void update(SMSTemplate object) {
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//                entityManager.merge(object);
//            }
//        });
//    }

    @Override
    public SMSTemplate findByTypeAndLangCode(String smsType, String langCode) {
        List<SMSTemplate> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        String hql = String.format("FROM SMSTemplate WHERE smsType = :smsType AND langCode = :langCode");
        Query query = entityManager.createQuery(hql);
        query.setParameter("smsType", smsType);
        query.setParameter("langCode", langCode);
        resultList = query.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public SMSTemplate findByUUID(UUID uuid) {

        List<SMSTemplate> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("FROM SMSTemplate WHERE uuid = :uuid");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("uuid", uuid);
        resultList = query.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public SMSTemplateListRespon getList(SMSTemplateList smsTemplateList) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("FROM SMSTemplate WHERE 1=1 AND status = 1");
        String derection = smsTemplateList.getDirection();
        int pageNum = smsTemplateList.getPageNum();
        int pageSize = smsTemplateList.getPageSize();
        if (!derection.equals("")) {
            if (derection.toUpperCase().equals("DESC")) {
                joiner.add("ORDER BY case when modifiedAt is not null then modifiedAt else createdAt end DESC");
            } else {
                joiner.add("ORDER BY case when modifiedAt is not null then modifiedAt else createdAt end ASC");
            }
        }

        List<SMSTemplate> lst = new ArrayList<>();
        List<SMSTemplateListRespon> list = new ArrayList<>();
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(joiner.toString());
        int count = query.getResultList().size();
        query.setFirstResult((pageNum - 1) * pageSize);
        query.setMaxResults(pageSize);
        lst.addAll(query.getResultList());
        if(lst != null && !lst.isEmpty()){
        list.add(new SMSTemplateListRespon(lst, count));
        return list.get(0);
        }
        return null;
    }



//    @Override
//    public List<SMSTemplate> getList() {
//        String order = filter.getOrder();
//        Object next = filter.next();
//        StringJoiner joiner = new StringJoiner(" ");
//        joiner.add("FROM sms_template WHERE 1=1");
//        generalFilter(filter, joiner);
//        if (order != null && next != null) {
//            joiner.add("AND create_at < :next");
//        }
//        if (order.equals("CreatedAtDesc")) {
//            joiner.add("ORDER BY create_at DESC");
//        } else if (order.equals("CreatedAtAsc")) {
//            joiner.add("ORDER BY create_at ASC");
//        }
//        List<SMSTemplate> lst = new ArrayList<>();
//        DAO.query((em) -> {
//            Query query = em.createQuery(joiner.toString());
//            setFilter(filter, query);
//            if (order != null && !order.isEmpty()) {
//                if (next != null) {
//                    query.setParameter("next", next);
//                }
//            }
//            query.setMaxResults(filter.getLimit());
//            lst.addAll(query.getResultList());
//        });
//        return lst;
//    }

//    @Override
//    public int num(SMSTemplateFilter filter) {
//        List resultList = new ArrayList();
//        StringJoiner joiner = new StringJoiner(" ");
//        joiner.add("SELECT COUNT(*) FROM sms_template WHERE 1=1");
//        generalFilter(filter, joiner);
//        DAO.query((EntityManager entityManager) -> {
//            String hql = joiner.toString();
//            Query query = entityManager.createQuery(hql);
//            setFilter(filter, query);
//            resultList.addAll(query.getResultList());
//        });
//        if (!resultList.isEmpty()) {
//            return Integer.parseInt(String.valueOf(resultList.get(0)));
//        }
//        return 0;
//    }
//
//    /**
//     * function used
//     */
//    void generalFilter(SMSTemplateFilter filter, StringJoiner joiner) {
//        if (filter != null) {
//            if (filter.getType() != 0) {
//                joiner.add("AND type = :type");
//            }
//        }
//    }
//
//    void setFilter(SMSTemplateFilter filter, Query query) {
//        if (query != null && filter != null) {
//            if (filter.getType() != 0) {
//                query.setParameter("type", filter.getType());
//            }
//        }
//    }

}
