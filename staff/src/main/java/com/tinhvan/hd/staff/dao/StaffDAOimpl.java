/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.dao;

import com.tinhvan.hd.staff.bean.StaffFindRoleId;
import com.tinhvan.hd.staff.bean.StaffList;
import com.tinhvan.hd.staff.bean.StaffSearch;
import com.tinhvan.hd.staff.bean.StaffSearchRespon;
import com.tinhvan.hd.staff.filter.StaffFilter;
import com.tinhvan.hd.staff.model.Staff;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author LUUBI
 */
@Repository
public class StaffDAOimpl implements StaffDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void createOrUpdate(Staff object) {
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//                entityManager.merge(object);
//            }
//        });
//    }

    @Override
    public Staff existEmail(String email, int status) {
        List<Staff> resultList = new ArrayList<>();
//        DAO.query(new HDQuery[]{(HDQuery) entityManager -> {
//
//        }});
        String hql = "FROM Staff WHERE email = :email AND status = :status";
        Query query = entityManager.createQuery(hql);
        query.setParameter("email", email);
        query.setParameter("status", status);
        resultList = query.getResultList();
//        resultList.addAll(query.getResultList());
        if (resultList != null && !resultList.isEmpty())
            return resultList.get(0);
        return null;
    }

    @Override
    public Staff findByUUID(UUID uuid) {
        List<Staff> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        String hql = "FROM Staff WHERE id = :id AND status = 1";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id", uuid);
        resultList.addAll(query.getResultList());
        if (resultList != null && !resultList.isEmpty())
            return resultList.get(0);
        return null;
    }


    @Override
    public StaffSearchRespon search(StaffSearch staffSearch) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("SELECT new com.tinhvan.hd.staff.bean.StaffList(s.id, s.email, s.fullName, s.department, s.area, s.roleCode, r.name, s.objectVersion, s.status)" +
                " FROM Staff s inner join RoleEntity r on s.roleCode = r.role And r.status = 1");
//        select s.id, s.email, s.department, s.area, s.staff_token, s.status,  s.object_version, r.name from staff s
//        inner join RoleEntity r on s.role_id = r.role
//        (int roleId, String roleName, String staffToken, int objectVersion, int status)
        String derection = staffSearch.getDirection();
        int pageNum = staffSearch.getPageNum();
        int pageSize = staffSearch.getPageSize();
        generalSearch(staffSearch, joiner);
        if (!derection.equals("")) {
            if (derection.toUpperCase().equals("DESC")) {
                joiner.add("ORDER BY s.fullName DESC");
            } else {
                joiner.add("ORDER BY s.fullName ASC");
            }
        }

        List<StaffList> listStaffList = null;
        Query query = entityManager.createQuery(joiner.toString());
        System.out.println(joiner.toString());
        setSearch(staffSearch, query);
        int count = query.getResultList().size();
        query.setFirstResult((pageNum - 1) * pageSize);
        query.setMaxResults(pageSize);
        listStaffList = query.getResultList();
        if (listStaffList != null) {
            return new StaffSearchRespon(listStaffList, count);
        }
        //
        listStaffList = new ArrayList<>();
        count = 0;
        return new StaffSearchRespon(listStaffList, count);
    }


    @Override
    public long findRoleIdByUUID(UUID uuid) {
        StringJoiner joiner = new StringJoiner(" ");
        List<StaffFindRoleId> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        joiner.add("select new com.tinhvan.hd.staff.bean.StaffFindRoleId(r.id) from Staff s inner join RoleEntity r on s.roleCode= r.role and  s.id = :id");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("id", uuid);
        resultList = query.getResultList();
//        resultList.addAll(query.getResultList());
        if (resultList != null && !resultList.isEmpty())
            return resultList.get(0).getRoleId();
        return 0;
    }

    @Override
    public Staff findByToken(String token) {
        StringJoiner joiner = new StringJoiner(" ");
        List<Staff> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        joiner.add("FROM Staff WHERE staffToken = :staffToken");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("staffToken", token);
        resultList.addAll(query.getResultList());
        if (resultList != null && !resultList.isEmpty())
            return resultList.get(0);
        return null;
    }

    /**
     * function used
     */
    void generalSearch(StaffSearch filter, StringJoiner joiner) {
        if (filter != null) {
            if (filter.getRole().length() != 0) {
                joiner.add("AND s.roleCode = :roleCode");
            }
            if (filter.getDepartment().length() != 0) {
                joiner.add("AND s.department = :department");
            }
            if (filter.getKey().length() != 0) {
                joiner.add("AND ( s.email LIKE :email or s.fullName LIKE :fullName) ");
//                joiner.add("AND s.fullName LIKE :fullName");
            }

        }
    }

    void setSearch(StaffSearch filter, Query query) {
        System.out.println(filter.toString());
        if (query != null && filter != null) {
            if (filter.getRole().length() != 0) {
                query.setParameter("roleCode", String.valueOf(filter.getRole()));
            }
            if (filter.getDepartment().length() != 0) {
                query.setParameter("department", filter.getDepartment());
            }
            if (filter.getKey().length() != 0) {
                query.setParameter("email", "%" + filter.getKey() + "%");
                query.setParameter("fullName", "%" + filter.getKey() + "%");
            }
        }
    }

    //no use
    @Override
    public List<Staff> getList(StaffFilter filter) {
        String order = filter.getOrder();
        Object next = filter.next();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("FROM Staff WHERE 1=1");
//        generalFilter(filter, joiner);
        if (order != null && next != null) {
            if (order.equals("CreatedAtDesc")) {
                joiner.add("AND createdAt < :next");
                joiner.add("ORDER BY createdAt DESC");
            }
            if (order.equals("CreatedAtAsc")) {
                joiner.add("AND createdAt > :next");
                joiner.add("ORDER BY createdAt ASC");
            }
        }

        List<Staff> lst = new ArrayList<>();
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(joiner.toString());
//            setFilter(filter, query);
        if (order != null && !order.isEmpty()) {
            if (next != null) {
                query.setParameter("next", next);
            }
        }
        query.setMaxResults(filter.getLimit());
        lst.addAll(query.getResultList());
        return lst;

    }

    @Override
    public int num(StaffFilter filter) {
        List resultList = new ArrayList();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("SELECT COUNT(*) FROM Staff WHERE 1=1");
//        generalFilter(filter, joiner);
//        DAO.query((EntityManager entityManager) -> {
//
//        });
        String hql = joiner.toString();
        Query query = entityManager.createQuery(hql);
//            setFilter(filter, query);
        resultList.addAll(query.getResultList());
        if (resultList != null && !resultList.isEmpty()) {
            return Integer.parseInt(String.valueOf(resultList.get(0)));
        }
        return 0;
    }

    @Override
    public int checkRoleApiByUser(UUID userId, String api) {
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("p_check_authen_api");
        storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        storedProcedure.setParameter(1, userId.toString());
        storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        storedProcedure.setParameter(2, api);
        List<Integer> lst = storedProcedure.getResultList();
        if (lst != null && lst.size() > 0)
            return lst.get(0);
        return 0;
    }

//    void generalFilter(StaffFilter filter, StringJoiner joiner) {
//        if (filter != null) {
//            if (filter.getRole() != 0) {
//                joiner.add("AND role_id = :role_id");
//            }
//            if (filter.getDepartment().length() != 0) {
//                joiner.add("AND department = :department");
//            }
//            if (filter.getKey().length() != 0) {
//                joiner.add("AND email LIKE :email");
//                joiner.add("OR full_name LIKE :full_name");
//            }
//
//        }
//    }

//    void setFilter(StaffFilter filter, Query query) {
//        if (query != null && filter != null) {
//            if (filter.getRole() != 0) {
//                query.setParameter("role_id", filter.getRole());
//            }
//            if (filter.getDepartment().length() != 0) {
//                query.setParameter("department", filter.getDepartment());
//            }
//            if (filter.getKey().length() != 0) {
//                query.setParameter("email", "%" + filter.getKey() + "%");
//                query.setParameter("full_name", "%" + filter.getKey() + "%");
//            }
//        }
//    }


}
