package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.dao.NotificationDao;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.payload.NotificationSearchRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

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
        stringBuilder.append(" order by no.id desc");
        List<Notification> lst = new ArrayList<>();

        Query query = entityManager.createQuery(stringBuilder.toString());
        if (notificationSearchRequest.getCustomerUuid() != null) {
            query.setParameter("customerUuid", notificationSearchRequest.getCustomerUuid());
        }
        if (notificationSearchRequest.getType() != null && notificationSearchRequest.getType() > 0) {
            query.setParameter("type", notificationSearchRequest.getType());
        }
        if (notificationSearchRequest.getPageSize() > 0) {
            query.setFirstResult((notificationSearchRequest.getPageNum() - 1) * notificationSearchRequest.getPageSize());
            query.setMaxResults(notificationSearchRequest.getPageSize());
        }
        lst.addAll(query.getResultList());
        return lst;
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
