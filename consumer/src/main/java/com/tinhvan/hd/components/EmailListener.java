package com.tinhvan.hd.components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.entity.LogMail;
import com.tinhvan.hd.service.LogMailService;
import com.tinhvan.hd.vo.EmailResponse;
import com.tinhvan.hd.vo.UriRequest;
import com.tinhvan.hd.vo.UriResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Component
public class EmailListener {

    @Autowired
    LogMailService logMailService;
    // Create a Logger
    Logger logger = Logger.getLogger(EmailListener.class.getName());
    @Value("${app.module.filehandler.service.endpoint}")
    private String urlFileHandlerRequest;
/*
    @RabbitListener(queues = RabbitConfig.QUEUE_SEND_EMAIL_QUEUE)
    public void recievedMessage(EmailResponse object) {
        try {
            if (object != null) {
                logger.info("recievedMessage:" + object.toString());
                sendMsg(object);
            }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }
*/
    private boolean sendMsg(EmailResponse object) {
        Config config = HDConfig.getInstance();
        String[] listEmailConfig = config.getList("EMAIL_SEND");
        String[] listEmailTo = object.getListEmail();
        String title = object.getTitle();
        String[] listFile = object.getListFile();
        String content = object.getContent();
        String userName = object.getUserName();
        String password = object.getPassword();
        String mailFrom = object.getMailFrom();
        String fileType = object.getFileType();
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
                String email = "";
                for (int i = 0; i < numListTo; i++) {
                    email += listEmailTo[i] + ";";
                }
                log.setMailTo(email);

                //send mail local
                if (listEmailConfig != null) {
                    listEmailTo = listEmailConfig;
                }
                int numList = listEmailTo.length;
                InternetAddress[] arrAddressTo = new InternetAddress[numList];
                for (int i = 0; i < numList; i++) {
//                    email += listEmailTo[i]+";";
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
            if (listFile != null && listFile.length != 0) {
                sendFile(messageBodyPart, listFile, multipart, fileType, title);
            }
            // Send the complete message parts
            msg.setContent(multipart);
            // Send message
            Transport.send(msg);

            log.setTitle(title);
//            log.setContent(content);

            if (listFile != null) {
                log.setIsFileAttachment(listFile.length);
            }
            log.setIsSent(1);
            logMailService.create(log);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.setIsSent(0);
            log.setError(ex.getMessage());
            logMailService.create(log);
        }

        return false;
    }

    private void sendFile(BodyPart bodyPart, String[] listFile, Multipart multipart, String fileType, String title) throws Exception {
        try {
            List<String> list = Arrays.asList(listFile);
            if (list != null && !list.isEmpty()) {
                if (!fileType.equals("s3")) {
                    int count = 1;
                    for (String data : list) {
                        bodyPart = new MimeBodyPart();

                        byte[] base64Bytes = Base64.getDecoder().decode(data);
                        File file = null;
                        try {
                            file = new File(title + count);
                            //creating the file in the server (temporarily)
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(base64Bytes);
                            fos.close();

                            DataSource source = new FileDataSource(file);
                            bodyPart.setDataHandler(new DataHandler(source));
                            bodyPart.setFileName(title + count);
                            multipart.addBodyPart(bodyPart);
                        } catch (IOException io) {
                            //ex.printStackTrace();
                            throw new InternalServerErrorException(io.getMessage());
                        } finally {
                            //removing the file created in the server
                            if(file!=null)
                                file.delete();
                        }
                        count++;

//
//
//
//                        DataSource source = new FileDataSource(url);
//                        bodyPart.setDataHandler(new DataHandler(source));
//                        bodyPart.setFileName(url);
//                        multipart.addBodyPart(bodyPart);
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

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private File invokeFileHandlerS3_download(UriRequest uriRequest) throws Exception {
        Invoker invoker = new Invoker();
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("invokeFileHandlerS3_download_input: " + uriRequest.toString());
            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/download", uriRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            logger.info("invokeFileHandlerS3_download_output: " + dto.toString());
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
                    logger.info("invokeFileHandlerS3_download_Exception: " + uriRequest.toString());
                    throw new IOException(e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return null;
    }

    private void covertFile(List<String> list) {
        if (list != null && !list.isEmpty()) {
            for (String data : list) {
                byte[] base64Bytes = Base64.getDecoder().decode(data);
                File file = null;
                try {
                    file = new File("test.xlsx");
                    //creating the file in the server (temporarily)
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(base64Bytes);
                    fos.close();
                } catch (IOException io) {
                    //ex.printStackTrace();
                    throw new InternalServerErrorException(io.getMessage());
                } finally {
                    //removing the file created in the server
                    if(file!=null)
                    file.delete();
                }
            }
        }
    }

    private String convertNameFile(String emailType) {
        String contentResult = "";
        switch (emailType) {
            case "esign_to_customer":
                contentResult = "HD SAISON_Chứng từ điện tử";
                break;
            case "adj_to_customer":
                break;
            case "adj_to_si":
                break;
            case "esign_to_si":

                break;
            case "esign_to_accountant":
                break;
            case "adj_to_it":
                break;
            case "upload_adj_to_it":
                break;
            case "loan_form":
                break;
            default:
                break;
        }
        return contentResult;
    }
}
