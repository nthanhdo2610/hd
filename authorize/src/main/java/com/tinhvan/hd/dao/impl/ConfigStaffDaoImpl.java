package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.enities.ConfigEntity;
import com.tinhvan.hd.base.repository.ConfigEntityRepository;
import com.tinhvan.hd.bean.ConfigStaffListContractTypeRespon;
import com.tinhvan.hd.dao.ConfigStaffDao;
import com.tinhvan.hd.model.ConfigStaff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class ConfigStaffDaoImpl implements ConfigStaffDao {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ConfigEntityRepository configEntityRepository;

    long getUnixTime(String key, long value) {
        //years
        if (key.toUpperCase().equals("Y") || key.toUpperCase().equals("YY") || key.toUpperCase().equals("YYYY"))
            return value * 60 * 60 * 24 * 30 * 12;
        //months
        if (key.toUpperCase().equals("MM"))
            return value * 60 * 60 * 24 * 30;
        //days
        if (key.toUpperCase().equals("D") || key.toUpperCase().equals("DD"))
            return value * 60 * 60 * 24;
        //hours
        if (key.toUpperCase().equals("H") || key.toUpperCase().equals("HH"))
            return value * 60 * 60;
        //minutes
        if (key.toUpperCase().equals("M"))
            return value * 60;
        //seconds
        if (key.toUpperCase().equals("S") || key.toUpperCase().equals("SS"))
            return value;
        return -1;
    }

    void syncConfigSystem(ConfigStaff object) {
        if (!HDUtil.isNullOrEmpty(object.getValuePara()) && !HDUtil.isNullOrEmpty(object.getValue())) {
            long unixTime = getUnixTime(object.getValuePara(), Long.valueOf(object.getValue()));
            if (unixTime >= 0) {
                String configStaffKey = object.getKey();
                String[] value = {unixTime+""};
                ConfigEntity configEntity = new ConfigEntity();
                configEntity.setValue(value);
                boolean check = false;
                switch (configStaffKey){
                    case "time_out_mobile_app":
                        check = true;
                        configEntity.setKey("JWT_EXPIRED_TIME_APP");
                        break;
                    case "time_out_web_admin":
                        check = true;
                        configEntity.setKey("JWT_EXPIRED_TIME_WEB_ADMIN");
                        break;
                    case "time_out_change_password":
                        check = true;
                        configEntity.setKey("PASSWORD_EXPIRED_TIME");
                        break;
                    case "time_out_web_esign_portal":
                        check = true;
                        configEntity.setKey("JWT_EXPIRED_TIME_WEB_ESIGN");
                        break;
                    default:
                        break;
                }
                if(check){
                configEntityRepository.save(configEntity);
                }
//                String[] newString = {unixTime+""};
//                configEntityRepository.updateConfigEntity(newString, configStaffKey);
//                StringJoiner joiner = new StringJoiner(" ");
//                joiner.add("update system_config");
//                joiner.add("set value = '{" + unixTime + "}'");
//                joiner.add("where config_staff_key = '" + configStaffKey + "'");
//                Query query = entityManager.createNativeQuery(joiner.toString());
//                query.executeUpdate();
//                DAO.query((em) -> {
//                    Query query = em.createNativeQuery(joiner.toString());
//                    query.executeUpdate();
//                });

            }
        }
    }

    @Override
    public void insertOrUpdate(ConfigStaff object) {
//        DAO.query((em) -> {
//            em.merge(object);
//        });
//        entityManager.merge(object);
        if (!HDUtil.isNullOrEmpty(object.getValuePara())) {
            syncConfigSystem(object);
        }
    }
//
//    @Override
//    public List<ConfigStaff> list() {
//        List<ConfigStaff> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//            String hql = String.format("FROM ConfigStaff");
//            Query query = entityManager.createQuery(hql);
//            resultList.addAll(query.getResultList());
//        });
//        return resultList;
//    }

    @Override
    public String getValueByKey(String key) {
        List<ConfigStaff> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        String hql = String.format("FROM ConfigStaff WHERE key = :key");
        Query query = entityManager.createQuery(hql);
        query.setParameter("key", key);
        resultList.addAll(query.getResultList());
        if (!resultList.isEmpty())
            return resultList.get(0).getValue();
        return null;
    }

    @Override
    public List<ConfigStaffListContractTypeRespon> getListByContractType() {
        List<ConfigStaffListContractTypeRespon> resultList = new ArrayList<>();
        StringJoiner stringJoiner = new StringJoiner(" ");
//        DAO.query((entityManager) -> {
//
//        });
        stringJoiner.add("select new com.tinhvan.hd.bean.ConfigStaffListContractTypeRespon(cs.key, cs.value) from ConfigStaff cs where key like 'contract_type%'");
        Query query = entityManager.createQuery(stringJoiner.toString());
        resultList.addAll(query.getResultList());
        return resultList;
    }

//    @Override
//    public ConfigStaff findById(int id) {
//        List<ConfigStaff> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//            String hql = String.format("FROM ConfigStaff WHERE id = :id");
//            Query query = entityManager.createQuery(hql);
//            query.setParameter("id", id);
//            resultList.addAll(query.getResultList());
//        });
//        if (resultList != null && !resultList.isEmpty())
//            return resultList.get(0);
//        return null;
//    }

}
