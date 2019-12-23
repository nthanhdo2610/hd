/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.dao;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDQuery;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.sms.model.SMS;

import java.util.ArrayList;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

/**
 * @author LUUBI
 */
@Repository
public class SMSDAOImpl implements SMSDAO {

//    @Override
//    public void create(SMS object) {
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//                entityManager.persist(object);
//            }
//        });
//
//    }
//
//    @Override
//    public void update(SMS object) {
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//                entityManager.merge(object);
//            }
//        });
//
//    }

}
