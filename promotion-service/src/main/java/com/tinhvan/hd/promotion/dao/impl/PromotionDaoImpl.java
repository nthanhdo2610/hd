package com.tinhvan.hd.promotion.dao.impl;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.promotion.dao.PromotionDao;
import com.tinhvan.hd.promotion.entity.Promotion;
import com.tinhvan.hd.promotion.payload.MenuRequest;
import com.tinhvan.hd.promotion.payload.PromotionSearchRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Repository
public class PromotionDaoImpl implements PromotionDao {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public void postPromotion(Promotion Promotion) {
//        DAO.query((entityManager) -> {
//            entityManager.persist(Promotion);
//        });
//    }
//
//    @Override
//    public void updatePromotion(Promotion Promotion) {
//        try {
//            entityManager.merge(Promotion);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public Promotion findById(UUID id) {
        List<Promotion> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        ls.add(entityManager.find(Promotion.class, id));
        if (ls.size() > 0)
            return ls.get(0);
        return null;
    }

    @Override
    public List<Promotion> getListPromotionByStatus(Integer status) {
        List<Promotion> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Promotion where status = :status ");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("status", status);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<Promotion> getListFeatured(String type) {
        List<Promotion> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("from Promotion where status = :status and isFeatured = 1");
        if (!HDUtil.isNullOrEmpty(type)) {
            /*if (type.toUpperCase().equals("CL"))
                joiner.add("and (type = 'CL' or type = 'CLO')");
            else*/
            joiner.add("and type = :type");
        }
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        if (!HDUtil.isNullOrEmpty(type)) {
            query.setParameter("type", type);
        }
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<Promotion> findSendNotification() {
        List<Promotion> ls = new ArrayList<>();
        try {
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add("from Promotion where status = :status and endDate >= :now and startDate <= :now and statusNotification != :statusNotification");
            Query query = entityManager.createQuery(joiner.toString());
            query.setParameter("status", HDConstant.STATUS.ENABLE);
            query.setParameter("now", new Date());
            query.setParameter("statusNotification", Promotion.STATUS_NOTIFICATION.WAS_SEND);
            ls.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }

    @Override
    public List<Promotion> find(PromotionSearchRequest searchRequest) {
        StringJoiner joiner = new StringJoiner(" ");
        listQueryString(joiner, searchRequest);
        OderByAndSort(joiner, searchRequest.getOrderBy(), searchRequest.getDirection());
        List<Promotion> lst = new ArrayList<>();
        Query query = entityManager.createQuery(joiner.toString());
        query.setFirstResult((searchRequest.getPageNum() - 1) * searchRequest.getPageSize());
        query.setMaxResults(searchRequest.getPageSize());
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public int count(PromotionSearchRequest searchRequest) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("select count(*)");
        listQueryString(joiner, searchRequest);
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
    public List<Promotion> findIndividual(UUID customerUuid) {
        List<Promotion> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("from Promotion where status = :status and access = :access and id in( select promotionId from PromotionCustomer where customerId = :customerUuid)");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        query.setParameter("access", Promotion.ACCESS.INDIVIDUAL);
        query.setParameter("customerUuid", customerUuid);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<Promotion> findGeneral() {
        List<Promotion> ls = new ArrayList<>();
//        DAO.query((entityManager) -> {
//
//        });
        StringJoiner joiner = new StringJoiner(" ");
        //joiner.add("from Promotion where status = :status and access = :access and id not in(select distinct promotionId from PromotionCustomer)");
        joiner.add("from Promotion where status = :status and access = :access");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        query.setParameter("access", Promotion.ACCESS.GENERAL);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<Promotion> findHome(int limit) {
        List<Promotion> ls = new ArrayList<>();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("from Promotion where status = :status and access = :access");
        joiner.add("and startDate <= :todayPM");
        joiner.add("and endDate >= :todayAM");
        joiner.add("order by isFeatured desc, startDate desc, endDate asc, createdAt desc");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        query.setParameter("access", Promotion.ACCESS.GENERAL);
        query.setParameter("todayPM", new Date());//Timestamp.valueOf(LocalDate.now().atTime(23, 59, 59, 999)));
        query.setParameter("todayAM", new Date());//Timestamp.valueOf(LocalDate.now().atTime(00, 00, 00, 000)));
        if (limit > 0)
            query.setMaxResults(limit);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<Promotion> findHomeLogged(UUID customerUuid, int access, int limit) {

        List<Promotion> ls = new ArrayList<>();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("from Promotion where status = :status");
        joiner.add("and startDate <= :todayPM");
        joiner.add("and endDate >= :todayAM");
        if (access == 0)
            joiner.add("and (access = :access or id in( select promotionId from PromotionCustomer where customerId = :customerUuid))");
        if (access == Promotion.ACCESS.GENERAL)
            joiner.add("and access = :access");
        if (access == Promotion.ACCESS.INDIVIDUAL)
            joiner.add("and access = :access and id in( select promotionId from PromotionCustomer where customerId = :customerUuid)");
        joiner.add("order by isFeatured desc, startDate desc, endDate asc, createdAt desc");
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("status", HDConstant.STATUS.ENABLE);
        query.setParameter("todayPM", new Date());//Timestamp.valueOf(LocalDate.now().atTime(23, 59, 59, 999)));
        query.setParameter("todayAM", new Date());//Timestamp.valueOf(LocalDate.now().atTime(00, 00, 00, 000)));
        if (access == 0) {
            query.setParameter("access", Promotion.ACCESS.GENERAL);
            query.setParameter("customerUuid", customerUuid);
        }
        if (access == Promotion.ACCESS.GENERAL) {
            query.setParameter("access", Promotion.ACCESS.GENERAL);
        }
        if (access == Promotion.ACCESS.INDIVIDUAL) {
            query.setParameter("access", Promotion.ACCESS.INDIVIDUAL);
            query.setParameter("customerUuid", customerUuid);
        }
        if (limit > 0)
            query.setMaxResults(limit);
        ls.addAll(query.getResultList());
        return ls;
    }

    @Override
    public List<Promotion> findMenu(MenuRequest menuRequest) {
        StringJoiner joiner = new StringJoiner(" ");
        menuQueryString(joiner, menuRequest);
        OderByAndSort(joiner, menuRequest.getOrderBy(), menuRequest.getDirection());
        List<Promotion> lst = new ArrayList<>();
        Query query = entityManager.createQuery(joiner.toString());
        query.setFirstResult((menuRequest.getPageNum() - 1) * menuRequest.getPageSize());
        query.setMaxResults(menuRequest.getPageSize());
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public int countMenu(MenuRequest menuRequest) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("select count(*)");
        menuQueryString(joiner, menuRequest);
        List<String> lst = new ArrayList<>();
        Query query = entityManager.createQuery(joiner.toString());
        lst.addAll(query.getResultList());
        if (!lst.isEmpty())
            return Integer.parseInt(String.valueOf(lst.get(0)));
        return 0;
    }

    @Override
    public List<Promotion> findResizeImage() {
        List<Promotion> lst = new ArrayList<>();
        CharSequence separator = " ";
        LocalDateTime current = LocalDateTime.now();
        StringJoiner joiner = new StringJoiner(separator);
        joiner.add("From Promotion where (imagePath is not null and imagePathApp is null and endDate >= '" + Timestamp.valueOf(current) + "')");
        joiner.add("or (imagePathBriefApp is not null and imagePathBriefApp is null and endDate >= '" + Timestamp.valueOf(current) + "')");
        Query query = entityManager.createQuery(joiner.toString());
        lst.addAll(query.getResultList());
        return lst;
    }

    /**
     * function used
     */
    void listQueryString(StringJoiner joiner, PromotionSearchRequest searchRequest) {
        joiner.add("from Promotion where status != " + HDConstant.STATUS.DELETE_FOREVER);
        if (!HDUtil.isNullOrEmpty(searchRequest.getType())) {
            /*if (searchRequest.getType().toUpperCase().equals("CL"))
                joiner.add("and (type = 'CL' or type = 'CLO')");
            else*/
            joiner.add("and type = '" + searchRequest.getType() + "'");
        }
        if (searchRequest.getAccess() > 0) {
            joiner.add("and access = " + searchRequest.getAccess());
        }
        if (searchRequest.getDateFrom() != null || searchRequest.getDateTo() != null) {
            StringJoiner j1 = new StringJoiner(" ");
            StringJoiner j2 = new StringJoiner(" ");
            j1.add("(1=1");
            j2.add("(1=1");
            if (searchRequest.getDateFrom() != null) {
                LocalDateTime dateFrom = Instant.ofEpochMilli(searchRequest.getDateFrom().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .atTime(00, 00, 00, 000);
                j1.add("and startDate >= '" + Timestamp.valueOf(dateFrom) + "'");
                //j2.add("and endDate >= '" + Timestamp.valueOf(dateFrom) + "'");
            }
            if (searchRequest.getDateTo() != null) {
                LocalDateTime dateTo = Instant.ofEpochMilli(searchRequest.getDateTo().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .atTime(23, 59, 59, 999);
                //j1.add("and startDate <= '" + Timestamp.valueOf(dateTo) + "'");
                j2.add("and endDate <= '" + Timestamp.valueOf(dateTo) + "'");
            }
            j1.add(")");
            j2.add(")");

            joiner.add("and (");
            joiner.add(j1.toString());
            //joiner.add("or");
            joiner.add("and");
            joiner.add(j2.toString());
            joiner.add(")");
        }
        if (!HDUtil.isNullOrEmpty(searchRequest.getKeyWord())) {
            joiner.add("and (1=2");
            String[] key = searchRequest.getKeyWord().split(",");
            for (int i = 0; i < key.length; i++) {
                String s = key[i].trim().toUpperCase();
                joiner.add("or upper(title) like '%" + s + "%'");
            }
            joiner.add(")");
        }
    }

    void menuQueryString(StringJoiner joiner, MenuRequest menuRequest) {
        joiner.add("from Promotion where status = " + HDConstant.STATUS.ENABLE);
        if (!HDUtil.isNullOrEmpty(menuRequest.getType())) {
            /*if (menuRequest.getType().toUpperCase().equals("CL"))
                joiner.add("and (type = 'CL' or type = 'CLO')");
            else*/
            joiner.add("and type = '" + menuRequest.getType() + "'");
        }
        if (menuRequest.getAccess() == 0) {
            joiner.add("and (access = " + Promotion.ACCESS.GENERAL + " or id in( select promotionId from PromotionCustomer where customerId = '" + menuRequest.getCustomerUuid() + "'))");
        }
        if (menuRequest.getAccess() == Promotion.ACCESS.GENERAL) {
            joiner.add("and access = " + Promotion.ACCESS.GENERAL);
        }
        if (menuRequest.getAccess() == Promotion.ACCESS.INDIVIDUAL) {
            joiner.add("and access = " + Promotion.ACCESS.INDIVIDUAL + " and id in( select promotionId from PromotionCustomer where customerId = '" + menuRequest.getCustomerUuid() + "')");
        }

        if (!HDUtil.isNullOrEmpty(menuRequest.getKeyWord())) {
            joiner.add("and (1=2");
            String[] key = menuRequest.getKeyWord().split(",");
            for (int i = 0; i < key.length; i++) {
                String s = key[i].trim().toUpperCase();
                joiner.add("or upper(title) like '%" + s + "%'");
            }
            joiner.add(")");
        }
    }

    void OderByAndSort(StringJoiner joiner, String oderBy, String direction) {

        String[] oder = oderBy.split(",");
        String[] direct = direction.split(",");

        joiner.add("order by");
        if (HDUtil.isNullOrEmpty(oderBy)) {
            joiner.add("isFeatured desc, startDate desc, endDate asc, createdAt desc");
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
