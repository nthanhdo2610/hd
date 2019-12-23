package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.bean.RoleAuthorizeListByRoleIdRespon;
import com.tinhvan.hd.bean.RoleAuthorizeRequest;
import com.tinhvan.hd.bean.RoleAuthorizeListRespon;
import com.tinhvan.hd.bean.RoleAuthorizeRespon;
import com.tinhvan.hd.dao.RoleAuthorizeDao;
import com.tinhvan.hd.model.RoleAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
//@Transactional
public class RoleAuthorizeDaoImpl implements RoleAuthorizeDao {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void insertOrUpdate(RoleAuthorize object) {
//        DAO.query((em) -> {
//            em.merge(object);
//        });
//    }

//    @Override
//    public void deleteRole() {
//        DAO.query((em) -> {
//            String hql = String.format("delete from %s","RoleAuthorize");
//            Query query = em.createQuery(hql);
//            query.executeUpdate();
//        });
//    }

//    @Override
//    public RoleAuthorize find(int id) {
//        List<RoleAuthorize> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//            String hql = String.format("FROM RoleAuthorize WHERE id = :id");
//            Query query = entityManager.createQuery(hql);
//            query.setParameter("id", id);
//            resultList.addAll(query.getResultList());
//        });
//        if (!resultList.isEmpty())
//            return resultList.get(0);
//        return null;
//    }

    @Override
    public RoleAuthorizeListRespon getlist(int roleId) {
        List<RoleAuthorizeRespon> listRoleAuthorizeRespon = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        String hql = "SELECT new com.tinhvan.hd.bean.RoleAuthorizeRespon((CASE WHEN ra.id is not null THEN ra.id ELSE 0 END) AS raId ,s.id, s.menu, s.crud, s.action, (CASE WHEN ra.status=1 THEN 1 ELSE 0 END) AS status) " +
                "FROM Services s LEFT JOIN RoleAuthorize ra ON s.id = ra.servicesId AND ra.roleId= :roleId order by s.id";
        Query query = entityManager.createQuery(hql, RoleAuthorizeRespon.class);
        query.setParameter("roleId", roleId);
        listRoleAuthorizeRespon.addAll(query.getResultList());

        return new RoleAuthorizeListRespon(roleId, listRoleAuthorizeRespon);
    }

    @Override
    public RoleAuthorize findByRoleIdAndServiceId(int roleId, int servicesId) {
        List<RoleAuthorize> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        String hql = String.format("FROM RoleAuthorize WHERE roleId = :roleId AND servicesId = :servicesId");
        Query query = entityManager.createQuery(hql);
        query.setParameter("roleId", roleId);
        query.setParameter("servicesId", servicesId);
        resultList.addAll(query.getResultList());
        if (!resultList.isEmpty())
            return resultList.get(0);
        return null;
    }

    @Override
    public List<RoleAuthorizeListByRoleIdRespon> getListByRoleId(int roleId) {
//        select ra.id as role_authorize_id, s.id as service_id, s.crud, s.entry_point, s.menu, s.action  from role_authorize ra
//        right join services s on ra.services_id = s.id
//        select s.menu from role_authorize ra right join services s on ra.services_id = s.id where ra.role_id=9 and ra.status=1
//        group by s.menu
        //get id by roleId

        List<RoleAuthorizeListByRoleIdRespon> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//
//        });
        String hql = "SELECT new com.tinhvan.hd.bean.RoleAuthorizeListByRoleIdRespon(ra.id as role_authorize_id, s.id as service_id, s.crud, s.entryPoint, s.menu, s.action, s.parent) " +
                "FROM RoleAuthorize ra right join Services s on ra.servicesId = s.id where ra.roleId = :roleId AND ra.status = 1 order by s.idx ASC";
//            String hql = "select s.menu from RoleAuthorize ra right join Services s on ra.servicesId = s.id where ra.roleId = :roleId and ra.status=1\n" +
//                    "group by s.menu";
        Query query = entityManager.createQuery(hql, RoleAuthorizeListByRoleIdRespon.class);
//            Query query = entityManager.createQuery(hql, String.class);
        query.setParameter("roleId", roleId);
        resultList.addAll(query.getResultList());

        return resultList;
    }
}
