package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.bean.RoleListRequest;
import com.tinhvan.hd.bean.RoleListRespon;
import com.tinhvan.hd.dao.RoleDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


@Repository
//@Transactional
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void insertRole(RoleEntity role) {
//        DAO.query((em) -> {
//            em.persist(role);
//        });
//    }
//
//    @Override
//    public void updateRole(RoleEntity role) {
//        DAO.query((em) -> {
//            em.merge(role);
//        });
//    }
//
//    @Override
//    public void deleteRole(RoleEntity role) {
//        DAO.query((em) -> {
//            em.remove(role);
//        });
//    }
//
//    @Override
//    public RoleEntity getById(Long id) {
//        List<RoleEntity> ls = new ArrayList<>();
//        DAO.query((em) -> {
//            ls.add(em.find(RoleEntity.class, id));
//
//        });
//        return ls.get(0);
//    }

    @Override
    public RoleEntity findByRole(String role) {
        List<RoleEntity> list = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from RoleEntity where role = :role and status = :status ");
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("role", role);
        query.setParameter("status", 1);
        list.addAll(query.getResultList());
        if (!list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public List<RoleEntity> getAll(Integer status) {
        List<RoleEntity> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("from RoleEntity where status = :status ");

//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("status", status);
        ls.addAll(query.getResultList());

        return ls;
    }

    @Override
    public RoleListRespon getList(RoleListRequest roleListRequest) {

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("FROM RoleEntity WHERE 1=1 AND status = 1");
        String derection = roleListRequest.getDirection();
        int pageNum = roleListRequest.getPageNum();
        int pageSize = roleListRequest.getPageSize();
        if (!derection.equals("")) {
            if (derection.toUpperCase().equals("DESC")) {
                joiner.add("ORDER BY case when modifiedAt is not null then modifiedAt else createdAt end DESC");
            } else {
                joiner.add("ORDER BY case when modifiedAt is not null then modifiedAt else createdAt end ASC");
            }
        }

//        List<RoleEntity> lst = new ArrayList<>();
//        List<RoleListRespon> list = new ArrayList<>();
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(joiner.toString());
        int count = query.getResultList().size();
        query.setFirstResult((pageNum - 1) * pageSize);
        query.setMaxResults(pageSize);
//        lst = query.getResultList();
        return new RoleListRespon(query.getResultList(), count);
    }


}
