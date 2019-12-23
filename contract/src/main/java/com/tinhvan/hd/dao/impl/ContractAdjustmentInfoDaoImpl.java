package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.dao.ContractAdjustmentInfoDao;
import com.tinhvan.hd.dto.AdjustmentInfoMapper;
import com.tinhvan.hd.dto.ContractAdjustmentDetail;
import com.tinhvan.hd.dto.ContractsByCustomerUuid;
import com.tinhvan.hd.dto.PageSearch;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ContractAdjustmentInfoDaoImpl implements ContractAdjustmentInfoDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<AdjustmentInfoMapper> getListContractAdjustmentInfo(String contractCode, Integer isConfirm, PageSearch pageSearch) {

        List<AdjustmentInfoMapper> list = new ArrayList<>();
        int page = pageSearch.getPage();
        int pageSize = pageSearch.getPageSize();
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT contractCode,max(createdAt) as createdAt FROM ContractAdjustmentInfo  WHERE 1 = 1 ");
            generateFilterContractAdjustmentInfo(contractCode,isConfirm,queryBuilder);

            Query query = entityManager.createQuery(queryBuilder.toString());

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            setFilterContractAdjustmentInfo(contractCode,isConfirm,query);
            List<Object[]> storedProcedureResults = query.getResultList();
            List<AdjustmentInfoMapper> ls = storedProcedureResults.stream().map(result -> new AdjustmentInfoMapper(
                    (String) result[0],
                    (Date) result[1]
            )).collect(Collectors.toList());
            list.addAll(ls);


        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ContractAdjustmentDetail> getListContractAdjustmentInfoByContractCodeMobile(String contractCode) {
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("p_contract_adjconfirm_f_contractcode");

        // Set the parameters of the stored procedure.
        List<ContractAdjustmentDetail>  list = new ArrayList<>();
        try {

            storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            storedProcedure.setParameter(1, contractCode);

            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            list= storedProcedureResults.stream().map(result -> new ContractAdjustmentDetail(
                    (String) result[0],
                    (String) result[1],
                    (String) result[2],
                    (String) result[3],
                    (String) result[4]
            )).collect(Collectors.toList());
            // Use Java 8's cool new functional programming paradigm to map the objects from the stored procedure results

        }catch (Exception e) {
            e.printStackTrace();
        }
        // Call the stored procedure.
        return list;
    }

    public void generateFilterContractAdjustmentInfo(String contractCode,Integer isConfirm, StringBuilder stringBuilder){

        if (!HDUtil.isNullOrEmpty(contractCode)){
            stringBuilder.append(" and contractCode LIKE :contractCode");
        }

        if(isConfirm != null && isConfirm >=0 ){
            stringBuilder.append(" and isConfirm= :isConfirm ");
        }

        stringBuilder.append(" GROUP BY contractCode ");
        stringBuilder.append(" order by createdAt DESC ");
    }

    public void setFilterContractAdjustmentInfo(String contractCode,Integer isConfirm, Query query){

        if (!HDUtil.isNullOrEmpty(contractCode)){
            query.setParameter("contractCode","%" + contractCode +"%");
        }

        if(isConfirm != null && isConfirm >= 0){
            query.setParameter("isConfirm",isConfirm);
        }
    }


}
