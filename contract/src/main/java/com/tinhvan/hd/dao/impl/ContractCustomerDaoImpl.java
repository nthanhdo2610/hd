package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.dao.ContractCustomerDao;
import com.tinhvan.hd.entity.ContractCustomer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ContractCustomerDaoImpl implements ContractCustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ContractCustomer> findAllByCustomerUuidAndStatus(UUID customerUuid, Integer status) {
        List<ContractCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from ContractCustomer where 1=1 ");
        generateContractCustomer(customerUuid,status,queryBuilder);
//        DAO.query((em) -> {
//
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        setContractCustomer(customerUuid,status,query);
        ls.addAll(query.getResultList());
       return ls;
    }

    @Override
    public List<ContractCustomer> findAllByContractUuidAndStatus(UUID contractUuid, Integer status) {
        List<ContractCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from ContractCustomer where contractUuid= :contractUuid and status= :status");
//        DAO.query((em) -> {
//
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("contractUuid",contractUuid);
        query.setParameter("status",status);
        ls.addAll(query.getResultList());
        return ls;
    }

    public void generateContractCustomer(UUID customerUuid, Integer status, StringBuilder stringBuilder){

        if (customerUuid != null && !HDUtil.isNullOrEmpty(customerUuid.toString())){
            stringBuilder.append(" and customerUuid= :customerUuid");
        }

        if(status != null){
            stringBuilder.append(" and status= :status ");
        }

    }

    public void setContractCustomer(UUID customerUuid, Integer status, Query query){

        if (customerUuid != null && !HDUtil.isNullOrEmpty(customerUuid.toString())){
            query.setParameter("customerUuid",customerUuid);
        }

        if(status != null){
            query.setParameter("status",status);
        }
    }
}
