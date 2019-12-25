package com.tinhvan.hd.listener;

import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.dto.ContractInfo;
import com.tinhvan.hd.dto.ContractsByCustomerUuid;
import com.tinhvan.hd.dto.HDContractResponse;
import com.tinhvan.hd.dto.PhoneAndStatus;
import com.tinhvan.hd.entity.Contract;
import com.tinhvan.hd.entity.ContractCustomer;
import com.tinhvan.hd.entity.LogUpdateContractWhenLogin;
import com.tinhvan.hd.service.ContractCustomerService;
import com.tinhvan.hd.service.ContractService;
import com.tinhvan.hd.service.HDMiddleService;
import com.tinhvan.hd.service.LogUpdateContractService;
import com.tinhvan.hd.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ContractListener {

    @Autowired
    private ContractService contractService;

    @Autowired
    private HDMiddleService hdMiddleService;

    @Autowired
    private ContractCustomerService contractCustomerService;

    @Autowired
    private LogUpdateContractService logUpdateContractService;

    @RabbitListener(queues = RabbitConfig.QUEUE_UPDATE_CONTRACT_FROM_MIDDLE)
    public void updateContractByCustomer(UUID customerUuid) {
        String contractCodeFm = "";
        try {
            List<ContractsByCustomerUuid> lstContract = contractService.getListContractByCustomerUuid(customerUuid);

            if (lstContract != null && lstContract.size() > 0) {
                List<String> contractCodes = new ArrayList<>();
                for (ContractsByCustomerUuid item : lstContract) {
                    if (item.getContractCustomerStatus() == null || item.getContractCustomerStatus() == 0) {
                        continue;
                    }
                    contractCodes.add(item.getContractCode());
                }

                ContractInfo contractInfo = null;
                List<ContractInfo> contractInfos = hdMiddleService.getContractDetailFromMidServers(contractCodes);
                List<String> identifyIds = new ArrayList<>();
                List<String> phones = new ArrayList<>();
                if (contractInfos != null && contractInfos.size() > 0) {
                    for (ContractInfo info : contractInfos) {
                        if (contractInfo == null) {
                            contractInfo = info;
                        }
                        if (identifyIds.isEmpty() || !identifyIds.contains(info.getNationalID())){
                            identifyIds.add(info.getNationalID());
                        }

                        if (phones.isEmpty() || !phones.contains(info.getPhoneNumber())) {
                            phones.add(info.getPhoneNumber());
                        }
                    }
                }

                List<HDContractResponse> contractResponseInserts = new ArrayList<>();
                // get contract by identifyIds
                if (identifyIds.size() > 0) {
                    for (String identifyId : identifyIds) {
                        List<HDContractResponse> contractResponses = hdMiddleService.getListContractByIdentifyId(identifyId);
                        if (contractResponses == null) {
                            continue;
                        }

                        for (HDContractResponse response : contractResponses) {
                            if (!contractCodes.contains(response.getContractNumber())) {
                                response.setPhoneRequest(false);
                                contractResponseInserts.add(response);
                                contractCodes.add(response.getContractNumber());
                            }

                            if (!phones.contains(response.getPhoneNumber())) {
                                phones.add(response.getPhoneNumber());
                            }
                        }
                    }
                }

                // get contract by phone number
                if (phones.size() > 0) {
                    for (String phone : phones) {
                        List<HDContractResponse> contractResponses = hdMiddleService.getListContractByPhoneNumber(phone);
                        if (contractResponses == null) {
                            continue;
                        }

                        for (HDContractResponse response : contractResponses) {
                            if (!contractCodes.contains(response.getContractNumber())) {
                                response.setPhoneRequest(true);
                                contractResponseInserts.add(response);
                                contractCodes.add(response.getContractNumber());
                            }
                        }
                    }
                }

                if (contractResponseInserts != null && contractResponseInserts.size() > 0) {
                    List<String> contractCodeNews = new ArrayList<>();
                    for (HDContractResponse contractResponse : contractResponseInserts) {
                        if(!validateInsertContract(contractInfo,contractResponse)){
                            continue;
                        }

                        Date documentVerification = DateUtils.covertStringToDate(contractResponse.getDocumentVerificationDate());
                        Date printingDate = DateUtils.covertStringToDate(contractResponse.getContractPrintingDate());

                        if (documentVerification == null && printingDate == null) {

                            PhoneAndStatus phoneAndStatus = hdMiddleService.getPhoneAndStatusByContractCode(contractResponse.getContractNumber());
                            if (phoneAndStatus != null) {
                                if (phoneAndStatus.getDocumentVerificationDate() == null && phoneAndStatus.getContractPrintingDate() == null) {
                                    continue;
                                }
                            }
                        }

                        // insert contract
                        if (!insertContract(contractResponse,customerUuid)) {
                            throw new InternalServerErrorException();
                        }
                        contractCodeNews.add(contractResponse.getContractNumber());
                    }
                    if (contractCodeNews != null && contractCodeNews.size() > 0) {
                        contractCodeFm  = StringUtils.join(contractCodes, ",");
                    }
                }
             }

            // insert log update
            LogUpdateContractWhenLogin updateContractWhenLogin = new LogUpdateContractWhenLogin();
            updateContractWhenLogin.setCustomerUuid(customerUuid);
            updateContractWhenLogin.setCreatedAt(new Date());
            updateContractWhenLogin.setContractCode(contractCodeFm);
            logUpdateContractService.saveOrUpdate(updateContractWhenLogin);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }

    public boolean validateInsertContract(ContractInfo contractInfo,HDContractResponse contractResponse){

        if (contractInfo == null || contractResponse == null) {
            return false;
        }

        try {
            String fullNameNew = contractResponse.getLastName() + contractResponse.getFirstName();
            String fullNameOle = contractInfo.getLastName() + contractInfo.getFirstName();
            fullNameNew = HDUtil.unAccent(fullNameNew.toLowerCase());
            fullNameOle = HDUtil.unAccent(fullNameOle.toLowerCase());
            if (!fullNameNew.equals(fullNameOle)) {
                return false;
            }

            if (contractResponse.getPhoneRequest() != null && !contractResponse.getPhoneRequest()){
                String phoneOld = contractInfo.getPhoneNumber();
                String phoneNew = contractResponse.getPhoneNumber();

                if (phoneOld != null && phoneNew != null && phoneOld.equals(phoneNew)) {
                    return true;
                }

            }

            Date birthdayOld = contractInfo.getBirthday();
            Date birthdayNew = contractResponse.getDob();

            if (birthdayOld != null && birthdayNew != null && birthdayOld.compareTo(birthdayNew) == 0) {
                return true;
            }

            String familyBookNoOld = contractInfo.getFamilyBookNo();
            String familyBookNoNew = contractResponse.getFamilyBookNo();

            String driversLicenceOld = contractInfo.getDriversLicence();
            String driversLicenceNew = contractResponse.getDriversLicence();

            if ((familyBookNoOld != null && familyBookNoNew != null && familyBookNoOld.equals(familyBookNoNew))
                    || (driversLicenceNew != null && driversLicenceOld != null && driversLicenceOld.equals(driversLicenceNew))) {
                return true;
            }
        }catch (Exception e) {
            return false;
        }
        return false;
    }


    public boolean insertContract(HDContractResponse hdContractResponse, UUID customerUuid) {
        try {


            Contract con = contractService.getContractByContractCode(hdContractResponse.getContractNumber());
            UUID contractUuid = null;
            if (con == null) {

                // insert contract
                Contract contract = new Contract();
                contract.setCreatedAt(new Date());
                contract.setLendingCoreContractId(hdContractResponse.getContractNumber());
                contract.setContractUuid(UUID.randomUUID());
                contract.setPhone(HDUtil.maskNumber(hdContractResponse.getPhoneNumber(), "xxxxxx####"));
                String nationalId = "";
                if (hdContractResponse.getNationalID().length() == 9) {
                    nationalId = HDUtil.maskNumber(hdContractResponse.getNationalID(), "xxx xxx ###");
                } else {
                    nationalId = HDUtil.maskNumber(hdContractResponse.getNationalID(), "xxxx xxxx ####");
                }
                contract.setIdentifyId(nationalId);
                contract.setStatus(hdContractResponse.getStatus());
                contract.setIsInsurance(hdContractResponse.getIsInsurance());
                // Date
                contract.setContractCompleteDate(DateUtils.covertStringToDate(hdContractResponse.getContractPrintingCompleted()));
                contract.setFirstDue(hdContractResponse.getFirstDue());
                contract.setEndDue(hdContractResponse.getEndDue());
                contract.setDocumentVerificationDate(DateUtils.covertStringToDate(hdContractResponse.getDocumentVerificationDate()));
                contract.setContractPrintingDate(DateUtils.covertStringToDate(hdContractResponse.getContractPrintingDate()));

                // String
                contract.setContractCompleteDateTemp(hdContractResponse.getContractPrintingCompleted());
                contract.setFirstDueTemp(hdContractResponse.getFirstDue().toString());
                contract.setEndDueTemp(hdContractResponse.getEndDue().toString());
                contract.setDocumentVerificationDateTemp(hdContractResponse.getDocumentVerificationDate());
                contract.setContractPrintingDateTemp(hdContractResponse.getContractPrintingDate());

                // BigDecimal
                contract.setTenor(hdContractResponse.getTenor());
                contract.setLoanAmount(hdContractResponse.getLoanAmount());
                contract.setMonthlyDueDate(hdContractResponse.getMonthlyDueDate());
                contract.setMonthlyInstallmentAmount(hdContractResponse.getMonthlyInstallmentAmount());
                contractService.createContract(contract);

                contractUuid = contract.getContractUuid();
            } else {
                contractUuid = con.getContractUuid();
            }

            // insert contract customer
            ContractCustomer contractCustomer = contractCustomerService.getByContractCodeAndCustomerUuid(hdContractResponse.getContractNumber(),customerUuid);

            if (contractCustomer == null) {
                contractCustomer = new ContractCustomer();
                contractCustomer.setContractUuid(contractUuid);
                contractCustomer.setCreatedAt(new Date());
                contractCustomer.setCustomerUuid(customerUuid);
                contractCustomer.setStatus(1);
                contractCustomer.setIsRepaymentNotification(1);
                contractCustomer.setContractCode(hdContractResponse.getContractNumber());
                contractCustomerService.insertContractCustomer(contractCustomer);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
