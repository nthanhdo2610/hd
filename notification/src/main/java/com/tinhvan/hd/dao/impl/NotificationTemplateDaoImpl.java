package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.base.Log;
import com.tinhvan.hd.dao.NotificationTemplateDao;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.vo.NotificationTemplateFilterVO;
import com.tinhvan.hd.vo.NotificationTemplateList;
import com.tinhvan.hd.vo.NotificationTemplateListRespon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class NotificationTemplateDaoImpl  implements NotificationTemplateDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<NotificationTemplate> getAllNotificationTemplateIsActive(Integer isDelete) {
        List<NotificationTemplate> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationTemplate where isDeleted = :isDeleted ");
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("isDeleted", isDelete);
        ls.addAll(query.getResultList());

        return ls;
    }

//    @Override
//    public void saveNotificationTemplate(NotificationTemplate notificationTemplate) {
//        DAO.query((em) -> {
//            em.persist(notificationTemplate);
//        });
//    }
//
//    @Override
//    public void deleteNotificationTemplate(NotificationTemplate notificationTemplate) {
//        DAO.query((em) -> {
//            em.remove(notificationTemplate);
//        });
//    }
//
//    @Override
//    public NotificationTemplate getById(Integer id) {
//        List<NotificationTemplate> ls = new ArrayList<>();
//        DAO.query((em) -> {
//            ls.add(em.find(NotificationTemplate.class,id));
//
//        });
//        return ls.get(0);
//    }

    @Override
    public NotificationTemplate getByType(Integer type,String langcode) {
        List<NotificationTemplate> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationTemplate where type = :type and langcode= :langcode ");
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("type", type);
        query.setParameter("langcode",langcode);
        ls.addAll(query.getResultList());

        return ls.get(0);
    }

//    @Override
//    public NotificationTemplate createOrUpdate(NotificationTemplate notificationTemplate) {
//        List<NotificationTemplate> list = new ArrayList<>();
//        DAO.query((em) -> {
//            list.add(em.merge(notificationTemplate));
//        });
//        if(list != null)
//            return list.get(0);
//        return null;
//    }

    @Override
    public List<NotificationTemplate> getListNotificationTemplateModifiedBy(NotificationTemplateFilterVO templateFilterVO) {
        List<NotificationTemplate> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationTemplate where is_deleted = 0 ");

        generateFilterTemplate(templateFilterVO,queryBuilder);

//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        setFilterTemplate(templateFilterVO,query);
        ls.addAll(query.getResultList());


        return ls;
    }

    @Override
    public NotificationTemplateListRespon getList(NotificationTemplateList notificationTemplateList) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("FROM NotificationTemplate WHERE 1=1");
        String derection = notificationTemplateList.getDirection();
        int pageNum = notificationTemplateList.getPageNum();
        int pageSize = notificationTemplateList.getPageSize();
        if (!derection.equals("")) {
            if (derection.toUpperCase().equals("DESC")) {
                joiner.add("ORDER BY createdAt DESC");
            }else{
                joiner.add("ORDER BY createdAt ASC");
            }
        }

        List<NotificationTemplate> lst = new ArrayList<>();
        List<NotificationTemplateListRespon> list = new ArrayList<>();
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(joiner.toString());
        int count = query.getResultList().size();
        query.setFirstResult((pageNum - 1) * pageSize);
        query.setMaxResults(pageSize);
        lst.addAll(query.getResultList());
        list.add(new NotificationTemplateListRespon(lst, count));
        return list.get(0);
    }

    @Override
    public long countNotificationTemplateModifiedBy(NotificationTemplateFilterVO templateFilterVO) {

        List<Long> nums = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from NotificationTemplate WHERE isDeleted = 0 ");

        generateFilterTemplate(templateFilterVO,queryBuilder);

//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        setFilterTemplate(templateFilterVO,query);
        nums.add((Long)query.getResultStream().findFirst().orElse(0));

        return nums.get(0);
    }

    public void generateFilterTemplate(NotificationTemplateFilterVO templateFilterVO,StringBuilder stringBuilder){
        if (templateFilterVO != null){
            UUID modifiedBy = templateFilterVO.getModifiedBy();
            if (modifiedBy != null && !"".equals(modifiedBy.toString())){
                stringBuilder.append(" and modifiedBy= :modifiedBy ");
            }
        }
    }

    public void setFilterTemplate(NotificationTemplateFilterVO templateFilterVO,Query query){
        if (templateFilterVO != null){
            UUID modifiedBy = templateFilterVO.getModifiedBy();
            if (modifiedBy != null && !"".equals(modifiedBy.toString())){
                query.setParameter("modifiedBy",modifiedBy);

            }
        }
    }
}
