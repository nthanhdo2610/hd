package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.ConfigAdjustmentContractDAO;
import com.tinhvan.hd.model.ConfigAdjustmentContract;
import com.tinhvan.hd.repository.ConfigAdjustmentContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
//@Transactional
public class ConfigAdjustmentContractImpl implements ConfigAdjustmentContractDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ConfigAdjustmentContractRepository configAdjustmentContractRepository;

//    @Override
//    public void create(ConfigAdjustmentContract object) {
//        DAO.query((em) -> {
//            em.persist(object);
//        });
//    }
//
//    @Override
//    public void update(ConfigAdjustmentContract object) {
//        DAO.query((em) -> {
//            em.merge(object);
//        });
//    }

    @Override
    public List<ConfigAdjustmentContract> getList() {
        List<ConfigAdjustmentContract> resultList = new ArrayList<>();
//        resultList = configAdjustmentContractRepository.findAllByOrderByIdxAsc();
        return resultList;
    }

    @Override
    public List<String> getListByIsCheckDocument() {
        List<String> resultList = new ArrayList<>();
//        resultList = configAdjustmentContractRepository.getListByIsCheckDocument();
        return resultList;
    }

    @Override
    public ConfigAdjustmentContract getConfigAdjustmentContract(String code) {
//        List<ConfigAdjustmentContract> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
//        String hql = "FROM ConfigAdjustmentContract WHERE code = :code";
//        Query query = entityManager.createQuery(hql);
//        query.setParameter("code", code);
//        resultList.addAll(query.getResultList());
//        if (!resultList.isEmpty())
//            return resultList.get(0);
        return null;
    }
}
