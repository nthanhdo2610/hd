package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.ContractDisbursementInfoDao;
import com.tinhvan.hd.entity.ContractDisbursementInfo;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractDisbursementInfoDaoImpl implements ContractDisbursementInfoDao {
//    @Override
//    public void create(ContractDisbursementInfo contractDisbursementInfo) {
//        DAO.query((em) -> {
//            em.persist(contractDisbursementInfo);
//        });
//    }
//
//    @Override
//    public void update(ContractDisbursementInfo contractDisbursementInfo) {
//        DAO.query((em) -> {
//            em.merge(contractDisbursementInfo);
//        });
//    }
//
//    @Override
//    public void delete(ContractDisbursementInfo contractDisbursementInfo) {
//        DAO.query((em) -> {
//            em.remove(contractDisbursementInfo);
//        });
//    }
//
//    @Override
//    public ContractDisbursementInfo getById(Integer id) {
//        List<ContractDisbursementInfo> ls = new ArrayList<>();
//        DAO.query((em) -> {
//            ls.add(em.find(ContractDisbursementInfo.class,id));
//
//        });
//        return ls.get(0);
//    }
}
