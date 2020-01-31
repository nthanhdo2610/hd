package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.dao.ContractCustomerDao;
import com.tinhvan.hd.dto.AdjustmentInfoMapper;
import com.tinhvan.hd.dto.Uuids;
import com.tinhvan.hd.entity.ContractCustomer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ContractCustomerDaoImpl implements ContractCustomerDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<String> getCustomerUuidsByContractCode(String contractCode) {
        List<String> customerUuids = new ArrayList<>();
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT customerUuid from ContractCustomer  WHERE contractCode = :contractCode GROUP BY customerUuid");

            Query query = entityManager.createQuery(queryBuilder.toString());
            query.setParameter("contractCode",contractCode);
            List<Object[]> storedProcedureResults = query.getResultList();
            for (Object[] result : storedProcedureResults) {
                customerUuids.add(((UUID)result[0]).toString());
            }
            return customerUuids;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
