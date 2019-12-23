/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.controller;

import com.tinhvan.hd.base.HDConfig;
import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.email.bean.ContentEmail;
import com.tinhvan.hd.email.model.EmailTemplate;
import com.tinhvan.hd.email.rabbitmq.EmailRequest;
import com.tinhvan.hd.email.service.EmailService;
import com.tinhvan.hd.email.service.EmailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;

/**
 * @author LUUBI
 */
@RestController
@RequestMapping("api/v1/email")
public class EmailController extends HDController {

    @Autowired
    EmailService emailService;

    @Autowired
    EmailTemplateService emailTemplateService;

    // Create a Logger
    Logger logger = Logger.getLogger(EmailController.class.getName());

//    @PostMapping(value = "/send_s3")
//    public ResponseEntity<?> sendS3(@RequestBody RequestDTO<EmailResponse> request) {
//     EmailResponse emailResponse = request.init();
//     emailResponse.setFileType("s3");
//     emailResponse.setUserName(HDConfig.getInstance().get("MAIL_USERNAME"));
//     emailResponse.setPassword(HDConfig.getInstance().get("MAIL_PASSWORD"));
//     emailResponse.setMailFrom(HDConfig.getInstance().get("MAIL_MAIL_FROM"));
//     emailService.sendEmail(emailResponse);
//     return ok();
//    }

    /**
     * send mail attachment file not s3
     *
     * @param request object EmailRequest contain send email
     * @return http status code
     */
    @PostMapping(value = "/send")
    public ResponseEntity<?> send(@RequestBody RequestDTO<EmailRequest> request) {
        try {
            EmailRequest emailRequest = request.init();
            logger.info("/send intput: " + emailRequest.toString());
            setValueConfig(emailRequest);
            emailService.sendEmail(emailRequest);
            logger.info("/send output: " + emailRequest.toString());
            return ok();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return badRequest();
    }

    /**
     * send mail attachment file s3
     *
     * @param request object EmailRequest contain send email
     * @return http status code
     */
    @PostMapping(value = "/send_s3")
    public ResponseEntity<?> test(@RequestBody RequestDTO<EmailRequest> request) {
        EmailRequest emailRequest = request.init();
        logger.info("/send_s3_input: " + emailRequest.toString());
        emailRequest.setFileType("s3");
        setValueConfig(emailRequest);
        emailService.sendEmail(emailRequest);
//        SendMail sendMail = new SendMail(emailRequest.getListEmail(), emailRequest.getListFile(),
//                title, emailRequest.getContent());
//        sendMail.sendMsg();
        logger.info("/send_s3_output: " + emailRequest.toString());
        return ok();
    }

    /**
     * set value config email
     *
     * @param emailRequest object EmailRequest contain send email
     * @return no result
     */
    private void setValueConfig(EmailRequest emailRequest) {
        emailRequest.setUserName(HDConfig.getInstance().get("MAIL_USERNAME"));
        emailRequest.setPassword(HDConfig.getInstance().get("MAIL_PASSWORD"));
        emailRequest.setMailFrom(HDConfig.getInstance().get("MAIL_MAIL_FROM"));
        List<String> params = null;
        if (emailRequest.getParams() != null) {
            params = Arrays.asList(emailRequest.getParams());
        }
        EmailTemplate emailTemplate = emailTemplateService.findByEmailTypeAndAndLangCodeAndStatus(
                emailRequest.getEmailType(), emailRequest.getLangCode(), 1);
        ContentEmail contentEmail = null;
        if (emailTemplate != null) {
            contentEmail = convertContentEmail(emailTemplate.getEmailType(), emailTemplate.getTitle(), emailTemplate.getContent(), params, emailRequest.getFileType());
        }

        emailRequest.setContent(contentEmail.getContent());
        emailRequest.setTitle(contentEmail.getTitle());
        emailRequest.setFileType(contentEmail.getFileType());
    }

    /**
     * convert content email to send mail
     *
     * @param emailType
     * @param content
     * @param list
     * @param fileType
     *
     * @return content email
     */
    private ContentEmail convertContentEmail(String emailType, String title, String content, List<String> list, String fileType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'_'HH:mm:ss");
        StringJoiner join = new StringJoiner("_");
        ContentEmail contentEmail = new ContentEmail();
        String titleResult = title;
        String contentResult = content;
        switch (emailType) {
            case "esign_to_customer":
            case "adj_to_customer":
            case "adj_to_si":
            case "esignadj_to_customer":
                if (list != null && !list.isEmpty() && list.size() >= 2) {
                    titleResult = title.replace("[customerName]", list.get(0) ).replace("[contractCode]", list.get(1));
                    contentResult = content.replace("[customerName]", "<b>" + list.get(0) + "</b>").replace("[contractCode]", "<b>" + list.get(1) + "</b>");
                }
                break;
            case "esign_to_si":
                if (list != null && !list.isEmpty() && list.size() >= 5) {
                    titleResult = title.replace("[contractCode]", list.get(0) )
                            .replace("[customerName]", list.get(1));
                    contentResult = content.replace("[contractCode]", "<b>" + list.get(0) + "</b>")
                            .replace("[customerName]", "<b>" + list.get(1) + "</b>")
                            .replace("[cmnd]", "<b>" + list.get(2) + "</b>")
                            .replace("[loan]", "<b>" + list.get(3) + "</b>")
                            .replace("[productName]", "<b>" + list.get(4) + "</b>");
                }
                break;
            case "esign_to_accountant":
                join.add("HDSaison_Danh_Sach_Giai_Ngan_Tien_Mat");
                break;
            case "adj_to_it":
                join.add("HDSaison_Thong_tin_dieu_chinh");
                break;
            case "upload_adj_to_it":
                break;
            case "loan_form":
                join.add("HDSaison_Phan_Bo_Ho_So_Dang_Ky_Vay");
                break;
            case "signup_promotion":
                join.add("HDSaison_Phan_Bo_Ho_So_Dang_Ky_Vay_Uu_Dai");
                break;
            default:
                break;
        }
        if (!HDUtil.isNullOrEmpty(fileType))
            join.add(fileType);
        join.add(simpleDateFormat.format(new Date()));
        contentEmail.setFileType(join.toString());
        contentEmail.setTitle(titleResult);
        contentEmail.setContent(contentResult);
        return contentEmail;
    }

}
