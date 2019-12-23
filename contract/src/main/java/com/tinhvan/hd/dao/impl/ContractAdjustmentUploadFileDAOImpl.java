package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.dao.ContractAdjustmentUploadFileDAO;
import com.tinhvan.hd.dto.AdjustmentUploadFileSearch;
import com.tinhvan.hd.entity.ContractAdjustmentUploadFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class ContractAdjustmentUploadFileDAOImpl implements ContractAdjustmentUploadFileDAO {

    @PersistenceContext
    private EntityManager entityManager;
//    @Override
//    public void create(ContractAdjustmentUploadFile adjustmentUploadFile) {
//        DAO.query((em) -> {
//            em.persist(adjustmentUploadFile);
//        });
//    }
//
//    @Override
//    public void update(ContractAdjustmentUploadFile adjustmentUploadFile) {
//        DAO.query((em) -> {
//            em.merge(adjustmentUploadFile);
//        });
//    }

    @Override
    public void delete(int id) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" update ContractAdjustmentUploadFile set sendMail= :status where id = :id");
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("status", HDConstant.STATUS.DELETE_FOREVER);
        query.setParameter("id", id);
        query.executeUpdate();

    }

    @Override
    public ContractAdjustmentUploadFile findById(int id) {
        List<ContractAdjustmentUploadFile> lst = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from ContractAdjustmentUploadFile where id = :id");
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("id", id);
        lst.addAll(query.getResultList());
        if (lst.size() > 0)
            return lst.get(0);
        return null;
    }

    @Override
    public List<ContractAdjustmentUploadFile> find(AdjustmentUploadFileSearch searchRequest) {
        StringJoiner joiner = new StringJoiner(" ");
        generateQueryString(joiner, searchRequest);
        OderByAndSort(joiner, searchRequest.getOrderBy(), searchRequest.getDirection());
        List<ContractAdjustmentUploadFile> lst = new ArrayList<>();
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(joiner.toString());
        query.setFirstResult((searchRequest.getPageNum() - 1) * searchRequest.getPageSize());
        query.setMaxResults(searchRequest.getPageSize());
        lst.addAll(query.getResultList());
        return lst;
    }

    @Override
    public int count(AdjustmentUploadFileSearch searchRequest) {
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
    public List<ContractAdjustmentUploadFile> findSendMail() {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("from ContractAdjustmentUploadFile where sendMail = 0");
        Query query = entityManager.createQuery(joiner.toString());
        List<ContractAdjustmentUploadFile> lst = query.getResultList();
        return lst;
    }





    /**
     * function used
     */
    void generateQueryString(StringJoiner joiner, AdjustmentUploadFileSearch searchRequest) {
        joiner.add("from ContractAdjustmentUploadFile where sendMail != " + HDConstant.STATUS.DELETE_FOREVER);
        if (searchRequest.getDateFrom() != null) {
            joiner.add("and createdAt >= '" + new java.sql.Timestamp(searchRequest.getDateFrom().getTime())+"'");
        }
        if (searchRequest.getDateTo() != null) {
            joiner.add("and createdAt <= '" + new java.sql.Timestamp(searchRequest.getDateTo().getTime())+"'");
        }
        if (!HDUtil.isNullOrEmpty(searchRequest.getKeyWord())) {
            joiner.add("and (1=2");
            String[] key = searchRequest.getKeyWord().split(",");
            for (int i = 0; i < key.length; i++) {
                String s = key[i].trim().toUpperCase();
                joiner.add("or upper(description) like '%" + s + "%'");
            }
            joiner.add(")");
        }
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
