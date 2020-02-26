package com.tinhvan.hd.components;


import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.Config;
import com.tinhvan.hd.base.HDConfig;
import com.tinhvan.hd.base.Invoker;
import com.tinhvan.hd.base.ResponseDTO;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.entity.SMS;
import com.tinhvan.hd.service.SMSService;
import com.tinhvan.hd.utils.EncryptionUtils;
import com.tinhvan.hd.vo.*;
import kong.unirest.Unirest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import kong.unirest.HttpResponse;

@Component
public class SMSListener {
    @Autowired
    SMSService sMSService;

    @Value("${app.module.sms_gateway.service.url}")
    private String urlSMSGateway;

    @Value("${app.module.contract_e_signed.service.url}")
    private String urlContractEsigned;

    //config account sms
    @Value("${config.key.sms_username}")
    private String smsUsername;

    @Value("${config.key.sms_password}")
    private String smsPassword;

    @Value("${config.key.sms_base_url}")
    private String baseUrl;

    Invoker invoker = new Invoker();

    // Create a Logger
    Logger logger = Logger.getLogger(SMSListener.class.getName());

    @RabbitListener(queues = RabbitConfig.QUEUE_SEND_SMS_QUEUE)
    public void receivedMessage(String test) {
        try {
            logger.info("sms_service call rabbit mq "+ test);
            List<SMS> listUpdateSMSLogs = sMSService.getListUpdateSMSLogs();
            logger.info("size sms: "+listUpdateSMSLogs.size());
            if(listUpdateSMSLogs != null && !listUpdateSMSLogs.isEmpty()){
                for(SMS s : listUpdateSMSLogs){
                    SMSLogs smsLogs = callSMSLogs(s.getMessageId());
                    if(smsLogs != null && smsLogs.getResults().size() > 0){
                        SMSLogsResults smsLogsResults = smsLogs.getResults().get(0);
                        logger.info(smsLogsResults.getStatus().getAction()+" accept logs "+s.getMessageId());
                        s.setSmsFrom(smsLogsResults.getFrom());
                        s.setSentAt(smsLogsResults.getSentAt());
                        s.setDoneAt(smsLogsResults.getDoneAt());
                        s.setSmsCount(smsLogsResults.getSmsCount());
                        sMSService.createOrUpdate(s);
                    }else{
                        logger.info("no accept logs "+s.getMessageId());
                    }
                }
            }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }

    private SMSLogs callSMSLogs(String messageId){
        //messageId=28190630803003597711
        EncryptionUtils encryptionUtils = new EncryptionUtils();
        ObjectMapper objectMapper = new ObjectMapper();
        SMSLogs smsLogs = null;
//        Map<String, String> map = new HashMap<>();
//        map.put("messageId", messageId);
        try {
            String baseURL = encryptionUtils.decrypt(baseUrl, encryptionUtils.getKey());
            String username = encryptionUtils.decrypt(smsUsername, encryptionUtils.getKey());
            String password = encryptionUtils.decrypt(smsPassword, encryptionUtils.getKey());
            String headerValue = (username + ':' + password);
            byte[] bytesEncoded = Base64.encodeBase64(headerValue.getBytes());
            String authorization = new String(bytesEncoded);
            StringJoiner joiner = new StringJoiner("");
            joiner.add(baseURL);
            joiner.add("?messageId="+messageId);
            HttpResponse<String> response = Unirest.get(joiner.toString())
                    .header("authorization", "Basic " + authorization)
                    .header("content-type", "application/json")
                    .header("accept", "application/json")
//                    .body(new JSONObject(map))
                    .asString();

            smsLogs = objectMapper.readValue(response.getBody(), SMSLogs.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return smsLogs;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_SEND_VERIFY_OTP_QUEUE)
    public void recievedMessage(ContractEsignedRequest contractEsignedRequest) {
        try {
            logger.info("input: "+contractEsignedRequest.toString());
            ResponseDTO<Object> rs = invoker.call(urlContractEsigned, contractEsignedRequest, new ParameterizedTypeReference<ResponseDTO<Object>>() {
            });
            logger.info("output: "+rs.toString());
        } catch (Exception e) {
            logger.info("error: "+e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }

    private void getSMSGateway(SMS smsres) {
        //String phone = "0988334459"; // anh Thai
        //String phone = "0706004435"; //vDN
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
                    throw new AmqpRejectAndDontRequeueException(e.getMessage());
                }
            }
        }
    }


}
