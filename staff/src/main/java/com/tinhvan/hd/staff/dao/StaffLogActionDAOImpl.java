package com.tinhvan.hd.staff.dao;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDQuery;
import com.tinhvan.hd.staff.model.StaffLogAction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StaffLogActionDAOImpl implements StaffLogActionDAO {

//    @Override
//    public void create(StaffLogAction object) {
//        DAO.query(new HDQuery() {
//            @Override
//            public void execute(EntityManager entityManager) {
//                entityManager.persist(object);
//            }
//        });
//    }

    @Override
    public List<StaffLogAction> list() {
        List<StaffLogAction> resultList = new ArrayList<>();
        DAO.query(new HDQuery[]{(HDQuery) entityManager -> {
            Query query = entityManager.createQuery(String.format("FROM staff_log_action"));
            resultList.addAll(query.getResultList());
        }});
        if (resultList != null && !resultList.isEmpty())
            return resultList;
        return null;
    }
}
