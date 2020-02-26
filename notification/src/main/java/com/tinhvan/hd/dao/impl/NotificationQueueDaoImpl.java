package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.dao.NotificationQueueDao;
import com.tinhvan.hd.entity.NotificationQueue;
import com.tinhvan.hd.payload.NotificationQueueDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Repository
public class NotificationQueueDaoImpl implements NotificationQueueDao {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public void insert(NotificationQueue queue) {
        DAO.query((entityManager) -> {
            entityManager.persist(queue);
        });
    }

    @Override
    public void update(NotificationQueue queue) {
        try {
            entityManager.merge(queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(NotificationQueue queue) {
        DAO.query((entityManager) -> {
            entityManager.remove(queue);
        });
    }

    @Override
    public List<NotificationQueue> findPushNotification() {
        List<NotificationQueue> ls = new ArrayList<>();
        try {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add("from NotificationQueue where status =:status0 or status = :status1");
            Query query = entityManager.createQuery(joiner.toString());
            query.setParameter("status0", NotificationQueue.STATUS.NEW);
            query.setParameter("status1", NotificationQueue.STATUS.FAIL);
            ls.addAll(query.getResultList());
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        return ls;
    }

    @Override
    @Transactional
    public void update(NotificationQueueDTO queueDTO) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("update NotificationQueue");
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

    @Override
    public boolean validNotification(UUID notificationUuid, int type, UUID customerId) {
        try {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add("select count(*) from NotificationQueue where (status !=:status0 or status != :status1) and type = :type");
            joiner.add("and (newsId = :notificationUuid or promotionId = :notificationUuid)");
            if (customerId != null)
                joiner.add("and customerId = :customerId");
            System.out.println(joiner.toString());
            Query query = entityManager.createQuery(joiner.toString());
            query.setParameter("status0", NotificationQueue.STATUS.NEW);
            query.setParameter("status1", NotificationQueue.STATUS.FAIL);
            query.setParameter("type", type);
            query.setParameter("notificationUuid", notificationUuid);
            if (customerId != null)
                query.setParameter("customerId", customerId);
            List<String> lst = new ArrayList<>();
            lst.addAll(query.getResultList());
            System.out.println(lst.toString());
            if (!lst.isEmpty() && Integer.parseInt(String.valueOf(lst.get(0))) > 0)
                return false;

        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        return true;
    }
}
