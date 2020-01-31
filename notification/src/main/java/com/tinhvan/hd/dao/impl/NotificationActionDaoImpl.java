package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.NotificationActionDao;
import com.tinhvan.hd.entity.NotificationAction;
import com.tinhvan.hd.payload.NotificationQueueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Repository
public class NotificationActionDaoImpl implements NotificationActionDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public NotificationAction find(UUID customerUuid, int notificationId) {
        Query query = entityManager.createQuery("from NotificationAction where customerUuid = :customerUuid and notificationId = :notificationId");
        query.setParameter("customerUuid", customerUuid);
        query.setParameter("notificationId", notificationId);
        List<NotificationAction> notificationActions = query.getResultList();
        if (notificationActions != null && notificationActions.size() > 0)
            return notificationActions.get(0);
        return null;
    }

    @Override
    @Transactional
    public void update(NotificationQueueDTO queueDTO) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("update NotificationAction");
        joiner.add("set title = :title,");
        joiner.add("content = :content,");
        joiner.add("type = :type,");
        joiner.add("endDate = :endDate");
        joiner.add("where 1=2");
        if (queueDTO.getNewsId() != null)
            joiner.add("or newsId = :newsId");
        if (queueDTO.getPromotionId() != null)
            joiner.add("or promotionId = :promotionId");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("title", queueDTO.getTitle());
        query.setParameter("content", queueDTO.getContent());
        query.setParameter("type", queueDTO.getType());
        query.setParameter("endDate", queueDTO.getEndDate());
        if (queueDTO.getNewsId() != null)
            query.setParameter("newsId", UUID.fromString(queueDTO.getNewsId()));
        if (queueDTO.getPromotionId() != null)
            query.setParameter("promotionId", UUID.fromString(queueDTO.getPromotionId()));
        query.executeUpdate();
    }
}
