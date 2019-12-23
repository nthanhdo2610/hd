package com.tinhvan.hd.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.ResponseDTO;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.ContractEditInfo;
import com.tinhvan.hd.entity.LogCallProcedureMiddleDB;
import com.tinhvan.hd.entity.SignUpLoan;
import com.tinhvan.hd.repository.ContractEditInfoRepository;
import com.tinhvan.hd.service.HDMiddleService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HDMiddleServiceImpl implements HDMiddleService {

    @Value("${hd.url.request.middle}")
    private String baseUrl;

    @Value("${hd.url.request.ldap}")
    private String ldapUrl;

    @Value("${hd.secretKey}")
    private String secretKey;


    @Autowired
    private ContractEditInfoRepository contractEditInfoRepository;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public HDMiddleServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public HDContractResponse getContractByContractCode(String contractCode) {

        ContractRegister contractRegister = new ContractRegister();
        contractRegister.setContractCode(contractCode);

        String subPathRequest = "/byContractCode";

        List<HDContractResponse> list = sendRequestHdMiddleServer(contractRegister,subPathRequest,true);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public HDContractResponse getContractByContractCodeAndIdentifyId(String contractCode, String identifyId) {
        ContractRegister contractRegister = new ContractRegister();
        contractRegister.setContractCode(contractCode);
        contractRegister.setIdentifyId(identifyId);
        String subPathRequest = "/byContractCodeAndIdentifyId";

        List<HDContractResponse> list = sendRequestHdMiddleServer(contractRegister,subPathRequest,false);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<HDContractResponse> getListContractByIdentifyId(String identifyId) {
        ContractRegister contractRegister = new ContractRegister();
        contractRegister.setIdentifyId(identifyId);
        String subPathRequest = "/byIdentifyId";

        List<HDContractResponse> list = sendRequestHdMiddleServer(contractRegister,subPathRequest,true);

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public List<HDContractResponse> getListContractByIdentifyIdRealTime(String identifyId) {
        ContractRegister contractRegister = new ContractRegister();
        contractRegister.setIdentifyId(identifyId);
        String subPathRequest = "/byIdentifyIdRealTime";

        List<HDContractResponse> list = sendRequestHdMiddleServer(contractRegister,subPathRequest,true);

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public List<HDContractResponse> getListContractByPhoneNumber(String phoneNumber) {
        ContractRegister contractRegister = new ContractRegister();
        contractRegister.setPhoneNumber(phoneNumber);
        String subPathRequest = "/byPhoneNumber";

        List<HDContractResponse> list = sendRequestHdMiddleServer(contractRegister,subPathRequest,true);

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public List<HDContractResponse> getListContractByPhoneNumberRealTime(String phoneNumber) {
        ContractRegister contractRegister = new ContractRegister();
        contractRegister.setPhoneNumber(phoneNumber);
        String subPathRequest = "/byPhoneNumberRealTime";

        List<HDContractResponse> list = sendRequestHdMiddleServer(contractRegister,subPathRequest,true);

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public ContractInfo getContractDetailFromMidServer(String contractCode) {
        ContractInfo contractInfo = null;
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        logCallProcedureMiddleDB.setProcedureName("MIDDB.get_coninfo_by_contractid");
        ContractRegister contractRegister = new ContractRegister();
        contractRegister.setContractCode(contractCode);
        logCallProcedureMiddleDB.setParameter(contractRegister.toString());
        logCallProcedureMiddleDB.setStartTime(new Date());
        String errorMessage = "";
        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/getContractDetailByContractCode");
            HttpEntity<ContractRegister> request = new HttpEntity<>(contractRegister, headers);

            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());
            if (result.getStatusCode() == HttpStatus.OK){
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();

                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    contractInfo = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<ContractInfo>() {
                            });
                    if (contractInfo != null) {
                        ContractEditInfo contractEditInfo = contractEditInfoRepository.findByContractCode(contractInfo.getContractNumber());

                        if (contractEditInfo != null) {
                            if (contractInfo.getMonthlyDueDate() == null) {
                                contractInfo.setMonthlyDueDate(new BigDecimal(contractEditInfo.getMonthlyDueDate()));
                            }

                            if (contractInfo.getEnginerNo() == null || contractInfo.getChassisNo() == null) {
                                contractInfo.setChassisNo(contractEditInfo.getChassisno());
                                contractInfo.setEnginerNo(contractEditInfo.getEnginerno());
                            }

                            if (contractInfo.getFirstDue() == null || contractInfo.getEndDue() == null) {
                                contractInfo.setFirstDue(contractEditInfo.getFirstDate());
                                contractInfo.setEndDue(contractEditInfo.getEndDate());
                            }
                        }
                    }

                } else {
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }
            }
        } catch (Exception e) {
            logCallProcedureMiddleDB.setErrorStr(errorMessage + "|" + e.getMessage());
            logCallProcedureMiddleDB.setStatus("Fail");
            e.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return contractInfo;
    }

    @Override
    public List<PaymentInformation> getListPaymentInfoByContractCodeAndLatestPaymentDate(PaymentInfoRequest paymentInfoRequest) {
        List<PaymentInformation> paymentInformations = new ArrayList<>();
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        logCallProcedureMiddleDB.setProcedureName("MIDDB.get_payinfo_by_con_realtime");

        logCallProcedureMiddleDB.setParameter(paymentInfoRequest.toString());
        logCallProcedureMiddleDB.setStartTime(new Date());
        String errorMessage = "";
        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/getPaymentInformationByContractCode");
            HttpEntity<PaymentInfoRequest> request = new HttpEntity<>(paymentInfoRequest, headers);

            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());
            if (result.getStatusCode() == HttpStatus.OK){
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();
                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    paymentInformations = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<PaymentInformation>>() {
                            });
                }else {
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }


            }
        } catch (Exception e) {
            logCallProcedureMiddleDB.setErrorStr(errorMessage + " | " + e.getMessage());
            logCallProcedureMiddleDB.setStatus("Fail");
            e.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return paymentInformations;
    }

    @Override
    public PhoneAndStatus getPhoneAndStatusByContractCode(String contractCode) {
        PhoneAndStatus phoneAndStatus = null;
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        logCallProcedureMiddleDB.setProcedureName("MIDDB.get_stat_phone_by_con_realtime");
        ContractRegister contractRegister = new ContractRegister();
        contractRegister.setContractCode(contractCode);
        logCallProcedureMiddleDB.setParameter(contractRegister.toString());
        logCallProcedureMiddleDB.setStartTime(new Date());
        String errorMessage = "";
        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/getStatusAndPhoneNumberByContractCode");
            HttpEntity<ContractRegister> request = new HttpEntity<>(contractRegister, headers);

            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            if (result.getStatusCode() == HttpStatus.OK){
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();

                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    phoneAndStatus = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<PhoneAndStatus>() {
                            });
                }else {
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }

            }
        } catch (Exception e) {
            logCallProcedureMiddleDB.setErrorStr(errorMessage + "|" + e.getMessage());
            logCallProcedureMiddleDB.setStatus("Fail");
            e.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return phoneAndStatus;
    }

    @Override
    public List<HDContractResponse> getListContractByIdentifyIds(IdentifyIds identifyIds) {
        List<HDContractResponse> list = new ArrayList<>();

        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            URI uri = new URI(baseUrl + "/getAllContractByListIdentifyId");

            HttpEntity<IdentifyIds> request = new HttpEntity<>(identifyIds, headers);

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);

            if (result.getStatusCode() == HttpStatus.OK){
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();

                list = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<List<HDContractResponse>>() {
                        });

            }
        }catch (URISyntaxException ex){
            ex.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();

        } catch (JsonMappingException e) {
            e.printStackTrace();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insertSignUpLoan(SignUpLoanRequest signUpLoanRequest) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.insert_loan_form");

            logCallProcedureMiddleDB.setParameter(signUpLoanRequest.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/insertSignUpLoan");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<SignUpLoanRequest> request = new HttpEntity<>(signUpLoanRequest, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
        } catch (Exception ex) {
             ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
    }

    @Override
    public void insertSignUpPromotion(SignUpPromotionRequest signUpPromotionRequest) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.insert_loan_form_promotion");

            logCallProcedureMiddleDB.setParameter(signUpPromotionRequest.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/insertSignUpPromotion");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<SignUpPromotionRequest> request = new HttpEntity<>(signUpPromotionRequest, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
    }

    @Override
    public boolean updateMonthlyDueDateContract(UpdateMonthlyDueDate updateMonthlyDueDate) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        boolean isUpdate = true;
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_monthly_due_date");

            logCallProcedureMiddleDB.setParameter(updateMonthlyDueDate.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/updateMonthlyDueDate");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<UpdateMonthlyDueDate> request = new HttpEntity<>(updateMonthlyDueDate, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);

            if (logCallProcedureMiddleDB.getStatus().equals("Fail")){
                isUpdate = false;
            }

        } catch (Exception ex) {
            isUpdate = false;
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return isUpdate;
    }

    @Override
    public boolean updateChassisNoAndEngineerNo(UpdateChassisNoAndEnginerNo chassisNoAndEnginerNo) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        boolean isUpdate = true;
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_chassisno_and_enginerno");

            logCallProcedureMiddleDB.setParameter(chassisNoAndEnginerNo.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/updateChassisNoAndEngineerNo");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<UpdateChassisNoAndEnginerNo> request = new HttpEntity<>(chassisNoAndEnginerNo, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);

            if (logCallProcedureMiddleDB.getStatus().equals("Fail")){
                isUpdate = false;
            }
        } catch (Exception ex) {
            isUpdate = false;
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return isUpdate;
    }

    @Override
    public boolean updateStatusByContractCode(UpdateStatus updateStatus) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        boolean isUpdate = true;
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_status_by_contractCode");

            logCallProcedureMiddleDB.setParameter(updateStatus.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/updateStatusByContractCodeWhenEsign");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<UpdateStatus> request = new HttpEntity<>(updateStatus, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
            if (logCallProcedureMiddleDB.getStatus().equals("Fail")){
                isUpdate = false;
            }
        } catch (Exception ex) {
            isUpdate = false;
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return isUpdate;
    }

    @Override
    public void insertDisbursementInfo(DisbursementInfo disbursementInfo) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.insert_disbursement_info");

            logCallProcedureMiddleDB.setParameter(disbursementInfo.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/insertDisbursementInfo");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<DisbursementInfo> request = new HttpEntity<>(disbursementInfo, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
    }

    @Override
    public void updateDisbursementInfo(UpdateDisbursementInfo updateDisbursementInfo) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_disbursement_info");

            logCallProcedureMiddleDB.setParameter(updateDisbursementInfo.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/updateDisbursementInfo");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<UpdateDisbursementInfo> request = new HttpEntity<>(updateDisbursementInfo, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
    }

    @Override
    public void updateSignUpLoan(UpdateSignUpLoan updateSignUpLoan) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_sign_up_loan");

            logCallProcedureMiddleDB.setParameter(updateSignUpLoan.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/updateSignUpLoan");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<UpdateSignUpLoan> request = new HttpEntity<>(updateSignUpLoan, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
    }

    @Override
    public void updateSignUpPromotion(UpdateSignUpPromotion updateSignUpPromotion) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_sign_up_promotion");

            logCallProcedureMiddleDB.setParameter(updateSignUpPromotion.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/updateSignUpPromotion");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<UpdateSignUpPromotion> request = new HttpEntity<>(updateSignUpPromotion, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
    }

    @Override
    public List<ResultDisbursementInfo> getDisbursementInfoByIsSent(SearchDisbursementInfo searchDisbursementInfo) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        String errorMessage = "";
        List<ResultDisbursementInfo> list = new ArrayList<>();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.search_disbursement_info");

            logCallProcedureMiddleDB.setParameter(searchDisbursementInfo.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/searchDisbursementInfo");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<SearchDisbursementInfo> request = new HttpEntity<>(searchDisbursementInfo, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            ObjectMapper mapper = new ObjectMapper();

            if (result.getStatusCode() == HttpStatus.OK) {
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();
                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    list = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<ResultDisbursementInfo>>() {
                            });
                }else{
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return list;
    }

    @Override
    public List<ResultSearchSignUpLoan> searchSignUpLoan(SearchSignUpLoan searchSignUpLoan) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        String errorMessage = "";
        List<ResultSearchSignUpLoan> list = new ArrayList<>();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.search_sign_up_loan");

            logCallProcedureMiddleDB.setParameter(searchSignUpLoan.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/searchSignUpLoan");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<SearchSignUpLoan> request = new HttpEntity<>(searchSignUpLoan, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            ObjectMapper mapper = new ObjectMapper();

            if (result.getStatusCode() == HttpStatus.OK) {
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();
                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    list = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<ResultSearchSignUpLoan>>() {
                            });
                }else{
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return list;
    }

    @Override
    public List<ResultSearchSignUpPromotion> searchSignUpPromotion(SearchSignUpPromotion searchSignUpPromotion) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        String errorMessage = "";
        List<ResultSearchSignUpPromotion> list = new ArrayList<>();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.search_sign_up_promotion");

            logCallProcedureMiddleDB.setParameter(searchSignUpPromotion.toString());
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/searchSignUpPromotion");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<SearchSignUpPromotion> request = new HttpEntity<>(searchSignUpPromotion, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            ObjectMapper mapper = new ObjectMapper();

            if (result.getStatusCode() == HttpStatus.OK) {
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();
                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    list = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<ResultSearchSignUpPromotion>>() {
                            });
                }else{
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return list;
    }

    @Override
    public List<ContractInfo> getContractDetailFromMidServers(List<String> contractCodes) {
        List<ContractInfo> contractInfos = null;
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        logCallProcedureMiddleDB.setProcedureName("MIDDB.get_coninfo_by_contractid");
        ContractCodes codes = new ContractCodes();
        codes.setContractCodes(contractCodes);
        logCallProcedureMiddleDB.setParameter(contractCodes.toString());
        logCallProcedureMiddleDB.setStartTime(new Date());
        String errorMessage = "";
        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/getContractDetailByContractCodes");

            HttpEntity<ContractCodes> request = new HttpEntity<>(codes, headers);

            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());
            if (result.getStatusCode() == HttpStatus.OK){
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();

                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    contractInfos = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<ContractInfo>>() {
                            });
                } else {
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }
            }
        } catch (Exception e) {
            logCallProcedureMiddleDB.setErrorStr(errorMessage + "|" + e.getMessage());
            logCallProcedureMiddleDB.setStatus("Fail");
            e.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return contractInfos;
    }

    @Override
    public List<PhoneAndStatus> getPhoneAndStatusByContractCodes(List<String> contractCodes) {
        List<PhoneAndStatus> statusList = new ArrayList<>();
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        logCallProcedureMiddleDB.setProcedureName("MIDDB.get_stat_phone_by_con_realtime");
        ContractCodes codes = new ContractCodes();
        codes.setContractCodes(contractCodes);
        logCallProcedureMiddleDB.setParameter(codes.toString());
        logCallProcedureMiddleDB.setStartTime(new Date());
        String errorMessage = "";
        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/getStatusAndPhoneNumberByContractCodes");
            HttpEntity<ContractCodes> request = new HttpEntity<>(codes, headers);

            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            if (result.getStatusCode() == HttpStatus.OK){
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();

                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    statusList = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<PhoneAndStatus>>() {
                            });
                }else {
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }

            }
        } catch (Exception e) {
            logCallProcedureMiddleDB.setErrorStr(errorMessage + "|" + e.getMessage());
            logCallProcedureMiddleDB.setStatus("Fail");
            e.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return statusList;
    }

    @Override
    public List<PaymentInformation> getListPaymentInfoPaymentByContractCodes(List<String> contractCodes) {
        List<PaymentInformation> paymentInformations = new ArrayList<>();
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        logCallProcedureMiddleDB.setProcedureName("MIDDB.get_payinfo_by_con_realtime");
        ContractCodes codes = new ContractCodes();
        codes.setContractCodes(contractCodes);
        logCallProcedureMiddleDB.setParameter(codes.toString());
        logCallProcedureMiddleDB.setStartTime(new Date());
        String errorMessage = "";
        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/getPaymentInformationByContractCodes");
            HttpEntity<ContractCodes> request = new HttpEntity<>(codes, headers);

            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            if (result.getStatusCode() == HttpStatus.OK){
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();

                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    paymentInformations = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<PaymentInformation>>() {
                            });
                }else {
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }

            }
        } catch (Exception e) {
            logCallProcedureMiddleDB.setErrorStr(errorMessage + "|" + e.getMessage());
            logCallProcedureMiddleDB.setStatus("Fail");
            e.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return paymentInformations;
    }

    @Override
    public String checkStaffWithConnectLdap(RequestConnectLDap requestConnectLDap) {
        String displayName = "";
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        logCallProcedureMiddleDB.setProcedureName("check_connect_ldap_login_staff");

        logCallProcedureMiddleDB.setParameter(requestConnectLDap.toString());
        logCallProcedureMiddleDB.setStartTime(new Date());
        String errorMessage = "";
        try {

            HttpHeaders headers = setHeaderRequest();

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(ldapUrl + "/connect");
            HttpEntity<RequestConnectLDap> request = new HttpEntity<>(requestConnectLDap, headers);

            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            if (result.getStatusCode() == HttpStatus.OK){
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();

                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    displayName = (String) dto.getPayload();
                }else {
                    errorMessage = dto.getMessage();
                    if (errorMessage == null) {
                        errorMessage = "";
                    }
                    logCallProcedureMiddleDB.setErrorStr(errorMessage);
                    logCallProcedureMiddleDB.setStatus("Fail");
                }
            }
        } catch (Exception e) {
            logCallProcedureMiddleDB.setErrorStr(errorMessage + "|" + e.getMessage());
            logCallProcedureMiddleDB.setStatus("Fail");
            e.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return displayName;
    }

    @Override
    public List<PaymentInformation> getListPaymentInfoByContractCodeAndLatestPaymentDates(List<PaymentInfoRequest> paymentInfoRequests) {
        List<PaymentInformation> paymentInformations = new ArrayList<>();

        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            URI uri = new URI(baseUrl + "/getPaymentInformationByRequestSearch");
            HttpEntity<List<PaymentInfoRequest>> request = new HttpEntity<>(paymentInfoRequests, headers);

            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);

            if (result.getStatusCode() == HttpStatus.OK){
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();
                if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                    paymentInformations = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<PaymentInformation>>() {
                            });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentInformations;
    }

    @Override
    public boolean updateStatusAdjustmentInfo(String contractCode) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();

        boolean isUpdate = true;
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_IndusStatus_after_DocVeri");

            logCallProcedureMiddleDB.setParameter(contractCode);
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/updateStatusAfterDocVerify");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<String> request = new HttpEntity<>(contractCode, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
            if (logCallProcedureMiddleDB.getStatus().equals("Fail")){
                isUpdate = false;
            }
        } catch (Exception ex) {
            isUpdate = false;
            logCallProcedureMiddleDB.setStatus("FAIL");
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return isUpdate;
    }

    @Override
    public void updateDisbursementInfos(List<UpdateDisbursementInfo> updateDisbursementInfos) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_disbursement_info");

            logCallProcedureMiddleDB.setParameter("");
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/updateDisbursementInfos");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<List<UpdateDisbursementInfo>> request = new HttpEntity<>(updateDisbursementInfos, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
    }

    @Override
    public void updateSignUpLoans(List<UpdateSignUpLoan> updateSignUpLoans) {
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        try {

            logCallProcedureMiddleDB.setProcedureName("MIDDB.update_sign_up_loan");

            logCallProcedureMiddleDB.setParameter("");
            logCallProcedureMiddleDB.setStartTime(new Date());

            RestTemplate restTemplate = new RestTemplate();

            setTimeout(restTemplate);

            URI uri = new URI(baseUrl + "/updateSignUpLoans");

            HttpHeaders headers = setHeaderRequest();

            HttpEntity<List<UpdateSignUpLoan>> request = new HttpEntity<>(updateSignUpLoans, headers);
            logCallProcedureMiddleDB.setStartTime(new Date());
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());

            logCallProcedureMiddleDB = checkResponseLog(result,logCallProcedureMiddleDB);

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
    }

    public LogCallProcedureMiddleDB checkResponseLog(ResponseEntity<ResponseDTO> result,LogCallProcedureMiddleDB logCallProcedureMiddleDB){
        if (result.getStatusCode() == HttpStatus.OK) {
            ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();
            if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                logCallProcedureMiddleDB.setStatus("Success");
            } else {
                logCallProcedureMiddleDB.setErrorStr(dto.getMessage());
                logCallProcedureMiddleDB.setStatus("Fail");
            }
        }

        return  logCallProcedureMiddleDB;
    }

    public List<HDContractResponse> sendRequestHdMiddleServer(ContractRegister contractRegister,String subPathRequest,boolean isList) {
        List<HDContractResponse> list = new ArrayList<>();
        LogCallProcedureMiddleDB logCallProcedureMiddleDB = new LogCallProcedureMiddleDB();
        String nameProcedure = "";
        if (subPathRequest.equals("/byContractCodeAndIdentifyId")){
            nameProcedure = "MIDDB.get_customer_identification";
        }else if (subPathRequest.equals("/byIdentifyId")){
            nameProcedure = "MIDDB.get_contract_list_by_id";
        }else if (subPathRequest.equals("/byIdentifyIdRealTime")){
            nameProcedure = "MIDDB.get_contract_list_id_realtime";
        }else if (subPathRequest.equals("/byPhoneNumber")){
            nameProcedure = "MIDDB.get_contract_by_phonenumber";
        }else if (subPathRequest.equals("/byPhoneNumberRealTime")){
            nameProcedure = "MIDDB.get_contract_phonenum_realtime";
        }
        logCallProcedureMiddleDB.setProcedureName(nameProcedure);
        logCallProcedureMiddleDB.setParameter(contractRegister.toString());
        logCallProcedureMiddleDB.setStartTime(new Date());
        String errorMessage = "";
        try {

            ObjectMapper mapper = new ObjectMapper();

            HttpHeaders headers = setHeaderRequest();

            URI uri = new URI(baseUrl + subPathRequest);

            HttpEntity<ContractRegister> request = new HttpEntity<>(contractRegister, headers);

            RestTemplate restTemplate = new RestTemplate();
            setTimeout(restTemplate);
            ResponseEntity<ResponseDTO> result = restTemplate.postForEntity(uri, request, ResponseDTO.class);
            logCallProcedureMiddleDB.setEndTime(new Date());
            if (result.getStatusCode() == HttpStatus.OK){
                logCallProcedureMiddleDB.setStatus("Success");
                ResponseDTO<Object> dto = (ResponseDTO<Object>) result.getBody();
                if (isList) {
                    if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                        list = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                                new TypeReference<List<HDContractResponse>>() {
                                });
                    }else{
                        errorMessage = dto.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "";
                        }
                        logCallProcedureMiddleDB.setErrorStr(errorMessage);
                        logCallProcedureMiddleDB.setStatus("Fail");
                    }
                }else{
                    if (HDUtil.isNullOrEmpty(dto.getMessage())) {
                        HDContractResponse hdContractResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                                new TypeReference<HDContractResponse>() {
                                });
                        list.add(hdContractResponse);
                    }else {
                        errorMessage = dto.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "";
                        }
                        logCallProcedureMiddleDB.setErrorStr(errorMessage);
                        logCallProcedureMiddleDB.setStatus("Fail");
                    }
                }

            }
        } catch (Exception e) {
            logCallProcedureMiddleDB.setErrorStr(errorMessage + " | " + e.getMessage());
            logCallProcedureMiddleDB.setStatus("Fail");
            e.printStackTrace();
        }finally {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_PROCEDURE, logCallProcedureMiddleDB);
        }
        return list;
    }

    public HttpHeaders setHeaderRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", secretKey);
        return headers;
    }

    public void setTimeout(RestTemplate restTemplate){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(30*1000); // set short connect timeout
        requestFactory.setReadTimeout(120*1000); // set slightly longer read timeout
        restTemplate.setRequestFactory(requestFactory);
    }
}
