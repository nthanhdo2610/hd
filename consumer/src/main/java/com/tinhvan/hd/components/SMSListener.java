package com.tinhvan.hd.components;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.tinhvan.hd.base.Config;
import com.tinhvan.hd.base.HDConfig;
import com.tinhvan.hd.base.Invoker;
import com.tinhvan.hd.base.ResponseDTO;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.entity.SMS;
import com.tinhvan.hd.service.SMSService;
import com.tinhvan.hd.vo.ContractEsignedRequest;
import com.tinhvan.hd.vo.ContractEsignedRespon;
import com.tinhvan.hd.vo.SMSVerifyOTP;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SMSListener {
    @Autowired
    SMSService sMSService;

    @Value("${app.module.sms_gateway.service.url}")
    private String urlSMSGateway;

    @Value("${app.module.contract_e_signed.service.url}")
    private String urlContractEsigned;

    Invoker invoker = new Invoker();

    // Create a Logger
    Logger logger = Logger.getLogger(SMSListener.class.getName());

    @RabbitListener(queues = RabbitConfig.QUEUE_SEND_SMS_QUEUE)
    public void recievedMessage(SMS smsres) {
        try {
            //getlist by status = 0 limit = 50
            List<SMS> list = sMSService.getList(50);
            if (list != null && !list.isEmpty()) {
                for (SMS sms : list) {
                    if(sms.getStatus()==0) {
                        sms.setStatus(1);
                        sms.setModifiedAt(new Date());
                        SMS smsResult = sMSService.createOrUpdate(sms);
                        if (smsResult.getStatus() == 1) {
                            //call sms gateway
                            getSMSGateway(smsResult);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
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
