package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.dao.NotificationGroupDao;
import com.tinhvan.hd.entity.NotificationGroup;
import com.tinhvan.hd.vo.NotificationGroupFilterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class NotificationGroupDaoImpl implements NotificationGroupDao {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void saveNotificationGroup(NotificationGroup notificationGroup) {
//        DAO.query((em) -> {
//            em.persist(notificationGroup);
//        });
//    }
//
//    @Override
//    public void updateNotificationGroup(NotificationGroup notificationGroup) {
//        DAO.query((em) -> {
//            em.merge(notificationGroup);
//        });
//    }
//
//    @Override
//    public void deleteNotificationGroup(NotificationGroup notificationGroup) {
//        DAO.query((em) -> {
//            em.remove(notificationGroup);
//        });
//    }
//
//    @Override
//    public NotificationGroup getNotificationGroupById(Integer groupId) {
//        List<NotificationGroup> ls = new ArrayList<>();
//        DAO.query((em) -> {
//            ls.add(em.find(NotificationGroup.class,groupId));
//
//        });
//        return ls.get(0);
//    }

    @Override
    public List<NotificationGroup> getListNotificationGroupModifiedBy(NotificationGroupFilterVO groupFilterVO) {
        StringBuilder queryBuilder = new StringBuilder();
        List<NotificationGroup> ls = new ArrayList<>();
        queryBuilder.append(" from NotificationGroup where status = 1  ");

        generateFilterTemplate(groupFilterVO,queryBuilder);

//        DAO.query((em) -> {
//
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        setFilterTemplate(groupFilterVO,query);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public long countNotificationGroup(NotificationGroupFilterVO groupFilterVO) {
        long num = 0;
        List<Long> nums = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from NotificationGroup WHERE status = 1 ");
        generateFilterTemplate(groupFilterVO,queryBuilder);

//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        setFilterTemplate(groupFilterVO,query);
        nums.add((Long)query.getResultStream().findFirst().orElse(0));

        num = nums.get(0);

        return num;
    }


    public void generateFilterTemplate(NotificationGroupFilterVO groupFilterVO,StringBuilder stringBuilder){
        if (groupFilterVO != null){
            UUID modifiedBy = groupFilterVO.getModifiedBy();
            if (modifiedBy != null && !"".equals(modifiedBy.toString())){
                stringBuilder.append(" and modifiedBy= :modifiedBy ");
            }
        }
    }

    public void setFilterTemplate(NotificationGroupFilterVO groupFilterVO,Query query){
        if (groupFilterVO != null){
            UUID modifiedBy = groupFilterVO.getModifiedBy();
            if (modifiedBy != null && !"".equals(modifiedBy.toString())){
                query.setParameter("modifiedBy",modifiedBy);

            }
        }
    }

}

