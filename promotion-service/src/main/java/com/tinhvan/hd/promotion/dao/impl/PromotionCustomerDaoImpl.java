package com.tinhvan.hd.promotion.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.promotion.dao.PromotionCustomerDao;
import com.tinhvan.hd.promotion.entity.Promotion;
import com.tinhvan.hd.promotion.entity.PromotionCustomer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Repository
public class PromotionCustomerDaoImpl implements PromotionCustomerDao {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public void insert(PromotionCustomer PromotionCustomer) {
//        try{
//            entityManager.persist(PromotionCustomer);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void update(PromotionCustomer PromotionCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(PromotionCustomer);
//        });
//    }
//
//    @Override
//    public void delete(PromotionCustomer PromotionCustomer) {
//        DAO.query((entityManager) -> {
//            entityManager.remove(PromotionCustomer);
//        });
//    }

    @Override
    public PromotionCustomer findById(UUID id) {
        List<PromotionCustomer> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        ls.add(entityManager.find(PromotionCustomer.class, id));
        if (ls.size() > 0)
            return ls.get(0);
        return null;
    }

    @Override
    public List<PromotionCustomer> getListPromotionCustomerByCustomer(UUID customerUuid) {
        List<PromotionCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from PromotionCustomer where customerId = :customerId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("customerId", customerUuid);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<PromotionCustomer> getListPromotionCustomerByPromotionId(UUID promotionId) {
        List<PromotionCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from PromotionCustomer where promotionId = :promotionId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("promotionId", promotionId);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public int countListPromotionCustomerByPromotionId(UUID promotionId) {
        List<Integer> lst = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from PromotionCustomer where promotionId = :promotionId ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("promotionId", promotionId);
        lst.addAll(query.getResultList());
        if (!lst.isEmpty())
            return Integer.parseInt(String.valueOf(lst.get(0)));
        return 0;
    }

    @Override
    public PromotionCustomer find(UUID promotionId, UUID customerUuid) {
        List<PromotionCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from PromotionCustomer where promotionId = :promotionId and customerId = :customerUuid");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("promotionId", promotionId);
        query.setParameter("customerUuid", customerUuid);
        ls.addAll(query.getResultList());
        if (ls != null && ls.size() > 0)
            return ls.get(0);
        return null;
    }

    @Override
    public List<PromotionCustomer> findCustomerAndSendNotification() {
        List<PromotionCustomer> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from PromotionCustomer where status = 0");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setMaxResults(1000);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public int validateSendNotification(PromotionCustomer promotionCustomer) {
        List<Integer> lst = new ArrayList<>();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("select count(*) from PromotionCustomer");
        joiner.add("where promotionId = :promotionId");
        joiner.add("and customerId = :customerId");
        joiner.add("and statusNotification = :statusNotification");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("promotionId", promotionCustomer.getPromotionId());
        query.setParameter("customerId", promotionCustomer.getCustomerId());
        query.setParameter("statusNotification", Promotion.STATUS_NOTIFICATION.WAS_SEND);
        lst.addAll(query.getResultList());
        if (!lst.isEmpty())
            return Integer.parseInt(String.valueOf(lst.get(0)));
        return 0;
    }

    @Override
    public void updateByPromotion(Promotion promotion) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("update PromotionCustomer");
        joiner.add("set title = :title,");
        joiner.add("notificationContent = :notificationContent,");
        joiner.add("imagePath = :imagePath,");
        joiner.add("endDate = :endDate");
        joiner.add("where promotionId = :promotionId");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("title", promotion.getTitle());
        query.setParameter("notificationContent", promotion.getNotificationContent());
        query.setParameter("imagePath", promotion.getImagePath());
        query.setParameter("endDate", promotion.getEndDate());
        query.setParameter("promotionId", promotion.getId());
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void deleteByPromotionId(UUID promotionId) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("delete from PromotionCustomer");
        joiner.add("where promotionId = :promotionId");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("promotionId", promotionId);
        query.executeUpdate();
    }
}
