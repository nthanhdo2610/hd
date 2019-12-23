package com.tinhvan.hd.service;

import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.SignUpLoan;

import java.util.Date;
import java.util.List;

public interface HDMiddleService {

    HDContractResponse getContractByContractCode(String contractCode);

    HDContractResponse getContractByContractCodeAndIdentifyId(String contractCode,String identifyId);

    List<HDContractResponse> getListContractByIdentifyId(String identifyId);

    List<HDContractResponse> getListContractByIdentifyIdRealTime(String identifyId);

    List<HDContractResponse> getListContractByPhoneNumber(String phoneNumber);

    List<HDContractResponse> getListContractByPhoneNumberRealTime(String phoneNumber);

    ContractInfo getContractDetailFromMidServer(String contractCode);

    List<PaymentInformation> getListPaymentInfoByContractCodeAndLatestPaymentDate(PaymentInfoRequest paymentInfoRequest);

    PhoneAndStatus getPhoneAndStatusByContractCode(String contractCode);

    List<HDContractResponse> getListContractByIdentifyIds(IdentifyIds identifyIds);

    void insertSignUpLoan(SignUpLoanRequest signUpLoan);

    void insertSignUpPromotion(SignUpPromotionRequest signUpPromotionRequest);

    boolean updateMonthlyDueDateContract(UpdateMonthlyDueDate updateMonthlyDueDate);

    boolean updateChassisNoAndEngineerNo(UpdateChassisNoAndEnginerNo chassisNoAndEnginerNo);

    boolean updateStatusByContractCode(UpdateStatus updateStatus);

    void insertDisbursementInfo(DisbursementInfo disbursementInfo);

    void updateDisbursementInfo(UpdateDisbursementInfo updateDisbursementInfo);

    void updateSignUpLoan(UpdateSignUpLoan updateSignUpLoan);

    void updateSignUpPromotion(UpdateSignUpPromotion updateSignUpPromotion);

    List<ResultDisbursementInfo> getDisbursementInfoByIsSent(SearchDisbursementInfo searchDisbursementInfo);

    List<ResultSearchSignUpLoan> searchSignUpLoan(SearchSignUpLoan searchSignUpLoan);

    List<ResultSearchSignUpPromotion> searchSignUpPromotion(SearchSignUpPromotion searchSignUpPromotion);

    List<ContractInfo> getContractDetailFromMidServers(List<String> contractCodes);

    List<PhoneAndStatus> getPhoneAndStatusByContractCodes(List<String> contractCodes);

    List<PaymentInformation> getListPaymentInfoPaymentByContractCodes(List<String> contractCodes);

    String checkStaffWithConnectLdap(RequestConnectLDap requestConnectLDap);

    List<PaymentInformation> getListPaymentInfoByContractCodeAndLatestPaymentDates(List<PaymentInfoRequest> paymentInfoRequests);

    boolean updateStatusAdjustmentInfo(String contractCode);

    void updateDisbursementInfos(List<UpdateDisbursementInfo> updateDisbursementInfos);

    void updateSignUpLoans(List<UpdateSignUpLoan> updateSignUpLoans);
}
