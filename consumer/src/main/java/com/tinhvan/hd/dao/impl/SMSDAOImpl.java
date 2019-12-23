/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.SMSDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.tinhvan.hd.entity.SMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LUUBI
 */
@Repository
public class SMSDAOImpl implements SMSDAO {

    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public SMS createOrUpdate(SMS object) {
        return entityManager.merge(object);
    }

    @Override
    public SMS findByUUID(UUID uuid) {
        List<SMS> list = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from SMS where uuid = :uuid ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("uuid", uuid);
        list.addAll(query.getResultList());
        if(list != null)
            return list.get(0);
        return null;
    }

    @Override
    public List<SMS> getList(int size) {
        List<SMS> list = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from SMS where status = 0 order by createdAt ASC ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setMaxResults(size);
        list.addAll(query.getResultList());
        return list;
    }

}
