package com.tinhvan.hd.dao;

import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.Contract;
import org.springframework.data.domain.Pageable;

import javax.swing.*;
import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
public interface ContractDao {

    List<Contract> getListContractByFilter(ContractFilter contractFilter);

    List<Contract> getListContractByContractCodeOrIdentifyId(CustomerFilter customerFilter);

    List<String> getAllContractCodesLive();

    List<ContractsByCustomerUuid> getListContractByCustomerUuid(UUID customerUuid);

    List<UpdateRepayment> getContractCodeAndStatusUpdateRepayment();

    List<AdjConfirmDto> getAdjConfirmByContractCode(String contractCode);

    List<ContractWaitingForSigning> getContractWaitingForSigning(String customerUuid);

    List<WaitingAdjustment> getContractAdjustmentInfoByCustomerUuid(String customerUuid);


    List<PopupNotification> getListContractDuePayment(UUID customerUuid);

    List<ContractByCustomer> getContractCodesByCustomer(UUID customerUuid);

    int countContractWaitingEsign();

     List<ObjectSendMailIT> getListAdjustmentInfoSendMailIt();

    void autoAlertRepaymentNotification();

    List<String> getContractByCustomerActive(String contractCode,PageSearch pageSearch);

}
