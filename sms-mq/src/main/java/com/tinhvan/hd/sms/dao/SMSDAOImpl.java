/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.dao;

import com.tinhvan.hd.sms.model.SMS;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

/**
 * @author LUUBI
 */
@Component
public class SMSDAOImpl implements SMSDAO {

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public SMS createOrUpdate(SMS object) {
        EntityManager em = this.emf.createEntityManager();
        SMS smsResult = null;
        try {
            em.getTransaction().begin();
            smsResult = em.merge(object);
            em.getTransaction().commit();
            em.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return smsResult;
    }

    @Override
    public List<SMS> getList() {
        EntityManager em = this.emf.createEntityManager();
        List<SMS> list = null;
        try {
            String hql = "FROM sms";
            Query query = em.createQuery(hql);
            list = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return list;
    }

    @Override
    public long countSMS() {
        EntityManager em = this.emf.createEntityManager();
        long count = 0;
        try {
            String hql = "select count(*) FROM sms";
            Query query = em.createQuery(hql);
            count = (long) query.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return count;
    }

}
