package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.NotificationTemplateDao;
import com.tinhvan.hd.entity.NotificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository

public class NotificationTemplateDaoImpl  implements NotificationTemplateDao {

    @Autowired
    EntityManager entityManager;

    public NotificationTemplateDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    NamedParameterJdbcTemplate template;

    @Override
    public List<NotificationTemplate> getAllNotificationTemplateIsActive(Integer isDelete) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationTemplate where isDeleted = :isDeleted ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("isDeleted", isDelete);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void saveNotificationTemplate(NotificationTemplate notificationTemplate) {
        entityManager.persist(notificationTemplate);
    }

    @Override
    @Transactional
    public void deleteNotificationTemplate(NotificationTemplate notificationTemplate) {
        entityManager.remove(notificationTemplate);
    }

    @Override
    public NotificationTemplate getById(Integer id) {
        return entityManager.find(NotificationTemplate.class,id);
    }

    @Override
    public NotificationTemplate getByType(Integer type,String langcode) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationTemplate where type = :type and langcode= :langcode ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("type", type);
        query.setParameter("langcode",langcode);
        return (NotificationTemplate) query.getResultStream().findFirst().orElse(null);
    }

    @Override
    @Transactional
    public void updateNotificationTemplate(NotificationTemplate notificationTemplate) {
        entityManager.merge(notificationTemplate);
    }

    @Override
    public List<NotificationTemplate> getListNotificationTemplateModifiedBy(UUID modifiedBy) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from NotificationTemplate where modifiedBy = :modifiedBy ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("modifiedBy", modifiedBy);
        return query.getResultList();
    }
}
