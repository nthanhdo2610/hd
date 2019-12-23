/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDQuery;
import com.tinhvan.hd.sms.bean.OTPLimitRespon;
import com.tinhvan.hd.sms.bean.OTPVerifyResult;
import com.tinhvan.hd.sms.bean.SMSVerifyOTP;
import com.tinhvan.hd.sms.model.OTP;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;

/**
 * @author LUUBI
 */
@Repository
public class OTPDAOImpl implements OTPDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void create(OTP object) {
//        DAO.query((HDQuery) entityManager -> entityManager.persist(object));
//    }
//
//    @Override
//    public void update(OTP object) {
//        DAO.query((HDQuery) entityManager -> entityManager.merge(object));
//    }

    @Override
    public List<OTPVerifyResult> verifyOTP(SMSVerifyOTP object) {
//        List<OTP> resultList = new ArrayList<>();
        List<OTPVerifyResult> resultList = new ArrayList<>();

//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        UUID uuid = object.getContractUUID();
        String contactId = uuid == null ? "is null" : "= :contract_uuid";
        String hql = "select new com.tinhvan.hd.sms.bean.OTPVerifyResult(uuid, otpType, otpCode, status"
//                        + ", created_at + (process_time * interval '1 second') - now() tg)"
                + ", processTime"
                // + ", (createdAt + (processTime * interval '1 second')) as expiredTime)"
                + ", createdAt"
                + ", NOW() as currentDate)"
                + " FROM OTP WHERE status in (0,2)  AND customerUUID = :customer_uuid" +
                " AND otpType = :otp_type AND contractUUID " + contactId;
        // + " AND created_at + (process_time * interval '1 second') >= now()";

        Query query = entityManager.createQuery(hql);
        // query.setParameter("otp_code", object.getCodeOTP());
        query.setParameter("customer_uuid", object.getCustomerUUID());
        query.setParameter("otp_type", object.getOtpType());
        if (uuid != null) {
            query.setParameter("contract_uuid", uuid);
        }
        resultList = query.getResultList();
        return resultList;
    }

    @Override
    public OTP findByUUID(UUID uuid) {
        List<OTP> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        String hql = "FROM OTP WHERE uuid = :uuid";
        Query query = entityManager.createQuery(hql);
        query.setParameter("uuid", uuid);
        resultList.addAll(query.getResultList());
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public OTPLimitRespon getLimitOTP(String customerUUID) {
        List<OTPLimitRespon> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("p_otp_count_sent")
                .registerStoredProcedureParameter(
                        "customerUUID",
                        String.class,
                        ParameterMode.IN
                )
//                        .registerStoredProcedureParameter(
//                                "otpLimitRespon",
//                                OTPLimitRespon.class,
//                                ParameterMode.OUT
//                        )
                .setParameter("customerUUID", customerUUID);
        query.execute();
        List<Object[]> rows = query.getResultList();
        for (Object[] row : rows) {
            resultList.add(new OTPLimitRespon((int)row[0], (int)row[1]));
        }
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public String getPhoneNumber(SMSVerifyOTP object) {
        List<String> resultList = new ArrayList<>();

//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("SELECT phone FROM OTP WHERE customerUUID =:customerUUID AND otpType = :otpType AND otpCode = :otpCode");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("customerUUID", object.getCustomerUUID());
        query.setParameter("otpType", object.getOtpType());
        query.setParameter("otpCode", object.getCodeOTP());
        resultList.addAll(query.getResultList());

        if(resultList != null && !resultList.isEmpty())
            return resultList.get(0);
        return "";
    }

    //    @Override
//    public OTP verifyOTP(SMSVerifyOTP object) {
//        List<OTP> resultList = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//                UUID uuid = object.getContractUUID();
//                String contactId = uuid == null ? "is null" : "= :contract_uuid";
//                String hql = "FROM otp WHERE status = 0 AND otp_code = :otp_code AND customer_uuid = :customer_uuid" +
//                        " AND otp_type = :otp_type AND contract_uuid " + contactId ;
//                Query query = entityManager.createQuery(hql);
//                query.setParameter("otp_code", object.getCodeOTP());
//                query.setParameter("customer_uuid", object.getCustomerUUID());
//                query.setParameter("otp_type", object.getOtpType());
//                if(uuid!=null){
//                    query.setParameter("contract_uuid", uuid);
//                }
//                resultList.addAll(query.getResultList());
//            }
//        });
//        if (!resultList.isEmpty()) {
//            return resultList.get(0);
//        }
//        return null;
//    }

    @Override
    public Date getTimeNow() {
        List<Date> list = new ArrayList<>();
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//
//            }
//        });
        String hql = "SELECT NOW()";
        Query query = entityManager.createNativeQuery(hql);
        query.getResultList();
        list.addAll(query.getResultList());

        return list.get(0);
    }


}
