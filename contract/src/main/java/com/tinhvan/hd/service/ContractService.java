package com.tinhvan.hd.service;

import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.Contract;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */

public interface ContractService {

    void createContract(Contract contract);

    void updateContract(Contract contract);

    void deleteContract(Contract contract);

    Contract getById(UUID id);

    Contract getContractByContractCode(String contractCode);


    List<Contract> getListContractByFilter(ContractFilter contractFilter);

    List<Contract> getListContractByIdentifyId(String identifyId);

    List<Contract> getListContractByContractCodeOrIdentifyId(CustomerFilter customerFilter);

    List<Contract> getContractByContractCodes(List<String> contractCodes);

    List<Contract> searchContractByContractCode(String contractCode,Pageable pageable);

    List<Contract> searchContractByContractCodeAndPageable(String contractCode,Pageable pageable);

    List<Contract> getListContractInfo(Pageable pageable);

    int countContract();

    int countContractByContractCode(String contractCode);


    List<Contract> getListContractByStatus(List<String> status);

    DashBoardContract getCountContractForDashBoard(List<String> waiting);

    List<String> getAllContractCodesLive();

    void updateContractByCustomerUuid(UUID customerUuid);

    void updateContractAll(List<Contract> contracts);

    List<ContractsByCustomerUuid> getListContractByCustomerUuid(UUID customerUuid);

    List<UpdateRepayment> getContractCodeAndStatusUpdateRepayment();

    List<AdjConfirmDto> getAdjConfirmByContractCode(String contractCode);

    List<ContractWaitingForSigning> getContractWaitingForSigning(String customerUuid);

    List<Contract> getContractListCurrentContract(List<String> status);

    List<WaitingAdjustment> getContractAdjustmentInfoByCustomerUuid(String customerUuid);

    List<PopupNotification> getListContractDuePayment(UUID customerUuid);

    List<ContractByCustomer> getContractCodesByCustomer(UUID customerUuid);

    int countContractWaitingEsign();

    List<ObjectSendMailIT> getListAdjustmentInfoSendMailIt();

    void autoAlertRepaymentNotification();

    List<String> getContractByCustomerActive(String contractCode,PageSearch pageSearch);
}
