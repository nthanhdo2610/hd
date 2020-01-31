package com.tinhvan.hd.dao.impl;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.dao.ContractDao;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.Contract;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Repository
public class ContractDaoImpl implements ContractDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Contract> getListContractByFilter(ContractFilter contractFilter) {
        List<Contract> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Contract where 1 = 1 ");
        generateFilterContract(contractFilter,queryBuilder);
        Query query = entityManager.createQuery(queryBuilder.toString());
        setFilterContract(contractFilter,query);
        ls = query.getResultList();
        return ls;
    }



    @Override
    public List<Contract> getListContractByContractCodeOrIdentifyId(CustomerFilter customerFilter) {
        List<Contract> ls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Contract where 1 = 1 ");
        generateCustomerFilterContract(customerFilter,queryBuilder);
        Query query = entityManager.createQuery(queryBuilder.toString());
        setCustomerFilterContract(customerFilter,query);
        ls = query.getResultList();
        return ls;
    }

    @Override
    public List<String> getAllContractCodesLive() {
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("p_contract_list_for_synchronize");

        // Set the parameters of the stored procedure.
        List<String>  list = new ArrayList<>();
        try {
            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            list= storedProcedureResults.stream().map(result -> new String(
                    (String) result[0]
            )).collect(Collectors.toList());
            // Use Java 8's cool new functional programming paradigm to map the objects from the stored procedure results

        }catch (Exception e) {
            e.printStackTrace();
        }
        // Call the stored procedure.
        return list;
    }

    @Override
    public List<ContractsByCustomerUuid> getListContractByCustomerUuid(UUID customerUuid) {
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("p_contract_list_f_customer_uuid");

        // Set the parameters of the stored procedure.
        List<ContractsByCustomerUuid>  list = new ArrayList<>();
        try {

            storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            storedProcedure.setParameter(1, customerUuid.toString());

            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            list= storedProcedureResults.stream().map(result -> new ContractsByCustomerUuid(
                    (String) result[0],
                    (String) result[1],
                    (String) result[2],
                    (Short) result[3]
            )).collect(Collectors.toList());
            // Use Java 8's cool new functional programming paradigm to map the objects from the stored procedure results

        }catch (Exception e) {
            e.printStackTrace();
        }
        // Call the stored procedure.
        return list;
    }

    @Override
    public List<UpdateRepayment> getContractCodeAndStatusUpdateRepayment() {
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("p_contract_list_for_sync_repayment");

        // Set the parameters of the stored procedure.
        List<UpdateRepayment>  list = new ArrayList<>();
        try {

            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            list= storedProcedureResults.stream().map(result -> new UpdateRepayment(
                    (String) result[0],
                    (String) result[1],
                    (Date) result[2],
                    (Date) result[3],
                    (Double) result[4]
            )).collect(Collectors.toList());
            // Use Java 8's cool new functional programming paradigm to map the objects from the stored procedure results

        }catch (Exception e) {
            e.printStackTrace();
        }
        // Call the stored procedure.
        return list;
    }




    public List<AdjConfirmDto> getAdjConfirmByContractCode(String contractCode){
        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_contract_adjconfirm_f_contractcode")
                    .registerStoredProcedureParameter(
                            "contractCode",
                            String.class,
                            ParameterMode.IN
                    )
                    .setParameter("contractCode", contractCode);
            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            List<AdjConfirmDto> lst = storedProcedureResults.stream().map(result -> new AdjConfirmDto(
                    (String) result[0],
                    (String) result[1],
                    (String) result[2]
            )).collect(Collectors.toList());
            return lst;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ContractWaitingForSigning> getContractWaitingForSigning(String customerUuid) {

        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_contract_waiting_esign_list");

            storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            storedProcedure.setParameter(1, customerUuid);

            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            List<ContractWaitingForSigning> lst = storedProcedureResults.stream().map(result -> new ContractWaitingForSigning(
                    (String) result[0],
                    (String) result[1],
                    (String) result[2],
                    (Date) result[3]
            )).collect(Collectors.toList());


            return lst;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<WaitingAdjustment> getContractAdjustmentInfoByCustomerUuid(String customerUuid) {
        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_contract_waiting_adjustment_list");

            storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            storedProcedure.setParameter(1, customerUuid);

            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            List<WaitingAdjustment> lst = storedProcedureResults.stream().map(result -> new WaitingAdjustment(
                    (String) result[0],
                    (String) result[1]
            )).collect(Collectors.toList());

            return lst;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<PopupNotification> getListContractDuePayment(UUID customerUuid) {

        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_contract_alert_repayment");

            storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            storedProcedure.setParameter(1, customerUuid.toString());

            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            List<PopupNotification> lst = storedProcedureResults.stream().map(result -> new PopupNotification(
                    String.valueOf(result[0]),
                    String.valueOf(result[1]),
                    (Date) result[2],
                    String.valueOf(result[3]),
                    (BigDecimal) result[4],
                    (BigDecimal) result[5],
                    (Date) result[6],
                    (Date) result[7]
            )).collect(Collectors.toList());

            return lst;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ContractByCustomer> getContractCodesByCustomer(UUID customerUuid) {
        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_contract_list_codeandstatus");

            storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            storedProcedure.setParameter(1, customerUuid.toString());

            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            List<ContractByCustomer> lst = storedProcedureResults.stream().map(result -> new ContractByCustomer(
                    (String) result[0],
                    (String) result[1],
                    (String) result[2],
                    (String) result[3]
            )).collect(Collectors.toList());

            return lst;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countContractWaitingEsign() {
        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_count_contract_waiting_esign");

            storedProcedure.registerStoredProcedureParameter(1, Long.class, ParameterMode.OUT);


            storedProcedure.execute();

            Long commentCount = (Long) storedProcedure.getOutputParameterValue(1);
            if (commentCount != null) {
                return commentCount.intValue();
            }

        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    @Override
    public List<ObjectSendMailIT> getListAdjustmentInfoSendMailIt() {
        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_contract_adj_send_mail");

            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            List<ObjectSendMailIT> lst = storedProcedureResults.stream().map(result -> new ObjectSendMailIT(
                    (String) result[0],
                    (String) result[1],
                    (String) result[2],
                    (String) result[3],
                    (String) result[4],
                    (Date) result[5]
            )).collect(Collectors.toList());

            return lst;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void autoAlertRepaymentNotification() {
        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_auto_alert_repayment");

            storedProcedure.execute();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getContractByCustomerActive(String contractCode,PageSearch pageSearch) {
        List<String> list = new ArrayList<>();
        int page = pageSearch.getPage() - 1;
        int pageSize = pageSearch.getPageSize();
        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_search_contract_by_limit_offset");

            storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
            storedProcedure.setParameter(1,contractCode);
            storedProcedure.setParameter(2, pageSize);
            storedProcedure.setParameter(3, page);
            List<Object[]> storedProcedureResults = storedProcedure.getResultList();

            list = storedProcedureResults.stream().map(result -> new String(
                    (String) result[0]
            )).collect(Collectors.toList());

        }catch (Exception e){
            e.printStackTrace();

        }
        return list;
    }

    @Override
    public int countContractByCustomerActive(String contractCode) {
        int total = 0;
        try {
            StoredProcedureQuery storedProcedure = entityManager
                    .createStoredProcedureQuery("public.p_count_contract_by_customer_active");

            storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            storedProcedure.setParameter(1,contractCode);

            storedProcedure.registerStoredProcedureParameter(2, Long.class, ParameterMode.OUT);

            storedProcedure.execute();

            Long countContract = (Long) storedProcedure.getOutputParameterValue(2);
            if (countContract != null) {
                total = countContract.intValue();
            }

        }catch (Exception e){
            e.printStackTrace();

        }
        return total;
    }

    public void generateFilterContract(ContractFilter contractFilter, StringBuilder stringBuilder){
        if (contractFilter != null){
            UUID customerUuid = contractFilter.getCustomerUuid();
            Integer status = contractFilter.getStatus();

            if (customerUuid != null && !"".equals(customerUuid.toString())){
                stringBuilder.append(" and customerUuid= :customerUuid ");
            }

            if(status != null){
                stringBuilder.append(" and status= :status ");
            }
        }
    }

    public void setFilterContract(ContractFilter contractFilter, Query query){
        if (contractFilter != null){
            UUID customerUuid = contractFilter.getCustomerUuid();
            Integer status = contractFilter.getStatus();
            if (customerUuid != null && !"".equals(customerUuid.toString())){
                query.setParameter("customerUuid",customerUuid);
            }

            if(status != null){
                query.setParameter("status",status);
            }
        }
    }

    public void generateCustomerFilterContract(CustomerFilter customerFilter, StringBuilder stringBuilder){
        if (customerFilter != null){
            String contractCode = customerFilter.getContractCode();
            String identifyId = customerFilter.getIdentityNumber();

            if (!HDUtil.isNullOrEmpty(contractCode)){
                stringBuilder.append(" and lendingCoreContractId %:lendingCoreContractId%");
            }

            if(!HDUtil.isNullOrEmpty(identifyId)){
                stringBuilder.append(" and status= :status ");
            }
        }
    }

    public void setCustomerFilterContract(CustomerFilter customerFilter, Query query){
        if (customerFilter != null){
            String contractCode = customerFilter.getContractCode();
            String identifyId = customerFilter.getIdentityNumber();
            if (!HDUtil.isNullOrEmpty(contractCode)){
                query.setParameter("lendingCoreContractId",contractCode);
            }
        }
    }
}
