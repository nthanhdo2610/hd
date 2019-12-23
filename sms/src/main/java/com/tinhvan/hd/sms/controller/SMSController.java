/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.sms.bean.*;
import com.tinhvan.hd.sms.infobip.Message;
import com.tinhvan.hd.sms.infobip.SMSGateWay;
import com.tinhvan.hd.sms.invoke.ConfigContractTypeBackground;
import com.tinhvan.hd.sms.invoke.ContractEsignedRequest;
import com.tinhvan.hd.sms.model.OTP;
import com.tinhvan.hd.sms.model.SMS;
import com.tinhvan.hd.sms.model.SMSTemplate;
import com.tinhvan.hd.sms.service.OTPService;
import com.tinhvan.hd.sms.service.SMSService;
import com.tinhvan.hd.sms.service.SMSTemplateService;
import com.tinhvan.hd.sms.util.EncryptionUtils;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author LUUBI
 */
@RestController
@RequestMapping("api/v1/sms")
public class SMSController extends HDController {
    Logger logger = Logger.getLogger(SMSController.class.getName());

    @Autowired
    SMSService sMSService;

    @Autowired
    SMSTemplateService sMSTemplateService;

    @Autowired
    OTPService oTPService;

    Invoker invoker = new Invoker();
    @Value("${app.module.config_staff.service.url}")
    private String urlConfigStaff;

    @Value("${app.module.sms_gateway.service.url}")
    private String urlSMSGateway;

    @Value("${app.module.customer.service.url}")
    private String urlCustomer;

    @Value("${app.module.contract.service.url}")
    private String urlContract;

    @Value("${app.module.config_contract_type_background.url}")
    private String urlContractTypeBackground;

    //config account sms
    @Value("${config.key.sms_username}")
    private String smsUsername;

    @Value("${config.key.sms_password}")
    private String smsPassword;

    @Value("${config.key.sms_base_url}")
    private String baseUrl;

    /**
     * Send sms when use lending app
     *
     * @param request object SMSRequest contain information send sms
     * @return http status code
     */
    @PostMapping(value = "/send")
    public ResponseEntity<?> send(@RequestBody RequestDTO<SMSRequest> request) {
        SMSRequest sMSRequest = request.init();
        logger.info("send intput:" + sMSRequest.toString());
        //save sms
        try {
            String content = getContentSmsTemplate(sMSRequest.getParam(), sMSRequest.getSmsType(), sMSRequest.getLangCode());
            SMS smsres = new SMS(request.now(), sMSRequest.getPhoneNumber(), content, "");
//            smsres.setStatus(1);
//            sMSService.create(smsres);
//            getSMSGateway(smsres);
            getSMSGatewayHD(smsres);
        } catch (Exception ex) {
            throw new BadRequestException(1247, ex.getMessage());
        }
        //send sms
        //call rabbit mq, get result sms gateway and update sms
        //sMSService.mqSendSMS(smsres);
        return ok("ok");
    }


    /**
     * Connect sms_gateway using username, password sending sms
     *
     * @param smsRequest object SMS contain information send sms
     * @return no return
     */
    private void getSMSGatewayHD(SMS smsRequest) {
        EncryptionUtils encryptionUtils = new EncryptionUtils();
        ObjectMapper objectMapper = new ObjectMapper();
        String[] phoneOtps = HDConfig.getInstance().getList("PHONE_OTP");
        List<String> listPhone = Arrays.asList(phoneOtps);
        SMSGateWay smsGateWay = null;
        List<Message> listMessage = null;
        if (listPhone != null && !listPhone.isEmpty()) {
            for (String phone : listPhone) {
                Map<String, String> map = new HashMap<>();
                map.put("from", "HDSAISON");
                // map.put("to", smsRequest.getPhone());
                map.put("to", formatPhone(phone));
//                    map.put("to", formatPhone(smsRequest.getPhone()));
                map.put("text", smsRequest.getMessage());
                try {
                    String baseURL = encryptionUtils.decrypt(baseUrl, encryptionUtils.getKey());
                    String username = encryptionUtils.decrypt(smsUsername, encryptionUtils.getKey());
                    String password = encryptionUtils.decrypt(smsPassword, encryptionUtils.getKey());
                    String headerValue = (username + ':' + password);
                    byte[] bytesEncoded = Base64.encodeBase64(headerValue.getBytes());
                    String authorization = new String(bytesEncoded);
                    HttpResponse<String> response = Unirest.post(baseURL)
                            .header("authorization", "Basic "+authorization)
                            .header("content-type", "application/json")
                            .header("accept", "application/json")
                            .body(new JSONObject(map))
                            .asString();

                    smsGateWay = objectMapper.readValue(response.getBody(), SMSGateWay.class);
                } catch (IOException e) {
                    smsRequest.setStatus(2);
                    e.printStackTrace();
                }
            }
        }
        if (smsGateWay != null) {
            //insert status sms = 1 success
            listMessage = smsGateWay.getMessages();
            if (listMessage != null && !listMessage.isEmpty()) {
                smsRequest.setStatus(1);
                smsRequest.setMessageId(smsGateWay.getMessages().get(0).getMessageId());
            }
        }
        sMSService.create(smsRequest);
    }

    /**
     * format phone number
     *
     * @param phone
     * @return format phone number
     */
    private String formatPhone(String phone) {
        if (phone.startsWith("0")) {
            return phone.replaceFirst("0", "84");
        }
        return phone;
    }

    /**
     * Get otp and sending sms
     *
     * @param request object SMSGetOTP contain information get otp
     * @return SMSGetOTPRespon content information (otpExpired, otpResend)
     */
    @PostMapping("/get_otp")
    public ResponseEntity<?> getOTP(@RequestBody RequestDTO<SMSGetOTP> request) {
        SMSGetOTP sMSGetOTP = request.init();
        logger.info("get_otp intput:" + sMSGetOTP.toString());
        String otpType = sMSGetOTP.getOtpType();
        // check limit getOTP
        if (checkLimitOTP(sMSGetOTP.getCustomerUUID().toString())) {
            String otpExpired = getValue(new ConfigStaffGetValue("otp_expired"));
            String otpResend = getValue(new ConfigStaffGetValue("otp_resend"));
            String codeOTP = new OTP().generatedOTP(Integer.parseInt(HDConfig.getInstance().get("OTP_LENGTH")));
            String[] params = {codeOTP, otpExpired};
            if(otpType.equals("Sign.OTP.Appendix")){
                params = new String[] {codeOTP, sMSGetOTP.getContractCode()};
            }
            String phoneNumber = sMSGetOTP.getPhoneNumber();
            //get sms template
            String message = getContentSmsTemplate(params, otpType, request.langCode());
            //save otp
            OTP otp = new OTP(phoneNumber, request.now(), "test_appid", otpType, codeOTP, Integer.parseInt(otpExpired) * 60, sMSGetOTP.getCustomerUUID(), sMSGetOTP.getContractUUID());
            oTPService.create(otp);

//        save sms
            SMS smsres = new SMS(request.now(), phoneNumber, message, codeOTP);
            getSMSGatewayHD(smsres);
//            smsres.setStatus(1);
//            sMSService.create(smsres);
//            getSMSGateway(smsres);
            //call rabbit mq, get result sms gateway and update sms
            // sMSService.sendSMS(smsres);

            //insert log customer
            CustomerLogAction customerLogAction = new CustomerLogAction();
            customerLogAction.setGetOtp(sMSGetOTP, codeOTP, phoneNumber, request.environment());
            invokeInsertLogActionCustomer(customerLogAction);
            return ok(new SMSGetOTPRespon(Integer.parseInt(otpExpired), Integer.parseInt(otpResend)));
        }
        //xem lại điều kien va in ra thông báo
        throw new BadRequestException(1235, "exceeded number of otp submissions");
    }

    /**
     * Verify otp and sending sms
     *
     * @param request object SMSVerifyOTP contain information verify otp
     *
     * @return http status code
     */
    @PostMapping("/verify_otp")
    public ResponseEntity<?> verifyOTP(@RequestBody RequestDTO<SMSVerifyOTP> request) {

        SMSVerifyOTP sMSVerifyOTP = request.init();
        logger.info("verify_otp intput:" + sMSVerifyOTP.toString());
        String codeOTP = sMSVerifyOTP.getCodeOTP();
        String otpType = sMSVerifyOTP.getOtpType();
        Date now = request.now();
        //insert log nocheck codeOTP type == 0
        CustomerLogAction customerLogAction = new CustomerLogAction();
        customerLogAction.setSMSVerifyOTP(sMSVerifyOTP, request.environment(), 1);
        invokeInsertLogActionCustomer(customerLogAction);
        List<OTPVerifyResult> list = oTPService.verifyOTP(sMSVerifyOTP);
        if (list != null && !list.isEmpty()) {
            for (OTPVerifyResult otpVerifyResult : list) {
                OTP otp = oTPService.findByUUID(otpVerifyResult.getUuid());
                if (otp != null) {
                    if (otpVerifyResult.getOtp_code().equals(codeOTP)) {
                        Long createdAt = otpVerifyResult.getCreatedAt().getTime() + otpVerifyResult.getProcess_time() * 1000;
                        Long currentDate = otpVerifyResult.getCurrentDate().getTime();
                        if (createdAt >= currentDate) {
                            logger.info("-------"+otpType);
                            // OK
                            otp.setStatus(1);
                            //register ok --> send sms
                            if (otpType.equals("AccVeri")) {
                                invokeUpateCustomer(sMSVerifyOTP.getCustomerUUID());
                                // verifyOTPRegisterSuccess(sMSVerifyOTP.getCustomerUUID(), otp.getPhone(), codeOTP, now, request.langCode());
                            }
                            //esign send rabbit mq (IsSigned =1)
                            if (otpType.equals("SignOTP")) {
                                //send sms
                                signOTPSendSMS(otp.getPhone(), codeOTP, sMSVerifyOTP.getContractCode(), String.valueOf(sMSVerifyOTP.getCustomerUUID()), now, request.langCode());
                                //
                                ContractEsignedRequest contractEsignedRequest = new ContractEsignedRequest(sMSVerifyOTP.getContractUUID(), sMSVerifyOTP.getCustomerUUID(), otp.getPhone(), 1, codeOTP, now, "");
                                sMSService.mqVerifyOTPTypeEsign(contractEsignedRequest);

                                String contractCode = sMSVerifyOTP.getContractCode();
                                IdPayload idPayload = new IdPayload();
                                idPayload.setId(sMSVerifyOTP.getContractCode());
                                // update status contract
                                String loanType = getLoanTypeContract(contractCode);
                                if (!HDUtil.isNullOrEmpty(loanType)) {
                                    if (loanType.equals("CL") || loanType.equals("CLO") || loanType.equals("MC")) {
                                        try {
                                            ResponseDTO<Object> dto = invoker.call(urlContract + "/updateStatusContract", idPayload, new ParameterizedTypeReference<ResponseDTO<Object>>() {
                                            });
                                            if (dto == null || dto.getCode() != 200) {
                                                Log.system("Invoker Update status esign error",idPayload.toString());
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            //ký phu luc(IsSigned =2)
                            if (otpType.equals("Sign.OTP.Appendix")) {
                                logger.info(" signOTPAppendixSendSMS :");
                                otpType ="Sign.Conf.Appendix";
                                signOTPAppendixSendSMS(otpType, otp.getPhone(), codeOTP, sMSVerifyOTP.getContractCode(), now, request.langCode());
                                ContractEsignedRequest contractEsignedRequest = new ContractEsignedRequest(sMSVerifyOTP.getContractUUID(), sMSVerifyOTP.getCustomerUUID(), otp.getPhone(), 2, codeOTP, now, "");
                                sMSService.mqVerifyOTPTypeEsign(contractEsignedRequest);

                                String contractCode = sMSVerifyOTP.getContractCode();
                                IdPayload idPayload = new IdPayload();
                                idPayload.setId(contractCode);

                                // update status contract
                                String loanType = getLoanTypeContract(contractCode);
                                if (!HDUtil.isNullOrEmpty(loanType)) {
                                    if (loanType.equals("CL") || loanType.equals("CLO") || loanType.equals("MC")) {
                                        try {
                                            ResponseDTO<Object> dto = invoker.call(urlContract + "/updateStatusContractAfterDocVerify", idPayload, new ParameterizedTypeReference<ResponseDTO<Object>>() {
                                            });
                                            if (dto == null || dto.getCode() != 200) {
                                                Log.system("Invoker Update status adjustmentInfo error",idPayload.toString());
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                        } else {
                            // verify khong thanh cong
                            otp.setStatus(3);
                        }
                        otp.setModifiedAt(otpVerifyResult.getCurrentDate());
                        oTPService.update(otp);
                        //insert customer logs action type == 1
                        CustomerLogAction customerLogActionIs = new CustomerLogAction();
                        customerLogActionIs.setSMSVerifyOTP(sMSVerifyOTP, request.environment(), 2);
                        invokeInsertLogActionCustomer(customerLogActionIs);
                        //return
                        if (otp.getStatus() == 1) {
                            return ok("ok");
                        }
                        //status = 3 limit-time
                        throw new BadRequestException(1234, "verify otp failed");
                    } else {
                        //status = 2 no equals otpCode
                        otp.setStatus(2);
                        otp.setModifiedAt(otpVerifyResult.getCurrentDate());
                        oTPService.update(otp);
                    }
                }
            }
            throw new BadRequestException(1234, "verify otp failed");
        }
        throw new BadRequestException(1233, "list otp is null");
    }

    /**
     * Convert contractType to smsType, get smsTemplate and sending sms
     *
     * @param phoneNumber
     * @param codeOTP
     * @param contractCode
     * @param customerUuid
     * @param now
     * @param langCode
     *
     * @return no return
     */
    private void signOTPSendSMS(String phoneNumber, String codeOTP, String contractCode, String customerUuid, Date now, String langCode) {
        String contractType = invokeGetContractType(contractCode);
        ContractInfoSignOTP contractInfo = invokeGetContractInfo(new ContractInfoRequest(customerUuid, contractCode));
        String smsType = "";
        String[] params = {};
        switch (contractType) {
            case "CL":
            case "CLO":
                smsType = "Sign.Conf.Bank";
                if (contractInfo != null) {
                    String accNo = contractInfo.getAccountNumber();
                    String accName = contractInfo.getBankName();
                    params = new String[]{contractCode, accNo == null ? "" : accNo, accName == null ? "" : accName};
                }
                break;
            case "MC":
            case "ED":
                smsType = "Sign.Conf";
                params = new String[]{contractCode};
                break;
//            case "CLO":
//                smsType = "?";
//                params = new String[]{"đang chờ cập nhật"};
//                break;
            default:
                break;
        }
        String message = getContentSmsTemplate(params, smsType, langCode);
        //send sms
        SMS smsres = new SMS(now, phoneNumber, message, codeOTP);
        getSMSGatewayHD(smsres);
    }

    /**
     * otpType contentValue signOTPAppendix, get smsTemplate and sending sms
     *
     * @param smsType
     * @param phoneNumber
     * @param codeOTP
     * @param contractCode
     * @param now
     * @param langCode
     *
     * @return no return
     */
    private void signOTPAppendixSendSMS(String smsType, String phoneNumber, String codeOTP, String contractCode, Date now, String langCode) {

        if (contractCode.trim().equals("") || codeOTP.trim().equals("")) {
            throw new BadRequestException(1248, "invalid params");
        }
        String[] params = {contractCode};
        String message = getContentSmsTemplate(params, smsType, langCode);
        logger.info(" signOTPAppendixSendSMS :" +message);
        //send sms
        SMS smsRes = new SMS(now, phoneNumber, message, codeOTP);
        getSMSGatewayHD(smsRes);
    }

    /**
     * Invoke configStaff get contractType
     *
     * @param contractCode
     *
     * @return contractType
     */
    private String invokeGetContractType(String contractCode) {
        try {
            IdPayload idPayload = new IdPayload();
            idPayload.setId(contractCode);
            ResponseDTO<String> rs = invoker.call(urlConfigStaff + "/get_kind_offer", idPayload, new ParameterizedTypeReference<ResponseDTO<String>>() {
            });
            if (rs == null || rs.getCode() != 200 || rs.getPayload() == null) {
                throw new BadRequestException(1249, "get contract type error");
            }
            return rs.getPayload();
        } catch (Exception e) {
            throw new BadRequestException(1249, "get contract type error");
        }
    }

    /**
     * Invoke ConfigContractTypeBackground get loanType
     *
     * @param contractCode
     *
     * @return loanType
     */
    private String getLoanTypeContract(String contractCode){
        Invoker invoker = new Invoker();
        String loanType = "";
        ObjectMapper mapper = new ObjectMapper();
        IdPayload idPayload = new IdPayload();
        idPayload.setId(contractCode);
        List<ConfigContractTypeBackground> list = new ArrayList<>();
        try {
            ResponseDTO<Object> dto = invoker.call(urlContractTypeBackground + "/find", idPayload, new ParameterizedTypeReference<ResponseDTO<Object>>() {
            });
            if (dto != null && dto.getCode() == 200) {

                list = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<List<ConfigContractTypeBackground>>() {
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0) {
            loanType = list.get(0).getContractType();
        }
        return loanType;
    }

    /**
     * Invoke Contract get ContractInfo
     *
     * @param contractInfoRequest contain information bank
     *
     * @return ContractInfoSignOTP
     */
    private ContractInfoSignOTP invokeGetContractInfo(ContractInfoRequest contractInfoRequest) {
        ContractInfoSignOTP result = null;
        try {
            logger.info("invokeGetContractInfo_input:" + contractInfoRequest.toString());
//            IdPayload idPayload = new IdPayload();
//            idPayload.setId(contractCode);
            ResponseDTO<ContractInfoSignOTP> rs = invoker.call(urlContract + "/getInformationBank", contractInfoRequest, new ParameterizedTypeReference<ResponseDTO<ContractInfoSignOTP>>() {
            });
//            if (rs == null || rs.getCode() != 200 || rs.getPayload() == null) {
//                throw new BadRequestException(1250, "get contract info error");
//            }
            result = rs.getPayload();
            if (result == null) {
                result = new ContractInfoSignOTP();
            }

            logger.info("invokeGetContractInfo_output:" + result.toString());
            return result;
        } catch (Exception e) {
//            throw new BadRequestException(1250, "get contract info error");
        }
        return result;
    }

    /**
     * Invoke Customer update status
     *
     * @param customerUUID
     *
     * @return rs.getPayload()
     */
    private String invokeUpateCustomer(UUID customerUUID) {
        try {
            IdPayload idPayload = new IdPayload();
            idPayload.setId(customerUUID);
            ResponseDTO<String> rs = invoker.call(urlCustomer + "/enable", idPayload, new ParameterizedTypeReference<ResponseDTO<String>>() {
            });
            if (rs == null || rs.getCode() != 200 || rs.getPayload() == null) {
                throw new BadRequestException(1240, "update customer error");
            }
            return rs.getPayload();
        } catch (Exception e) {
            throw new BadRequestException(1240, "update customer error");
        }
    }

    /**
     * Get content smsTemplate
     *
     * @param param
     * @param langCode
     * @param smsType
     *
     * @return contentSMSTemplate
     */
    private String getContentSmsTemplate(String[] param, String smsType, String langCode) {
        SMSTemplate sTemplate = sMSTemplateService.findByTypeAndLangCode(smsType, langCode);
        String message = "";
        if (sTemplate != null) {
            String content = sTemplate.getContent();
            if (param.length != 0) {
                message = getMessage(content, param);
            } else {
                message = content;
            }
            return message;
        }
        throw new BadRequestException(1215, "SMSTemplate is null");
    }

    /**
     * Check limit otp (1h and 24h)
     *
     * @param customerUUID
     *
     * @return OTPLimitRespon
     */
    private boolean checkLimitOTP(String customerUUID) {
        OTPLimitRespon otpLimitRespon = oTPService.getLimitOTP(customerUUID);
        //amount of time(<=10) ! (>10)
        if (otpLimitRespon.getV_cnt_brief() > 10) {
            throw new BadRequestException(1243, "Limit otp for a while");
        }
        //24h (<15) ! (>=15)
        if (otpLimitRespon.getV_cnt_24h() >= 15) {
            throw new BadRequestException(1244, "Limit otp sending in 24 hours");
        }
        return otpLimitRespon.getV_cnt_brief() <= 10 && otpLimitRespon.getV_cnt_24h() < 15;
    }

    /**
     * Get message fill params to content
     *
     * @param content
     * @param params
     *
     * @return message
     */
    private String getMessage(String content, String[] params) {
        try {
            return MessageFormat.format(content, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * Invoke configStaff get value
     *
     * @param configStaffGetValue information contain key
     *
     * @return value
     */
    private String getValue(ConfigStaffGetValue configStaffGetValue) {
        try {
            ResponseDTO<String> rs = invoker.call(urlConfigStaff + "/get_value", configStaffGetValue, new ParameterizedTypeReference<ResponseDTO<String>>() {
            });
            if (rs == null || rs.getCode() != 200 || rs.getPayload() == null) {
                throw new BadRequestException(1232, "get value error");
            }
            return rs.getPayload();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        throw new BadRequestException(1232, "get value error");
    }

    /**
     * Invoke customer insert customerLogAction
     *
     * @param customerLogAction
     *
     * @return no return
     */
    //insert log action customer
    private void invokeInsertLogActionCustomer(CustomerLogAction customerLogAction) {
        try {
            ResponseDTO<String> rs = invoker.call(urlCustomer + "/log_action/create", customerLogAction, new ParameterizedTypeReference<ResponseDTO<String>>() {
            });
        } catch (Exception e) {
            //throw new BadRequestException(1245, "insert log customer error");
            logger.info("insert log customer error: "+e.getMessage());
        }
    }

    //    private void signConfAppendixSendSMS(String smsType, String phoneNumber, String codeOTP, String contractCode, Date now, String langCode) {
//        if (contractCode.trim().equals("")) {
//            throw new BadRequestException(1248, "invalid params");
//        }
//        String[] params = {contractCode};
//        String message = getContentSmsTemplate(params, smsType, langCode);
//        //send sms
//        SMS smsres = new SMS(now, phoneNumber, message, codeOTP);
//        getSMSGatewayHD(smsres);
//    }

//    private void verifyOTPRegisterSuccess(UUID customerUUID, String phoneNumber, String codeOTP, Date now, String langCode) {
//        //update customer
//        invokeUpateCustomer(customerUUID);
//        List<String> list = new ArrayList<>();
//        list.add(userName);
//        String[] param = list.toArray(new String[list.size()]);
//        String message = getContentSmsTemplate(param, "AccSuccess", langCode);
//        //send sms
//        SMS smsres = new SMS(now, phoneNumber, message, codeOTP);
//        smsres.setStatus(1);
//        sMSService.create(smsres);
//        getSMSGateway(smsres);
//        getSMSGatewayHD(smsres);
//    }
/*
    @PostMapping(value = "/test")
    public ResponseEntity<?> test(@RequestBody RequestDTO<EmptyPayload> request) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmailType("loan_form");
        emailRequest.setLangCode("vi");
        emailRequest.setListEmail(new String[]{"cdcongminh1@gmail.com","minhnc@tinhvan.com"});
        emailRequest.setListFile(new String[]{"https://hdsaison-static.s3.ap-southeast-1.amazonaws.com/contract/6c0db35e-874a-4fed-9c96-9be21be17bcd/CL/HD_9c2b5397-35fe-4d14-9e8d-ade4a188f86d_20191123.pdf","https://hdsaison-static.s3.ap-southeast-1.amazonaws.com/contract/6c0db35e-874a-4fed-9c96-9be21be17bcd/CL/HD_9c2b5397-35fe-4d14-9e8d-ade4a188f86d_20191123.pdf"});

        invokeEmail(emailRequest);
//        IdPayload idPayload = request.init();
//        oTPService.findByUUID(UUID.fromString(idPayload.getId().toString()));
//        sMSTemplateService.findByTypeAndLangCode("Acc.Reset.Pass", "vi");
        return ok();
    }

    //insert log action customer
    private void invokeEmail(EmailRequest object) {
        try {
//            http://qlda.tinhvan.com/dev/hdsaison/lendingapp
            //http://localhost:8812
            ResponseDTO<String> rs = invoker.call("http://qlda.tinhvan.com/dev/hdsaison/lendingapp/api/v1/email/send_s3", object, new ParameterizedTypeReference<ResponseDTO<String>>() {
            });
            if (rs==null || rs.getCode() != 200 || rs.getPayload() == null) {
                throw new BadRequestException(1245, "invoke email error");
            }
        } catch (Exception e) {
            throw new BadRequestException(1245, "invoke email error");
        }
    }
    */
/*
    private void getSMSGateway(SMS smsres) {
//        smsres.getPhone()
        Config config = HDConfig.getInstance();
        String[] phoneOtps = config.getList("PHONE_OTP");
        List<String> listPhone = Arrays.asList(phoneOtps);
//        smsres.setPhone(phone);
        if (listPhone != null && !listPhone.isEmpty()) {
            for (String phone : listPhone) {
                StringBuilder builder = new StringBuilder(urlSMSGateway);
                builder.append("?phoneNumber=" + phone);
                builder.append("&content=" + smsres.getMessage());

                builder.append("&brandName=Aeon VN");
                builder.append("&apiKey=a585787f-b27a-44ff-88e6-0b06426719a0");
                builder.append("&secretKey=033695f3-e472-4729-9d98-796ecc9e9a92");

                RestTemplate restTemplate = new RestTemplate();
                try {
                    ResponseEntity<String> result = restTemplate.getForEntity(builder.toString(), String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    */
}
