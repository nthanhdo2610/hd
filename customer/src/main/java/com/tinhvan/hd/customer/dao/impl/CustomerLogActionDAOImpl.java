package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.customer.dao.CustomerLogActionDAO;
import com.tinhvan.hd.customer.payload.CustomerLogActionResponse;
import com.tinhvan.hd.customer.payload.CustomerLogActionSearch;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class CustomerLogActionDAOImpl implements CustomerLogActionDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void create(CustomerLogAction object) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(object);
//        });
//    }

    @Override
    public List<CustomerLogActionResponse> search(CustomerLogActionSearch object) {

        List<CustomerLogActionResponse> list = new ArrayList<>();
        List<CustomerLogAction> listCustomerLogAction = new ArrayList<>();
        String derection = object.getDirection();
//        DAO.query((entityManager) -> {
//
//        });
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("from CustomerLogAction");
//                    " where customerId = :customerId and contractCode = :contractCode and objectName = :objectName and createdAt between :fromDate and :toDate");
        generalSearch(object, joiner);
        if (!derection.equals("")) {
            if (derection.toUpperCase().equals("DESC")) {
                joiner.add("ORDER BY id DESC");
            }else{
                joiner.add("ORDER BY id ASC");
            }
        }
        Query query = entityManager.createQuery(joiner.toString());
        setSearch(object, query);
        int count = query.getResultList().size();
        query.setFirstResult((object.getPageNum() - 1) * object.getPageSize());
        query.setMaxResults(object.getPageSize());
        listCustomerLogAction.addAll(query.getResultList());
        list.add(new CustomerLogActionResponse(listCustomerLogAction, count));

        return list;
    }

    /**
     * function used
     */
    void generalSearch(CustomerLogActionSearch filter, StringJoiner joiner) {
        boolean check = false;
        if (filter != null) {
            if (filter.getCustomerId() != null || filter.getContractCode().trim().length() != 0
                    || filter.getObjectName().trim().length() != 0 || filter.getFromDate() !=null || filter.getToDate() !=null) {
                joiner.add("WHERE");
                if (filter.getCustomerId() != null) {
                    check = true;
                    joiner.add("customerId = :customerId");
                }
                if (filter.getContractCode().trim().length() != 0) {
                    if (check) {
                        joiner.add("and contractCode = :contractCode");
                    } else {
                        check = true;
                        joiner.add("contractCode = :contractCode");
                    }
                }
                if (filter.getObjectName().trim().length() != 0) {
                    if (check) {
                        joiner.add("and objectName = :objectName");
                    } else {
                        check = true;
                        joiner.add("objectName = :objectName");
                    }
                }
                if(filter.getFromDate() !=null && filter.getToDate() !=null){
                    if (check) {
                        joiner.add("and createdAt >= :fromDate and createdAt <= :toDate");
                    } else {
                        joiner.add("createdAt >= :fromDate and createdAt <= :toDate");
                    }
                }else{
                    if(filter.getFromDate() !=null){
                        if (check) {
                            joiner.add("and createdAt >= :fromDate");
                        } else {
                            joiner.add("createdAt >= :fromDate");
                        }
                    }
                    if(filter.getToDate() !=null){
                        if (check) {
                            joiner.add("and createdAt <= :toDate");
                        } else {
                            joiner.add("createdAt <= :toDate");
                        }
                    }
                }

            }

        }
    }

    void setSearch(CustomerLogActionSearch filter, Query query) {
        if (query != null && filter != null) {
            if (filter.getCustomerId() != null) {
                query.setParameter("customerId", filter.getCustomerId());
            }
            if (filter.getContractCode().trim().length() != 0) {
                query.setParameter("contractCode", filter.getContractCode());
            }
            if (filter.getObjectName().trim().length() != 0) {
                query.setParameter("objectName", filter.getObjectName());
            }
            if(filter.getFromDate() !=null){
                query.setParameter("fromDate", filter.getFromDate());
            }
            if(filter.getToDate() !=null){
                query.setParameter("toDate", filter.getToDate());
            }

        }
    }
}
