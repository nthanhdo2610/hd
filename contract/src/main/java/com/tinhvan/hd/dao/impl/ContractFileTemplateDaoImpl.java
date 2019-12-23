package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.ContractFileTemplateDao;
import com.tinhvan.hd.entity.ContractFileTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class ContractFileTemplateDaoImpl implements ContractFileTemplateDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ContractFileTemplate> findByType(String type) {
        List<ContractFileTemplate> lst = new ArrayList<>();
        try {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add("from ContractFileTemplate where type=:type");
            joiner.add("order by idx asc");
            Query query = entityManager.createQuery(joiner.toString());
            query.setParameter("type", type);
            lst = query.getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return lst;
    }
}
