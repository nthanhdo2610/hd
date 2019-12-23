package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.*;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.Contract;
import com.tinhvan.hd.entity.ContractEditInfo;
import com.tinhvan.hd.entity.ContractRepayment;
import com.tinhvan.hd.service.ContractEditInfoService;
import com.tinhvan.hd.service.ContractRepaymentService;
import com.tinhvan.hd.service.ContractService;
import com.tinhvan.hd.service.HDMiddleService;
import com.tinhvan.hd.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/contract/scheduler")
public class SchedulerContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private HDMiddleService hdMiddleService;

    @Autowired
    private ContractRepaymentService contractRepaymentService;

    @Autowired
    private ContractEditInfoService contractEditInfoService;

    /**
     * Cron-task auto check Contract and update status
     */
    @Scheduled(cron = "0 0/3 * * * *")
    public void updateStatusContract() {
        try {

            List<PhoneAndStatus> phoneAndStatuses = new ArrayList<>();

            List<String> contractCodes = contractService.getAllContractCodesLive();

            if (contractCodes != null && contractCodes.size() > 0) {
                phoneAndStatuses = hdMiddleService.getPhoneAndStatusByContractCodes(contractCodes);
            }

            List<Contract> contracts = new ArrayList<>();
            if (phoneAndStatuses != null && phoneAndStatuses.size() > 0) {
                for (PhoneAndStatus status : phoneAndStatuses) {
                    if (status == null || status.getContractNumber() == null) {
                        continue;
                    }
                    Contract contract = contractService.getContractByContractCode(status.getContractNumber());
                    if (contract == null) {
                        continue;
                    }
                    contract.setStatus(status.getStatus());
                    contract.setLastSynchronizedDate(new Date());
                    contract.setMaturedDate(status.getMatureDate());
                    contract.setEarlyTerminationDate(status.getEarlyDate());
                    contract.setLastUpdateApplicant(status.getLastUpdatePhone());
                    if (contract.getDocumentVerificationDate() == null && status.getContractPrintingDate() != null) {
                        contract.setContractPrintingDateTemp(status.getContractPrintingDate());
                        contract.setContractPrintingDate(DateUtils.covertStringToDate(status.getContractPrintingDate()));
                    }

                    if (contract.getDocumentVerificationDate() == null && status.getDocumentVerificationDate() != null) {
                        contract.setDocumentVerificationDateTemp(status.getContractPrintingDate());
                        contract.setDocumentVerificationDate(DateUtils.covertStringToDate(status.getDocumentVerificationDate()));
                    }

                    contracts.add(contract);
                }
            }

            contractService.updateContractAll(contracts);
            //System.out.println("Update contract success");

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.system("Error[UpdateStatus|Contract]", ex.getMessage());
            throw new InternalServerErrorException();
        }
    }

    /**
     * Cron-task auto check contract repayment and update status
     */
    @Scheduled(cron = "0 0/10 * * * *")
    public void updateContractRepayment() {
        // update contract rePayment
        List<UpdateRepayment> updateRepayments = contractService.getContractCodeAndStatusUpdateRepayment();

        List<PaymentInfoRequest> paymentInfoRequests = new ArrayList<>();
        if (updateRepayments != null && updateRepayments.size() > 0) {
            for (UpdateRepayment item : updateRepayments) {
                PaymentInfoRequest paymentInfoRequest = new PaymentInfoRequest();
                paymentInfoRequest.setContractCode(item.getContractCode());
                paymentInfoRequest.setLatestPaymentDate(item.getPaidDateMax());
                paymentInfoRequests.add(paymentInfoRequest);
            }
        }

        List<PaymentInformation> listPayments = new ArrayList<>();
        if (paymentInfoRequests != null && paymentInfoRequests.size() > 0) {
            listPayments = hdMiddleService.getListPaymentInfoByContractCodeAndLatestPaymentDates(paymentInfoRequests);
        }

        List<ContractRepayment> contractRepayments = new ArrayList<>();
        List<Contract> contracts = new ArrayList<>();
        try {
            if (listPayments != null && listPayments.size() > 0) {

                for (PaymentInformation paymentInformation : listPayments) {
                    Contract contract = contractService.getContractByContractCode(paymentInformation.getContractCode());
                    if (contract != null) {
                        contract.setLastSyncRepaymentDate(new Date());
                        contracts.add(contract);
                    }
                    ContractRepayment contractRepayment = new ContractRepayment();
                    contractRepayment.setCreateTime(new Date());
                    contractRepayment.setPaidDate(paymentInformation.getMonthlyDueDate());
                    contractRepayment.setPayment(paymentInformation.getMonthlyInstallmentAmount());
                    contractRepayment.setContractCode(paymentInformation.getContractCode());
                    contractRepayment.setStatus(1);

                    contractRepayments.add(contractRepayment);

                }
            }

            if (contractRepayments != null && contractRepayments.size() > 0) {
                contractRepaymentService.saveAll(contractRepayments);
            }

            if (contracts != null && contracts.size() > 0) {
                contractService.updateContractAll(contracts);
            }
            //System.out.println("Update repayment success");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Cron-task auto check MonthlyDueDateAndChassisNo status
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void updateMonthlyDueDateAndChassisNo() {

        try {
            boolean isUpdateMonthly = true;
            boolean isUpdateChassisNo = true;

            List<ContractEditInfo> contractEditInfos = new ArrayList<>();
            List<ContractEditInfo> updateIsMonthlyDueDate = contractEditInfoService.getByIsUpdateMonthlyDueDate(0);

            // update monthlyDueDate
            if (updateIsMonthlyDueDate != null && updateIsMonthlyDueDate.size() > 0) {
                for (ContractEditInfo contractEditInfo : updateIsMonthlyDueDate) {

                    if (contractEditInfo.getMonthlyDueDate() == null) {
                        continue;
                    }

                    UpdateMonthlyDueDate monthlyDueDate = new UpdateMonthlyDueDate();
                    monthlyDueDate.setContractCode(contractEditInfo.getContractCode());
                    monthlyDueDate.setMonthlyDueDate(contractEditInfo.getMonthlyDueDate());
                    monthlyDueDate.setFirstDate(contractEditInfo.getFirstDate());
                    monthlyDueDate.setEndDate(contractEditInfo.getEndDate());

                    isUpdateMonthly = hdMiddleService.updateMonthlyDueDateContract(monthlyDueDate);

                    if (isUpdateMonthly) {
                        contractEditInfo.setIsUpdateMonthlyDueDate(1);
                        contractEditInfo.setUpdateMonthlyDueDateAt(new Date());
                    } else {
                        contractEditInfo.setIsUpdateMonthlyDueDate(0);
                        contractEditInfo.setUpdateMonthlyDueDateAt(new Date());
                    }
                    contractEditInfos.add(contractEditInfo);
                }
            }

            // update chassisNo and engineerNo
            List<ContractEditInfo> isUpdateChassisNoAnEngineerNo = contractEditInfoService.getByIsUpdateChassinoEnginerno(0);

            if (isUpdateChassisNoAnEngineerNo != null && isUpdateChassisNoAnEngineerNo.size() > 0) {
                for (ContractEditInfo item : isUpdateChassisNoAnEngineerNo) {

                    if (item.getChassisno() == null || item.getEnginerno() == null) {
                        continue;
                    }

                    UpdateChassisNoAndEnginerNo updateChassisNoAndEnginerNo = new UpdateChassisNoAndEnginerNo();
                    updateChassisNoAndEnginerNo.setContractCode(item.getContractCode());
                    updateChassisNoAndEnginerNo.setChassisNo(item.getChassisno());
                    updateChassisNoAndEnginerNo.setEngineerNo(item.getEnginerno());
                    isUpdateChassisNo = hdMiddleService.updateChassisNoAndEngineerNo(updateChassisNoAndEnginerNo);

                    if (isUpdateChassisNo) {
                        item.setIsUpdateChassinoEnginerno(1);
                        item.setUpdateChassinoEnginernoAt(new Date());
                    } else {
                        item.setIsUpdateChassinoEnginerno(0);
                        item.setUpdateChassinoEnginernoAt(new Date());
                    }
                    contractEditInfos.add(item);
                }
            }


            // update is isUpdateConprintToDocveri
            List<ContractEditInfo> isUpdateDocVeris = contractEditInfoService.getByIsUpdateConprintToDocveri(0);

            if (isUpdateDocVeris != null && isUpdateDocVeris.size() > 0) {
                for (ContractEditInfo editInfo : isUpdateDocVeris) {
                    boolean isUpdate = hdMiddleService.updateStatusAdjustmentInfo(editInfo.getContractCode());
                    if (isUpdate) {
                        editInfo.setIsUpdateAdjustment(1);
                        editInfo.setUpdateAdjustmentAt(new Date());
                    } else {
                        editInfo.setIsUpdateAdjustment(0);
                        editInfo.setUpdateAdjustmentAt(new Date());
                    }
                    contractEditInfos.add(editInfo);
                }
            }

            // update contract edit info
            if (contractEditInfos != null && contractEditInfos.size() > 0) {
                contractEditInfoService.saveOrUpdateAll(contractEditInfos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Cron-task auto check alert repayment
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void autoAlertRepayment() {

        try {
            contractService.autoAlertRepaymentNotification();
            //System.out.println("Call notification repayment success");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

//    @PostMapping("/test_kk")
//    public List<ObjectSendMailIT> test() {
//        List<ObjectSendMailIT> resultSearchs = contractService.getListAdjustmentInfoSendMailIt();
//        if (resultSearchs != null && resultSearchs.size() > 0) {
//
//            List<String> contractCodes = new ArrayList<>();
//            resultSearchs.stream().distinct().forEach(x -> {
//                contractCodes.add(x.getContractCode());
//            });
//            List<ContractInfo> contractInfos = new ArrayList<>();
//            if (contractCodes.size() > 0) {
//                List<ContractInfo> infoList = hdMiddleService.getContractDetailFromMidServers(contractCodes);
//                if (infoList != null && infoList.size() > 0) {
//                    contractInfos.addAll(infoList);
//                }
//            }
//            // set value old
//            for (ObjectSendMailIT mailIT : resultSearchs) {
//                for (ContractInfo info : contractInfos) {
//                    if (!HDUtil.isNullOrEmpty(mailIT.getValueOld())) {
//                        break;
//                    }
//                    if (!mailIT.getContractCode().equals(info.getContractNumber())) {
//                        continue;
//                    }
//
//                    List<ConfigCheckRecords> checkRecords = info.getConfigRecords();
//                    for (ConfigCheckRecords records : checkRecords) {
//                        if (!mailIT.getKey().equals(records.getKey())) {
//                            continue;
//                        }
//                        mailIT.setValueOld(records.getValue());
//                        break;
//                    }
//                }
//            }
//        }
//        return resultSearchs;
//    }
}