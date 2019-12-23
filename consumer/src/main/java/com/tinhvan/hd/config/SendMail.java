/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.Invoker;
import com.tinhvan.hd.base.ResponseDTO;
import com.tinhvan.hd.entity.LogMail;
import com.tinhvan.hd.service.LogMailService;
import com.tinhvan.hd.vo.EmailResponse;
import com.tinhvan.hd.vo.UriRequest;
import com.tinhvan.hd.vo.UriResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author LUUBI
 */
public class SendMail {

    @Autowired
    LogMailService logMailService;
    //    @Value("${app.module.filehandler.service.endpoint}")
    private String urlFileHandlerRequest = "http://192.168.75.205:8000/api/v1/filehandler";

    // send mail
    private String[] listEmailTo;
    private String title;
    private String[] listFile;
    private String content;
    private String userName;
    private String password;
    private String mailFrom;
    private String fileType;

    public SendMail(EmailResponse object) {
        this.listEmailTo = object.getListEmail();
        this.listFile = object.getListFile();
        this.title = object.getTitle();
        this.content = object.getContent();
        this.userName = object.getUserName();
        this.password = object.getPassword();
        this.mailFrom = object.getMailFrom();
        this.fileType = object.getFileType();
    }

    public boolean sendMsg() {
        //write log mail
        LogMail log = new LogMail();
        try {
            Properties props = new Properties();
            Session session = null;
            props.put("mail.smtp.host", "mail.hdsaison.com.vn");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.from", mailFrom);
            javax.mail.Authenticator authenticator = new javax.mail.Authenticator() {
                @Override
                public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(userName, password);
                }
            };
            session = Session.getDefaultInstance(props, authenticator);
            // debug
            session.setDebug(false);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mailFrom, "Công ty tài chính HD Saison"));

            // addd mail to
            if (listEmailTo != null) {
                int numListTo = listEmailTo.length;
                InternetAddress[] arrAddressTo = new InternetAddress[numListTo];
                for (int i = 0; i < numListTo; i++) {
                    arrAddressTo[i] = new InternetAddress(listEmailTo[i], listEmailTo[i]);
                }
                msg.addRecipients(Message.RecipientType.TO, arrAddressTo);
            }

            // add subject
            msg.addHeader("EmailHeader", title);
            msg.setSentDate(Calendar.getInstance().getTime());
            msg.setSubject(title);
            // ad body
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html; charset=UTF-8");
//            messageBodyPart.setContent(mailTemplate(content), "text/html; charset=UTF-8");
            Multipart multipart = new MimeMultipart();
            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            // Part two is attachment
            if(listFile != null && listFile.length != 0){
            sendFile(messageBodyPart, listFile, multipart, fileType);
            }
            // Send the complete message parts
            msg.setContent(multipart);
            // Send message
            Transport.send(msg);

            log.setTitle(title);
            log.setContent(content);
            log.setMailTo(listEmailTo.toString());
            if(listFile != null){
            log.setIsFileAttachment(listFile.length);
            }
            log.setIsSent(1);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.setIsSent(0);
            log.setError(ex.getMessage());
        }

        logMailService.create(log);

        return false;
    }

    private void sendFile(BodyPart bodyPart, String[] listFile, Multipart multipart, String fileType) throws Exception {
        List<String> list = Arrays.asList(listFile);
        if (list != null && !list.isEmpty()) {
            if (!fileType.equals("s3")) {
                for (String url : list) {
                    bodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(url);
                    bodyPart.setDataHandler(new DataHandler(source));
                    bodyPart.setFileName(url);
                    multipart.addBodyPart(bodyPart);
                }
            } else {
                //file s3
                for (String url : list) {
                    File file = invokeFileHandlerS3_download(new UriRequest(url));
                    if (file != null) {
                        bodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(file.getAbsoluteFile());
                        bodyPart.setDataHandler(new DataHandler(source));
                        bodyPart.setFileName(file.getName());
                        multipart.addBodyPart(bodyPart);
                    }
                }
            }
        }

    }

    private File invokeFileHandlerS3_download(UriRequest uriRequest) throws Exception {
        Invoker invoker = new Invoker();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/download", uriRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                try {
                    UriResponse fileResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<UriResponse>() {
                            });
                    if (!HDUtil.isNullOrEmpty(fileResponse.getData())) {
                        String fileNameIn = "HD_REPORT_" + UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + "." + FilenameUtils.getExtension(uriRequest.getUri());
                        FileUtils.writeByteArrayToFile(new File(fileNameIn), Base64.getDecoder().decode(fileResponse.getData()));
                        return new File(fileNameIn);
                    }
                } catch (IOException e) {
                    throw new IOException(e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return null;
    }

    private String mailTemplate(String msg) {
        return "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head>\n"
                + "	<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\n"
                + "  	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0;\">\n"
                + " 	<meta name=\"format-detection\" content=\"telephone=no\"/>\n"
                + "\n"
                + "	<style>\n"
                + "/ Reset styles / \n"
                + "body { margin: 0; padding: 0; min-width: 100%; width: 100% !important; height: 100% !important;}\n"
                + "body, table, td, div, p, a { -webkit-font-smoothing: antialiased; text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; line-height: 100%; }\n"
                + "table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse !important; border-spacing: 0; }\n"
                + "img { border: 0; line-height: 100%; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic; }\n"
                + "#outlook a { padding: 0; }\n"
                + ".ReadMsgBody { width: 100%; } .ExternalClass { width: 100%; }\n"
                + ".ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div { line-height: 100%; }\n"
                + "/ Rounded corners for advanced mail clients only / \n"
                + "@media all and (min-width: 560px) {\n"
                + "	.container { border-radius: 8px; -webkit-border-radius: 8px; -moz-border-radius: 8px; -khtml-border-radius: 8px;}\n"
                + "}\n"
                + "/ Set color for auto links (addresses, dates, etc.) / \n"
                + "a, a:hover {\n"
                + "	color: #127DB3;\n"
                + "}\n"
                + ".footer a, .footer a:hover {\n"
                + "	color: #999999;\n"
                + "}\n"
                + " 	</style>\n"
                + "\n"
                + "	<title>Mail template</title>\n"
                + "\n"
                + "</head>\n"
                + "\n"
                + "<body topmargin=\"0\" rightmargin=\"0\" bottommargin=\"0\" leftmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" width=\"100%\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; width: 100%; height: 100%; -webkit-font-smoothing: antialiased; text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; line-height: 100%;\n"
                + "	background-color: #F0F0F0;\n"
                + "	color: #000000;\"\n"
                + "	bgcolor=\"#F0F0F0\"\n"
                + "	text=\"#000000\">\n"
                + "\n"
                + "<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; width: 100%;\" class=\"background\"><tr><td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0;\"\n"
                + "	bgcolor=\"#F0F0F0\">\n"
                + "\n"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"\n"
                + "	width=\"560\" style=\"border-collapse: collapse; border-spacing: 0; padding: 0; width: inherit;\n"
                + "	max-width: 560px;\" class=\"wrapper\">\n"
                + "\n"
                + "	<tr>\n"
                + "		<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%;\n"
                + "			padding-top: 20px;\n"
                + "			padding-bottom: 20px;\"> \n"
                + "			\n"
                + "			<div style=\"display: none; visibility: hidden; overflow: hidden; opacity: 0; font-size: 1px; line-height: 1px; height: 0; max-height: 0; max-width: 0;\n"
                + "				color: #F0F0F0;\" class=\"preheader\"></div>\n"
                + "			\n"
                + "		</td>\n"
                + "	</tr>\n"
                + "\n"
                + "</table>\n"
                + "\n"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"\n"
                + "	bgcolor=\"#FFFFFF\"\n"
                + "	width=\"560\" style=\"border-collapse: collapse; border-spacing: 0; padding: 0; width: inherit;\n"
                + "	max-width: 560px;\" class=\"container\">\n"
                + "\n"
                + "	<tr>\n"
                + "		<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%; font-size: 24px; font-weight: bold; line-height: 130%;\n"
                + "			padding-top: 25px;\n"
                + "			color: #000000;\n"
                + "			font-family: sans-serif;\" class=\"header\">\n"
                + "				<img src=\"https://www.hdsaison.com.vn/templates/hdf/images/hdf-logo.png\" alt=\"Smiley face\" width=\"300\" height=\"150\">\n"
                + "		</td>\n"
                + "	</tr>\n"
                + "\n"
                + "	<tr>\n"
                + "		<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%;\n"
                + "			padding-top: 25px;\" class=\"line\"><hr\n"
                + "			color=\"#E0E0E0\" align=\"center\" width=\"100%\" size=\"1\" noshade style=\"margin: 0; padding: 0;\" />\n"
                + "		</td>\n"
                + "	</tr>\n"
                + "	\n"
                + "	<tr>\n"
                + "		<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%; font-size: 17px; font-weight: 400; line-height: 160%;\n"
                + "			padding-top: 25px; \n"
                + "			color: #000000;\n"
                + "			font-family: sans-serif;\" class=\"paragraph\">\n"
                + "				" + msg + "\n"
                + "		</td>\n"
                + "	</tr>\n"
                + "\n"
                + "	<tr>	\n"
                + "		<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%;\n"
                + "			padding-top: 25px;\" class=\"line\"><hr\n"
                + "			color=\"#E0E0E0\" align=\"center\" width=\"100%\" size=\"1\" noshade style=\"margin: 0; padding: 0;\" />\n"
                + "		</td>\n"
                + "	</tr>\n"
                + "\n"
                + "	<tr>\n"
                + "		<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%; font-size: 17px; font-weight: 400; line-height: 160%;\n"
                + "			padding-top: 20px;\n"
                + "			padding-bottom: 25px;\n"
                + "			color: #000000;\n"
                + "			font-family: sans-serif;\" class=\"paragraph\">\n"
                + "				Liên hệ: <a>minhnc@tinhvan.com</a>\n"
                + "		</td>\n"
                + "	</tr>\n"
                + "\n"
                + "</table>\n"
                + "\n"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"\n"
                + "	width=\"560\" style=\"border-collapse: collapse; border-spacing: 0; padding: 0; width: inherit;\n"
                + "	max-width: 560px;\" class=\"wrapper\">\n"
                + "\n"
                + "	<tr>\n"
                + "		<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%; font-size: 13px; font-weight: 400; line-height: 150%;\n"
                + "			padding-top: 20px;\n"
                + "			padding-bottom: 20px;\n"
                + "			color: #999999;\n"
                + "			font-family: sans-serif;\" class=\"footer\">\n"
                + "			HCMC Branch:\n"
                + "\n"
                + "2th Floor – F2, Mirae Business Center\n"
                + "\n"
                + "268 To Hien Thanh St.\n"
                + "\n"
                + "Ward 15, Dist. 10, HCMC\n"
                + "\n"
                + "T: +84 8 38680888\n"
                + "\n"
                + "T: +84 8 38623866\n"
                + "		</td>\n"
                + "	</tr>\n"
                + "\n"
                + "</table>\n"
                + "\n"
                + "</td></tr></table>\n"
                + "\n"
                + "</body>\n"
                + "</html>";

    }
}
