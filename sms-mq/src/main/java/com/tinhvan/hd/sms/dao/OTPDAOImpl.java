/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.dao;

import com.tinhvan.hd.sms.model.OTP;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.springframework.stereotype.Component;

/**
 *
 * @author LUUBI
 */
@Component
public class OTPDAOImpl implements OTPDAO {

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public OTP createOrUpdate(OTP object) {
        EntityManager em = this.emf.createEntityManager();
        OTP otpResult = null;
        try {
            em.getTransaction().begin();
            otpResult = em.merge(object);
            em.getTransaction().commit();
            em.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return otpResult;
    }

}
