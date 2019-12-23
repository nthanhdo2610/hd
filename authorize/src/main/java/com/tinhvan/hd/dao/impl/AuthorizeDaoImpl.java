package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.enities.AuthorizeUserEntity;
import com.tinhvan.hd.dao.AuthorizeDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthorizeDaoImpl implements AuthorizeDao {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void insertAuthorize(AuthorizeUserEntity authorize) {
//        DAO.query((em) -> {
//            em.persist(authorize);
//        });
//    }
//
//    @Override
//    public void updateAuthorize(AuthorizeUserEntity authorize) {
//        DAO.query((em) -> {
//            em.merge(authorize);
//        });
//    }
//
//    @Override
//    public void deleteAuthorize(AuthorizeUserEntity authorize) {
//        DAO.query((em) -> {
//            em.remove(authorize);
//        });
//    }
//
//    @Override
//    public AuthorizeUserEntity getById(Long id) {
//        List<AuthorizeUserEntity> ls = new ArrayList<>();
//        DAO.query((em) -> {
//            ls.add(em.find(AuthorizeUserEntity.class,id));
//
//        });
//        return ls.get(0);
//    }

    @Override
    public List<AuthorizeUserEntity> getAll(Integer status) {
        List<AuthorizeUserEntity> listResult = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from AuthorizeUserEntity where status= :status ");

//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("status",status);
        listResult = query.getResultList();
        return listResult;
    }
}
