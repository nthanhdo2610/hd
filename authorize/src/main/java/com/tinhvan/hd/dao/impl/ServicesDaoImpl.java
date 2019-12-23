package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.dao.ServicesDao;
import com.tinhvan.hd.model.Services;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
//@Transactional
public class ServicesDaoImpl implements ServicesDao {
    @Override
    public void insertServices(Services object) {
        DAO.query((em) -> {
            em.persist(object);
        });
    }

    @Override
    public void update(Services object) {
        DAO.query((em) -> {
            em.merge(object);
        });
    }

    @Override
    public List<Services> list() {
        List<Services> list = new ArrayList<>();
        DAO.query((em) -> {
            String hql = "FROM Services order by idx";
            Query query = em.createQuery(hql);
            list.addAll(query.getResultList());
        });
        return list;
    }
}
