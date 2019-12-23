package com.tinhvan.hd.dao.impl;


import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.dao.NotificationGroupFilterDao;
import com.tinhvan.hd.entity.NotificationGroupFilter;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationGroupFilterDaoImpl implements NotificationGroupFilterDao {


    @Override
    public void saveNotificationGroupFilter(NotificationGroupFilter notificationGroupFilter) {
        DAO.query((em) -> {
            em.persist(notificationGroupFilter);
        });
    }

    @Override
    public List<NotificationGroupFilter> getListFilterByGroupId(Integer groupId) {
        List<NotificationGroupFilter> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationGroupFilter where notificationGroupId = :notificationGroupId ");
        DAO.query((em) -> {
            Query query = em.createQuery(queryBuilder.toString());
            query.setParameter("notificationGroupId", groupId);
            ls.addAll(query.getResultList());

        });

        return ls;
    }
}
