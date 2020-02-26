package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.dao.NotificationDao;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationQueue;
import com.tinhvan.hd.payload.NotificationQueueDTO;
import com.tinhvan.hd.payload.NotificationSearchRequest;
import com.tinhvan.hd.payload.ReadDetailNotificationRequest;
import com.tinhvan.hd.payload.UuidNotificationRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Notification> getListNotificationByIsSentAndCustomer(NotificationSearchRequest searchRequest) {
        StringJoiner joiner = new StringJoiner(" ");
        generateQueryString(joiner, searchRequest);
        //OderByAndSort(joiner, searchRequest.getOrderBy(), searchRequest.getDirection());
        List<Notification> lst = new ArrayList<>();

        Query query = entityManager.createQuery(joiner.toString());
        query.setFirstResult((searchRequest.getPageNum() - 1) * searchRequest.getPageSize());
        query.setMaxResults(searchRequest.getPageSize());
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public int countNotification(NotificationSearchRequest searchRequest) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("select count(*)");
        generateQueryString(joiner, searchRequest);
        List<String> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery(joiner.toString());
        lst.addAll(query.getResultList());
        if (!lst.isEmpty())
            return Integer.parseInt(String.valueOf(lst.get(0)));
        return 0;
    }

    @Override
    public List<Notification> getAll(NotificationSearchRequest notificationSearchRequest) {
        return null;
    }

    @Override
    public List<Notification> getListNotificationByCustomerUuidAndType(NotificationSearchRequest notificationSearchRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        if (notificationSearchRequest.getCustomerUuid() == null) {
            stringBuilder.append("From Notification no where no.customerUuid is null");
        } else {
            stringBuilder.append("select new Notification(no.id, na.isRead, no.sendTime, na.readTime, no.createdAt,");
            stringBuilder.append(" no.customerUuid, no.contractUuid, no.title, no.contentPara, no.content, no.newsId, no.promotionId, no.type, no.access)");
            stringBuilder.append(" From Notification no left join NotificationAction na on no.id = na.notificationId and na.customerUuid = :customerUuid");
            stringBuilder.append(" where (na.isDeleted is null or na.isDeleted <=0) ");
            stringBuilder.append(" and (no.customerUuid is null or no.customerUuid= :customerUuid)");
        }
        if (notificationSearchRequest.getType() != null && notificationSearchRequest.getType() > 0) {
            stringBuilder.append(" and no.type= :type ");
        }
        stringBuilder.append(" and no.status= :status ");
        stringBuilder.append(" and no.endDate > :now ");
        stringBuilder.append(" order by no.id desc");
        List<Notification> lst = new ArrayList<>();

        Query query = entityManager.createQuery(stringBuilder.toString());
        if (notificationSearchRequest.getCustomerUuid() != null) {
            query.setParameter("customerUuid", notificationSearchRequest.getCustomerUuid());
        }
        if (notificationSearchRequest.getType() != null && notificationSearchRequest.getType() > 0) {
            query.setParameter("type", notificationSearchRequest.getType());
        }
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        if (notificationSearchRequest.getPageSize() > 0) {
            query.setFirstResult((notificationSearchRequest.getPageNum() - 1) * notificationSearchRequest.getPageSize());
            query.setMaxResults(notificationSearchRequest.getPageSize());
        }
        query.setParameter("now", new Date());
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public List<Notification> getNotReadByCustomerUuid(UUID customerUuid) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" select new Notification(no.id, na.isRead, no.sendTime, na.readTime, no.createdAt,");
        stringBuilder.append(" no.customerUuid, no.contractUuid, no.title, no.contentPara, no.content, no.newsId, no.promotionId, no.type, no.access)");
        stringBuilder.append(" From Notification no left join NotificationAction na on no.id = na.notificationId and na.customerUuid = :customerUuid");
        stringBuilder.append(" where (na.isDeleted is null or na.isDeleted <=0) and (na.isRead is null or na.isRead = 0)");
        stringBuilder.append(" and (no.customerUuid is null or no.customerUuid= :customerUuid)");
        stringBuilder.append(" and no.status= :status ");
        stringBuilder.append(" and no.endDate > :now ");
        Query query = entityManager.createQuery(stringBuilder.toString());
        query.setParameter("customerUuid", customerUuid);
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        query.setParameter("now", new Date());
        List<Notification> list = query.getResultList();
        return list;
    }

    @Override
    public int countNotReadByCustomerUuid(UUID customerUuid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" select count(*)");
        stringBuilder.append(" From Notification no left join NotificationAction na on no.id = na.notificationId and na.customerUuid = :customerUuid");
        stringBuilder.append(" where (na.isDeleted is null or na.isDeleted <=0) and (na.isRead is null or na.isRead = 0)");
        stringBuilder.append(" and (no.customerUuid is null or no.customerUuid= :customerUuid)");
        stringBuilder.append(" and no.status= :status ");
        stringBuilder.append(" and no.endDate > :now ");
        Query query = entityManager.createQuery(stringBuilder.toString());
        query.setParameter("customerUuid", customerUuid);
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        query.setParameter("now", new Date());
        List<String> lst = query.getResultList();
        if (!lst.isEmpty())
            return Integer.parseInt(String.valueOf(lst.get(0)));
        return 0;
    }

    @Override
    public List<Notification> findByUuid(UuidNotificationRequest uuidNotificationRequest) {
        CharSequence separator;
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("From Notification where (1=2");
        if (uuidNotificationRequest.getNewsId() != null)
            joiner.add("or newsId = :newsId");
        if (uuidNotificationRequest.getPromotionId() != null)
            joiner.add("or promotionId = :promotionId");
        joiner.add(")");
        joiner.add("and status = :status");
        Query query = entityManager.createQuery(joiner.toString());
        if (uuidNotificationRequest.getNewsId() != null)
            query.setParameter("newsId", uuidNotificationRequest.getNewsId());
        if (uuidNotificationRequest.getPromotionId() != null)
            query.setParameter("promotionId", uuidNotificationRequest.getPromotionId());
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        List<Notification> list = query.getResultList();
        return list;
    }

    @Override
    @Transactional
    public void update(NotificationQueueDTO queueDTO) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("update Notification");
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
    public Notification findForReadDetail(ReadDetailNotificationRequest request) {
        CharSequence separator = " ";
        StringJoiner joiner = new StringJoiner(separator);
        joiner.add("From Notification where (isRead is null or isRead !=1)");
        joiner.add("and (newsId = :notificationUuid or promotionId = :notificationUuid)");
        joiner.add("and (customerUuid is null or customerUuid = :customerUuid)");
        joiner.add("and status = :status");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("notificationUuid", request.getNotificationUuid());
        query.setParameter("customerUuid", request.getCustomerUuid());
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        List<Notification> lst = query.getResultList();
        if (lst != null && lst.size() > 0)
            return lst.get(0);
        return null;
    }

    @Override
    public boolean validNotification(UUID notificationUuid, int type, UUID customerId) {
        try {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add("select count(*) from Notification where type = :type");
            joiner.add("and (newsId = :notificationUuid or promotionId = :notificationUuid)");
            if (customerId != null)
                joiner.add("and customerUuid = :customerId");
            System.out.println(joiner.toString());
            Query query = entityManager.createQuery(joiner.toString());
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

    /**
     * function used
     */
    void generateQueryString(StringJoiner joiner, NotificationSearchRequest searchRequest) {
        joiner.add("from Notification where status = " + HDConstant.STATUS.ENABLE);
        //joiner.add("and isSent = " + searchRequest.getIsSent());
        if (searchRequest.getType() > 0)
            joiner.add("and type = " + searchRequest.getType());
//        if (!HDUtil.isNullOrEmpty(searchRequest.getKeyWord())) {
//            joiner.add("and (1=2");
//            String[] key = searchRequest.getKeyWord().split(",");
//            for (int i = 0; i < key.length; i++) {
//                String s = key[i].trim().toUpperCase();
//                joiner.add("or upper(content) like '%" + s + "%'");
//            }
//            joiner.add(")");
//        }
    }

    void OderByAndSort(StringJoiner joiner, String oderBy, String direction) {

        String[] oder = oderBy.split(",");
        String[] direct = direction.split(",");

        joiner.add("order by");
        if (HDUtil.isNullOrEmpty(oderBy)) {
            joiner.add("createdAt desc");
        } else {
            for (int i = 0; i < oder.length; i++) {
                if (!HDUtil.isNullOrEmpty(oder[i])) {
                    joiner.add(oder[i]);
                }
                if (!HDUtil.isNullOrEmpty(direct[i])) {
                    joiner.add(direct[i]);
                }
                if (i < oder.length - 1)
                    joiner.add(",");
            }
        }
    }

}
