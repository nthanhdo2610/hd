package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.customer.dao.CustomerImageDAO;
import com.tinhvan.hd.customer.model.CustomerImage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomerImageDAOImpl implements CustomerImageDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public CustomerImage insert(CustomerImage image) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(image);
//        });
//        return image;
//    }
//
//    @Override
//    public void update(CustomerImage image) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(image);
//        });
//    }

    @Override
    public void delete(UUID uuid, Date modifiedAt, int type) {
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("update CustomerImage set active = :active, modifiedAt = :modifiedAt  where uuid = :uuid and type = :type");
        query.setParameter("active", HDConstant.STATUS.DISABLE);
        query.setParameter("modifiedAt", modifiedAt);
        query.setParameter("uuid", uuid);
        query.setParameter("type", type);
        query.executeUpdate();
    }

    @Override
    public CustomerImage find(UUID uuid, String fileName, int type) {
        List<CustomerImage> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from CustomerImage where uuid = :uuid and fileName = :fileName and type = :type");
        query.setParameter("uuid", uuid);
        query.setParameter("fileName", fileName);
        query.setParameter("type", type);
        lst.addAll(query.getResultList());
        if (lst.size()>0)
            return lst.get(0);
        return null;
    }

    @Override
    public CustomerImage findByType(UUID uuid, int type) {
        List<CustomerImage> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("from CustomerImage where uuid = :uuid and active = :active and type = :type");
        query.setParameter("uuid", uuid);
        query.setParameter("active", HDConstant.STATUS.ENABLE);
        query.setParameter("type", type);
        lst.addAll(query.getResultList());
        if (lst.size()>0)
            return lst.get(0);
        return null;
    }

}
