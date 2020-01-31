package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.ConfigSendMailDtlDAO;
import com.tinhvan.hd.model.ConfigSendMailDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class ConfigSendMailDtlDAOImpl implements ConfigSendMailDtlDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ConfigSendMailDtl> findByConfigSendMailId(long configId) {
        List<ConfigSendMailDtl> lst = new ArrayList<>();
        Query query = entityManager.createQuery("from ConfigSendMailDtl where configSendMailId = :configId order by province, district asc");
        query.setParameter("configId", configId);
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public List<ConfigSendMailDtl> generateTemplate() {
        List<ConfigSendMailDtl> lst = new ArrayList<>();
        CharSequence separator;
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("select new com.tinhvan.hd.model.ConfigSendMailDtl(p.provinceName, d.districtName)");
        joiner.add("from Province p left join District d on p.id = d.provinceId");
        joiner.add("order by p.provinceName, d.districtName");
        Query query = entityManager.createQuery(joiner.toString());
        lst.addAll(query.getResultList());
        return lst;
    }
}
