package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.ConfigContractTypeBackgroundDao;
import com.tinhvan.hd.model.ConfigContractTypeBackground;
import com.tinhvan.hd.model.ConfigStaff;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
//@Transactional
public class ConfigContractTypeBackgroundDaoImpl implements ConfigContractTypeBackgroundDao {

//    @PersistenceContext
//    private EntityManager entityManager;
//    @Override
//    public ConfigContractTypeBackground insertOrUpdate(ConfigContractTypeBackground object) {
//        List<ConfigContractTypeBackground> resultList = new ArrayList<>();
//        DAO.query((em) -> {
//            resultList.add(em.merge(object));
//        });
//        if(!resultList.isEmpty()){
//            return resultList.get(0);
//        }
//        return null;
//    }

//    @Override
//    public List<ConfigContractTypeBackground> list() {
//        List<ConfigContractTypeBackground> resultList = new ArrayList<>();
////        DAO.query((entityManager) -> {
////
////        });
//        String hql = String.format("FROM ConfigContractTypeBackground");
//        Query query = entityManager.createQuery(hql);
//        resultList.addAll(query.getResultList());
//        return resultList;
//    }

//    @Override
//    public List<ConfigContractTypeBackground> findAllByPromotionType(int promotionType) {
//        List<ConfigContractTypeBackground> resultList = new ArrayList<>();
////        DAO.query((entityManager) -> {
////
////        });
//        String hql = String.format("FROM ConfigContractTypeBackground WHERE promotionType = :promotionType");
//        Query query = entityManager.createQuery(hql);
//        query.setParameter("promotionType", promotionType);
//        resultList.addAll(query.getResultList());
//        return resultList;
//    }

//    @Override
//    public ConfigContractTypeBackground findById(int id) {
//        List<ConfigContractTypeBackground> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//            String hql = String.format("FROM ConfigContractTypeBackground WHERE id = :id");
//            Query query = entityManager.createQuery(hql);
//            query.setParameter("id", id);
//            resultList.addAll(query.getResultList());
//        });
//        if(!resultList.isEmpty())
//        return resultList.get(0);
//        return null;
//    }

//    @Override
//    public ConfigContractTypeBackground findByContractType(String contractType) {
//        List<ConfigContractTypeBackground> resultList = new ArrayList<>();
////        DAO.query((entityManager) -> {
////
////        });
//        String hql = String.format("FROM ConfigContractTypeBackground WHERE contractType = :contractType");
//        Query query = entityManager.createQuery(hql);
//        query.setParameter("contractType", contractType);
//        resultList.addAll(query.getResultList());
//        if(!resultList.isEmpty())
//            return resultList.get(0);
//        return null;
//    }
}
