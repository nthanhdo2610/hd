package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.customer.dao.LoginConfigDAO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoginConfigDAOImpl implements LoginConfigDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int find(int count) {
        List<Integer> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery("select lockedTime from LoginConfig where countTime =" + count);
        lst .addAll(query.getResultList());
        if (lst != null && lst.size() > 0)
            return lst.get(0).intValue();
        else
            return 0;
    }
}
