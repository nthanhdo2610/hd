package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.SchemeDao;
import com.tinhvan.hd.entity.Scheme;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Repository
public class SchemeDaoImpl implements SchemeDao {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void insertScheme(Scheme scheme) {
//        DAO.query((em) -> {
//            em.persist(scheme);
//        });
//    }
//
//    @Override
//    public void updateScheme(Scheme scheme) {
//        DAO.query((em) -> {
//            em.merge(scheme);
//        });
//    }
//
//    @Override
//    public void deleteScheme(Scheme scheme) {
//        DAO.query((em) -> {
//            em.remove(scheme);
//        });
//    }
//
//    @Override
//    public Scheme getById(Long id) {
//        List<Scheme> ls = new ArrayList<>();
//        DAO.query((em) -> {
//            ls.add(em.find(Scheme.class, id));
//
//        });
//        return ls.get(0);
//    }

    @Override
    public Scheme findByScheme(String schemeName) {
        List<Scheme> list = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Scheme where schemeName like '%:schemeName%'");
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("schemeName", schemeName);
        list.addAll(query.getResultList());
        if (!list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public List<Scheme> getAll() {
        List<Scheme> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("from Scheme");

//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        ls.addAll(query.getResultList());

        return ls;
    }
    @Override
    public List<Scheme> findBySchemeCode(String schemeCode) {
        List<Scheme> list = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Scheme where concat(',',schemeValue,',') like :schemeCode");
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("schemeCode", "%,"+schemeCode+",%");
        list.addAll(query.getResultList());
        return list;
    }

}
