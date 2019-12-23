package com.tinhvan.hd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.*;
import com.tinhvan.hd.service.*;
import com.tinhvan.hd.utils.ContractUtils;
import com.tinhvan.hd.utils.XlsxUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@Controller
public class SchedulerSendMailController {

    @Autowired
    private ContractSendFileService contractSendFileService;

    @Autowired
    private ContractEsignedFileService eSignedFileService;

    @Autowired
    private ContractAdjustmentUploadFileService adjustmentUploadFileService;

    @Autowired
    private SendMailLogActionService sendMailLogActionService;

    @Autowired
    private HDMiddleService hdMiddleService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractAdjustmentInfoService contractAdjustmentInfoService;

    @Value("${service.email.endpoint}")
    private String urlEmailRequest;
    @Value("${service.config_staff.endpoint}")
    private String urlConfigStaffRequest;

    @Value("${config.key.customer_info_adjustment_send_time}")
    private String adjustmentSendTime;
    @Value("${config.key.customer_info_adjustment_mail_list}")
    private String adjustmentMailList;

    @Value("${config.key.contract_disbursement_send_time}")
    private String disbursementSendTime;
    @Value("${config.key.customer_info_adjustment_mail_list}")
    private String disbursementMailList;

    @Value("${config.key.loan_application_MC_mail_list}")
    private String loanMCMailList;
    @Value("${config.key.loan_application_ED_mail_list}")
    private String loanEDMailList;
    @Value("${config.key.loan_application_cashloan_mail_list}")
    private String cashLoanMailList;
    @Value("${config.key.loan_sign_send_time}")
    private String loanSendTime;

    @Value("${config.key.signup_promotion_MC_mail_list}")
    private String loanMCPromotionMailList;
    @Value("${config.key.signup_promotion_ED_mail_list}")
    private String loanEDPromotionMailList;
    @Value("${config.key.signup_promotion_CL_mail_list}")
    private String cashLoanPromotionMailList;
    @Value("${config.key.signup_promotion_send_time}")
    private String signUpPromotionSendTime;

    private Locale locale = new Locale("vn", "VN");
    private NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    /**
     * Cron-task auto check contract files need to send mail to SI and Customer
     */
    @Scheduled(cron = "${scheduled.cron.send_mail}", zone = "Asia/Bangkok")
    void sendContractFileToSiAndCustomer() {
        List<ContractSendFile> contractSendFiles = contractSendFileService.findSend();
        if (contractSendFiles != null && contractSendFiles.size() > 0) {
            for (ContractSendFile contractSendFile : contractSendFiles) {
                if (HDUtil.isNullOrEmpty(contractSendFile.getMailType()))
                    continue;

                Contract contract = contractService.getById(contractSendFile.getContractUuid());
                if (contract == null) {
                    continue;
                }
                ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contract.getLendingCoreContractId());
                //ContractInfo contractInfo = contractEsignedRequest.getContractInfo();
                if (contractInfo == null) {
                    continue;
                }
                SendMailLogAction sendMailLogAction = new SendMailLogAction();
                sendMailLogAction.setCreatedAt(new Date());
                String fileType = "";
                if (contractSendFile.getIsSignedAdjustment() == Contract.SIGN_TYPE.E_SIGN) {
                    fileType = Contract.FILE_TYPE.E_SIGN;
                    sendMailLogAction.setName("E_SIGN");
                }
                if (contractSendFile.getIsSignedAdjustment() == Contract.SIGN_TYPE.ADJUSTMENT) {
                    fileType = Contract.FILE_TYPE.ADJUSTMENT;
                    sendMailLogAction.setName("ADJUSTMENT");
                }
                //sendmail request
                EmailRequest emailRequest = new EmailRequest();
                List<String> emails = new ArrayList<>();
                emails.add(contractSendFile.getEmail());
                emailRequest.setEmailType(contractSendFile.getMailType());
                emailRequest.setListEmail(emails.toArray(new String[emails.size()]));
                emailRequest.setLangCode("vi");
                if (contractSendFile.getMailType().equals("esign_to_si")) {
                    emailRequest.setParams(Arrays.asList(
                            contractInfo.getContractNumber(),
                            ContractUtils.generateFullName(contractInfo),
                            contractInfo.getNationalID(),
                            numberFormatter.format(contractInfo.getLoanAmount()),
                            contractInfo.getProductName())
                            .toArray(new String[5]));
                } else {
                    List<String> attachments = eSignedFileService.getFile(contractSendFile.getContractUuid(), fileType);
                    if (attachments != null && attachments.size() > 0) {
                        contractSendFile.setFileName(attachments.toString());
                        emailRequest.setListFile(attachments.toArray(new String[attachments.size()]));
                    }
                    emailRequest.setParams(Arrays.asList(
                            ContractUtils.generateFullName(contractInfo),
                            contractInfo.getContractNumber())
                            .toArray(new String[2]));
                }
                sendMailLogAction.setContent(emailRequest.toString());
                if (invokeEmailService_sendEmail_Success(emailRequest, sendMailLogAction, "/send_s3")) {
                    contractSendFile.setStatus(1);
                    contractSendFile.setEmail(HDUtil.formatEmailSave(contractSendFile.getEmail()));
                    contractSendFileService.update(contractSendFile);
                }
                sendMailLogActionService.writeLog(sendMailLogAction);
            }
        }
    }

    /**
     * Cron-task auto check contract adjustment files need to send mail to IT
     */
    @Scheduled(cron = "${scheduled.cron.send_mail_adjustment}", zone = "Asia/Bangkok")
    void sendContractFileAdjustment() {
        boolean isTime = invokeConfigStaffService_check_SendTime(adjustmentSendTime);
        if (isTime) {
            List<ContractAdjustmentUploadFile> uploadFiles = adjustmentUploadFileService.findSendMail();
            if (uploadFiles != null && uploadFiles.size() > 0) {
                List<String> emails = invokeConfigStaffService_get_MailList(adjustmentMailList);
                uploadFiles.forEach(uploadFile -> {
                    SendMailLogAction sendMailLogAction = new SendMailLogAction();
                    sendMailLogAction.setCreatedAt(new Date());
                    sendMailLogAction.setName("ADJUSTMENT_UPLOAD_FILE");

                    EmailRequest emailRequest = new EmailRequest();
                    emailRequest.setLangCode("vi");
                    emailRequest.setListEmail(emails.toArray(new String[emails.size()]));
                    emailRequest.setEmailType("upload_adj_to_it");
                    emailRequest.setListFile(Arrays.asList(uploadFile.getFilePath()).toArray(new String[1]));

                    sendMailLogAction.setContent(emailRequest.toString());
                    uploadFile.setSendMail(2);
                    if (invokeEmailService_sendEmail_Success(emailRequest, sendMailLogAction, "/send_s3")) {
                        uploadFile.setSendMail(1);
                        uploadFile.setSendDate(new Date());
                        adjustmentUploadFileService.update(uploadFile);
                    }
                    sendMailLogActionService.writeLog(sendMailLogAction);
                });
            }
        }
    }

    /**
     * Cron-task auto check info of adjustment need to send mail to IT
     */
    @Scheduled(cron = "${scheduled.cron.send_mail_adjustment_to_it}", zone = "Asia/Bangkok")
    void sendAdjustmentToIt() {
        boolean isTime = invokeConfigStaffService_check_SendTime(adjustmentSendTime);
        if (isTime) {
            SearchDisbursementInfo search = new SearchDisbursementInfo();
            search.setIsSent(0);
            List<ObjectSendMailIT> resultSearchs = contractService.getListAdjustmentInfoSendMailIt();
            if (resultSearchs != null && resultSearchs.size() > 0) {

                List<String> contractCodes = new ArrayList<>();
                resultSearchs.stream().distinct().forEach(x -> {
                    contractCodes.add(x.getContractCode());
                });
                List<ContractInfo> contractInfos = new ArrayList<>();
                if (contractCodes.size() > 0) {
                    List<ContractInfo> infoList = hdMiddleService.getContractDetailFromMidServers(contractCodes);
                    if (infoList != null && infoList.size() > 0) {
                        contractInfos.addAll(infoList);
                    }
                }
                // set value old
                for (ObjectSendMailIT mailIT : resultSearchs) {
                    for (ContractInfo info : contractInfos) {
                        if (!HDUtil.isNullOrEmpty(mailIT.getValueOld())) {
                            break;
                        }
                        if (!mailIT.getContractCode().equals(info.getContractNumber())) {
                            continue;
                        }

                        List<ConfigCheckRecords> checkRecords = info.getConfigRecords();
                        for (ConfigCheckRecords records : checkRecords) {
                            if (!mailIT.getKey().equals(records.getKey())) {
                                continue;
                            }
                            mailIT.setValueOld(records.getValue());
                            break;
                        }
                    }
                }

                // update status contract adjustment info
                List<ContractAdjustmentInfo> adjustmentInfos = contractAdjustmentInfoService.getByContractCodes(contractCodes);
                if (adjustmentInfos != null && adjustmentInfos.size() > 0) {
                    for (ContractAdjustmentInfo item : adjustmentInfos) {
                        item.setIsSentMail(1);
                        item.setSentMailAt(new Date());
                    }
                    contractAdjustmentInfoService.updateAll(adjustmentInfos);
                }

                List<String> emails = invokeConfigStaffService_get_MailList(adjustmentMailList);
                SendMailLogAction sendMailLogAction = new SendMailLogAction();
                sendMailLogAction.setCreatedAt(new Date());
                sendMailLogAction.setName("ADJUSTMENT_TO_IT");
                //File file = new File("HDSaison_template_Adjustment_for_IT.xlsx");
                //get file template from s3
                String uri = "https://hdsaison-static.s3-ap-southeast-1.amazonaws.com/contract/template/HDSaison_template_Adjustment_for_IT.xlsx";
                File file = invokeFileHandlerS3_download(new UriRequest(uri));
                if (file != null) {
                    try {
                        EmailRequest emailRequest = new EmailRequest();
                        emailRequest.setLangCode("vi");
                        emailRequest.setListEmail(emails.toArray(new String[emails.size()]));
                        emailRequest.setEmailType("adj_to_it");

                        /**
                         * generate file excel
                         */
                        File result = generateReportAdjustmentToIT(file, resultSearchs);
                        if (result != null) {
                            try {
                                emailRequest.setListFile(new String[]{Base64.getEncoder().encodeToString(ContractUtils.loadFile(result))});
                                sendMailLogAction.setContent(emailRequest.toString());
                                if (invokeEmailService_sendEmail_Success(emailRequest, sendMailLogAction, "/send")) {
                                    /*List<UpdateDisbursementInfo> updateDisbursementInfos = new ArrayList<>();
                                    resultSearchs.forEach(objectSendMailIT -> {
                                        UpdateDisbursementInfo updateDisbursementInfo = new UpdateDisbursementInfo();
                                        updateDisbursementInfo.setId(objectSendMailIT.getId());
                                        updateDisbursementInfo.setSentMailList(StringUtils.join(emails, ","));
                                        updateDisbursementInfo.setIsSent(1);
                                        updateDisbursementInfos.add(updateDisbursementInfo);
                                    });
                                    hdMiddleService.updateDisbursementInfos(updateDisbursementInfos);*/
                                }
                            } catch (IOException e) {
                                e.getMessage();
                            } finally {
                                result.delete();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        file.delete();
                    }
                } else {
                    sendMailLogAction.setStatus(2);
                    sendMailLogAction.setErrorDescription("file template not found:" + uri);
                }
                sendMailLogActionService.writeLog(sendMailLogAction);
            }
        }
    }

    /**
     * Cron-task auto check sign up loan form need to send mail
     */
    @Scheduled(cron = "${scheduled.cron.send_mail_loan}", zone = "Asia/Bangkok")
    void sendSignUpLoan() {
        boolean isTime = invokeConfigStaffService_check_SendTime(loanSendTime);
        if (isTime) {
            SearchSignUpLoan search = new SearchSignUpLoan();
            search.setIsSent(0);
            search.setPageNum(0);
            search.setPageSize(-99);
            List<ResultSearchSignUpLoan> signUpLoans = hdMiddleService.searchSignUpLoan(search);

            if (signUpLoans != null && signUpLoans.size() > 0) {
                //File file = new File("src/main/resources/HDSaison_template_LoanForm.xlsx");
                //get file template from s3
                String uri = "https://hdsaison-static.s3-ap-southeast-1.amazonaws.com/contract/template/HDSaison_template_LoanForm.xlsx";
                File file = invokeFileHandlerS3_download(new UriRequest(uri));
                if (file != null) {
                    try {
                        List<ResultSearchSignUpLoan> signUpLoans_MC = new ArrayList<>();
                        List<ResultSearchSignUpLoan> signUpLoans_ED_MB = new ArrayList<>();
                        List<ResultSearchSignUpLoan> signUpLoans_CL_CLO = new ArrayList<>();

                        signUpLoans.forEach(signUpLoan -> {
                            if (signUpLoan.getLoanType() != null) {
                                if (signUpLoan.getLoanType().toUpperCase().equals("MC"))
                                    signUpLoans_MC.add(signUpLoan);
                                if (signUpLoan.getLoanType().toUpperCase().equals("ED") || signUpLoan.getLoanType().toUpperCase().equals("MB"))
                                    signUpLoans_ED_MB.add(signUpLoan);
                                if (signUpLoan.getLoanType().toUpperCase().equals("CL") || signUpLoan.getLoanType().toUpperCase().equals("CLO"))
                                    signUpLoans_CL_CLO.add(signUpLoan);
                            }
                        });

                        //send mail loan form MC
                        if (signUpLoans_MC.size() > 0) {
                            List<String> emails = invokeConfigStaffService_get_MailList(loanMCMailList);
                            sendMailLoanForm(signUpLoans_MC, emails, file);
                        }

                        //send mail loan form ED
                        if (signUpLoans_ED_MB.size() > 0) {
                            List<String> emails = invokeConfigStaffService_get_MailList(loanEDMailList);
                            sendMailLoanForm(signUpLoans_ED_MB, emails, file);
                        }

                        //send mail loan form CL
                        if (signUpLoans_CL_CLO.size() > 0) {
                            List<String> emails = invokeConfigStaffService_get_MailList(cashLoanMailList);
                            sendMailLoanForm(signUpLoans_CL_CLO, emails, file);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * Send email SignUpLoan
     *
     * @param signUpLoans List of loan form info
     * @param emails      list of email need to send
     * @param file        attachment
     */
    void sendMailLoanForm(List<ResultSearchSignUpLoan> signUpLoans, List<String> emails, File file) {

        SendMailLogAction sendMailLogAction = new SendMailLogAction();
        sendMailLogAction.setCreatedAt(new Date());
        sendMailLogAction.setName("SignUpLoan");

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setLangCode("vi");
        emailRequest.setListEmail(emails.toArray(new String[emails.size()]));
        emailRequest.setEmailType("loan_form");
        if (signUpLoans.get(0).getLoanType().toUpperCase().equals("ED") || signUpLoans.get(0).getLoanType().toUpperCase().equals("MB"))
            emailRequest.setFileType("ED");
        else if (signUpLoans.get(0).getLoanType().toUpperCase().equals("CL") || signUpLoans.get(0).getLoanType().toUpperCase().equals("CLO"))
            emailRequest.setFileType("CL");
        else
            emailRequest.setFileType("MC");

        /**
         * generate file excel
         */
        File result = generateReportLoanForm(file, signUpLoans);
        if (result != null) {
            try {
                emailRequest.setListFile(new String[]{Base64.getEncoder().encodeToString(ContractUtils.loadFile(result))});
                sendMailLogAction.setContent(emailRequest.toString());
                if (invokeEmailService_sendEmail_Success(emailRequest, sendMailLogAction, "/send")) {
                    updateSignUpLoan(signUpLoans, emails);
                }
            } catch (IOException e) {
                e.getMessage();
            } finally {
                result.delete();
            }
        } else {
            sendMailLogAction.setStatus(2);
            sendMailLogAction.setErrorDescription("can not create file report");
        }
        sendMailLogActionService.writeLog(sendMailLogAction);
    }

    /**
     * Update status of SignUpLoan
     *
     * @param signUpLoans  list ResultSearchSignUpLoan need to update status
     * @param emailRequest email has received
     */
    void updateSignUpLoan(List<ResultSearchSignUpLoan> signUpLoans, List<String> emailRequest) {
        List<UpdateSignUpLoan> updateSignUpLoans = new ArrayList<>();
        if (signUpLoans != null && signUpLoans.size() > 0) {
            signUpLoans.forEach(signUpLoan -> {
                UpdateSignUpLoan updateSignUpLoan = new UpdateSignUpLoan();
                updateSignUpLoan.setId(signUpLoan.getId());
                updateSignUpLoan.setSentMailList(StringUtils.join(emailRequest, ","));
                updateSignUpLoan.setIsSent(1);
                updateSignUpLoans.add(updateSignUpLoan);
            });
            hdMiddleService.updateSignUpLoans(updateSignUpLoans);
        }
    }

    /**
     * Cron-task auto check sign up promotion form need to send mail
     */
    //@Scheduled(cron = "${scheduled.cron.send_mail_sign_up_promotion}", zone = "Asia/Bangkok")
    void sendSignUpPromotion() {
        boolean isTime = invokeConfigStaffService_check_SendTime(signUpPromotionSendTime);
        if (isTime) {
            SearchSignUpPromotion search = new SearchSignUpPromotion();
            search.setIsSent(0);
            search.setPageNum(0);
            search.setPageSize(-99);
            List<ResultSearchSignUpPromotion> signUpPromotions = hdMiddleService.searchSignUpPromotion(search);
            if (signUpPromotions != null && signUpPromotions.size() > 0) {
                //File file = new File("src/main/resources/HDSaison_template_LoanPromotion.xlsx");
                //get file template from s3
                String uri = "https://hdsaison-static.s3-ap-southeast-1.amazonaws.com/contract/template/HDSaison_template_LoanPromotion.xlsx";
                File file = invokeFileHandlerS3_download(new UriRequest(uri));
                if (file != null) {
                    try {
                        List<ResultSearchSignUpPromotion> signUpPromotions_MC = new ArrayList<>();
                        List<ResultSearchSignUpPromotion> signUpPromotions_ED_MB = new ArrayList<>();
                        List<ResultSearchSignUpPromotion> signUpPromotions_CL_CLO = new ArrayList<>();
                        signUpPromotions.forEach(signUpPromotion -> {
                            if (signUpPromotion.getPromotionType() != null) {
                                if (signUpPromotion.getPromotionType().toUpperCase().equals("MC"))
                                    signUpPromotions_MC.add(signUpPromotion);
                                if (signUpPromotion.getPromotionType().toUpperCase().equals("ED") || signUpPromotion.getPromotionType().toUpperCase().equals("MB"))
                                    signUpPromotions_ED_MB.add(signUpPromotion);
                                if (signUpPromotion.getPromotionType().toUpperCase().equals("CL") || signUpPromotion.getPromotionType().toUpperCase().equals("CLO"))
                                    signUpPromotions_CL_CLO.add(signUpPromotion);
                            }
                        });
                        if (signUpPromotions_MC.size() > 0) {
                            List<String> emails = invokeConfigStaffService_get_MailList(loanMCPromotionMailList);
                            sendMailLoanFormPromotion(signUpPromotions_MC, emails, file);
                        }
                        if (signUpPromotions_ED_MB.size() > 0) {
                            List<String> emails = invokeConfigStaffService_get_MailList(loanEDPromotionMailList);
                            sendMailLoanFormPromotion(signUpPromotions_ED_MB, emails, file);
                        }
                        if (signUpPromotions_CL_CLO.size() > 0) {
                            List<String> emails = invokeConfigStaffService_get_MailList(cashLoanPromotionMailList);
                            sendMailLoanFormPromotion(signUpPromotions_CL_CLO, emails, file);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * Send email SignUpPromotion
     *
     * @param signUpPromotions List of loan form Promotion info
     * @param emails           list of email need to send
     * @param file             attachment
     */
    void sendMailLoanFormPromotion(List<ResultSearchSignUpPromotion> signUpPromotions, List<String> emails, File file) {

        SendMailLogAction sendMailLogAction = new SendMailLogAction();
        sendMailLogAction.setCreatedAt(new Date());
        sendMailLogAction.setName("SignUpPromotion");

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setLangCode("vi");
        emailRequest.setListEmail(emails.toArray(new String[emails.size()]));
        emailRequest.setEmailType("signup_promotion");
        if (signUpPromotions.get(0).getPromotionType().toUpperCase().equals("ED") || signUpPromotions.get(0).getPromotionType().toUpperCase().equals("MB"))
            emailRequest.setFileType("ED");
        else if (signUpPromotions.get(0).getPromotionType().toUpperCase().equals("CL") || signUpPromotions.get(0).getPromotionType().toUpperCase().equals("CLO"))
            emailRequest.setFileType("CL");
        else
            emailRequest.setFileType("MC");
        /**
         * generate file excel
         */
        File result = generateReportLoanFormPromotion(file, signUpPromotions);
        if (result != null) {
            try {
                emailRequest.setListFile(new String[]{Base64.getEncoder().encodeToString(ContractUtils.loadFile(result))});
                sendMailLogAction.setContent(emailRequest.toString());
                if (invokeEmailService_sendEmail_Success(emailRequest, sendMailLogAction, "/send")) {
                    updateSignUpPromotion(signUpPromotions, emails);
                }
            } catch (IOException e) {
                e.getMessage();
            } finally {
                result.delete();
            }
        } else {
            sendMailLogAction.setStatus(2);
            sendMailLogAction.setErrorDescription("can not create file report");
        }
        sendMailLogActionService.writeLog(sendMailLogAction);
    }

    /**
     * Update status of SignUpPromotion
     *
     * @param signUpPromotions list ResultSearchSignUpPromotion need to update status
     * @param emailRequest     email has received
     */
    void updateSignUpPromotion(List<ResultSearchSignUpPromotion> signUpPromotions, List<String> emailRequest) {
        if (signUpPromotions != null && signUpPromotions.size() > 0) {
            UpdateSignUpPromotion updateSignUpPromotion = new UpdateSignUpPromotion();
            updateSignUpPromotion.setSentMailList(StringUtils.join(emailRequest, ","));
            updateSignUpPromotion.setIsSent(1);
            StringJoiner ids = new StringJoiner(",");
            signUpPromotions.forEach(signUpPromotion -> ids.add(String.valueOf(signUpPromotion.getId())));
            updateSignUpPromotion.setIds(ids.toString());
            hdMiddleService.updateSignUpPromotion(updateSignUpPromotion);
        }
    }

    /**
     * Cron-task auto check Disbursement info need to send mail
     */
    @Scheduled(cron = "${scheduled.cron.send_mail_disbursement}", zone = "Asia/Bangkok")
    void sendDisbursement() {
        boolean isTime = invokeConfigStaffService_check_SendTime(disbursementSendTime);
        if (isTime) {
            SearchDisbursementInfo search = new SearchDisbursementInfo();
            search.setIsSent(0);
            List<ResultDisbursementInfo> resultDisbursementInfos = hdMiddleService.getDisbursementInfoByIsSent(search);
            if (resultDisbursementInfos != null && resultDisbursementInfos.size() > 0) {
                SendMailLogAction sendMailLogAction = new SendMailLogAction();
                sendMailLogAction.setCreatedAt(new Date());
                sendMailLogAction.setName("Disbursement");
                //File file = new File("src/main/resources/HDSaison_template_Accountant.xlsx");
                //get file template from s3
                String uri = "https://hdsaison-static.s3-ap-southeast-1.amazonaws.com/contract/template/HDSaison_template_Accountant.xlsx";
                File file = invokeFileHandlerS3_download(new UriRequest(uri));
                if (file != null) {
                    try {
                        List<String> emails = invokeConfigStaffService_get_MailList(disbursementMailList);

                        EmailRequest emailRequest = new EmailRequest();
                        emailRequest.setLangCode("vi");
                        emailRequest.setListEmail(emails.toArray(new String[emails.size()]));
                        emailRequest.setEmailType("esign_to_accountant");

                        /**
                         * generate file excel
                         */
                        File result = generateReportAccountant(file, resultDisbursementInfos);
                        if (result != null) {
                            try {
                                emailRequest.setListFile(new String[]{Base64.getEncoder().encodeToString(ContractUtils.loadFile(result))});
                                sendMailLogAction.setContent(emailRequest.toString());
                                if (invokeEmailService_sendEmail_Success(emailRequest, sendMailLogAction, "/send")) {
                                    List<UpdateDisbursementInfo> updateDisbursementInfos = new ArrayList<>();
                                    resultDisbursementInfos.forEach(disbursementInfo -> {
                                        UpdateDisbursementInfo updateDisbursementInfo = new UpdateDisbursementInfo();
                                        updateDisbursementInfo.setId(disbursementInfo.getId());
                                        updateDisbursementInfo.setSentMailList(StringUtils.join(emails, ","));
                                        updateDisbursementInfo.setIsSent(1);
                                        updateDisbursementInfos.add(updateDisbursementInfo);
                                    });
                                    hdMiddleService.updateDisbursementInfos(updateDisbursementInfos);
                                }
                            } catch (IOException e) {
                                e.getMessage();
                            } finally {
                                result.delete();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        file.delete();
                    }
                } else {
                    sendMailLogAction.setStatus(2);
                    sendMailLogAction.setErrorDescription("file template not found:" + uri);
                }
                sendMailLogActionService.writeLog(sendMailLogAction);
            }
        }
    }

    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();

    /**
     * Invoke email service to send mail
     *
     * @param emailRequest      contain info need to send a mail
     * @param sendMailLogAction store logs of function
     * @param api               end point of email server will be call
     * @return result send mail success or not
     */
    boolean invokeEmailService_sendEmail_Success(EmailRequest emailRequest, SendMailLogAction sendMailLogAction, String api) {
        if (emailRequest != null) {
            try {
                ResponseDTO<Object> dto = invoker.call(urlEmailRequest + api, emailRequest,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                    sendMailLogAction.setStatus(1);
                    return true;
                }
                sendMailLogAction.setStatus(2);
                sendMailLogAction.setErrorDescription(dto.toString());
            } catch (Exception e) {
                e.printStackTrace();
                sendMailLogAction.setStatus(2);
                sendMailLogAction.setErrorDescription(e.getMessage());
            }
        }
        return false;
    }

    /**
     * Invoke Staff service to check it time to start send email
     *
     * @param key using to get time config
     * @return result is it time or not
     */
    boolean invokeConfigStaffService_check_SendTime(String key) {
        try {
            ResponseDTO<Object> dto = invoker.call(urlConfigStaffRequest + "/get_value", new ConfigStaffGetValue(key),
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                String value = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<String>() {
                        });
                if (HDUtil.isNullOrEmpty(value)) {
                    return false;
                }
                boolean b = false;
                List<String> times = Arrays.asList(value.split(";"));
                if (times != null && times.size() > 0) {
                    for (String time : times) {
                        String[] s = time.split(":");
                        int hour = Integer.valueOf(s[0]);
                        int minute = 0;
                        if (s.length == 2)
                            minute = Integer.valueOf(s[1]);
                        LocalTime configTime = LocalTime.of(hour, minute);
                        LocalTime localTime = LocalTime.now();
                        if (localTime.getHour() == configTime.getHour() && (localTime.getMinute() - configTime.getMinute()) <= 15) {
                            b = true;
                            break;
                        }
                        if (localTime.getHour() > configTime.getHour() && (configTime.getMinute() - localTime.getMinute()) >= 45) {
                            b = true;
                            break;
                        }

                    }
                }
                return b;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Invoke config staff service to get list of email need to send mail
     *
     * @param key using to find list email by key
     * @return list string of email
     */
    List<String> invokeConfigStaffService_get_MailList(String key) {
        ResponseDTO<Object> dto = invoker.call(urlConfigStaffRequest + "/get_value", new ConfigStaffGetValue(key),
                new ParameterizedTypeReference<ResponseDTO<Object>>() {
                });
        if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
            try {
                String value = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<String>() {
                        });
                List<String> lst = Arrays.asList(value.split(";"));
                return lst;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Create file report to send to Accountant
     *
     * @param file          template to create file report
     * @param resultSearchs list ResultDisbursementInfo need info to create file report
     * @return file report created successfully
     */
    File generateReportAccountant(File file, List<ResultDisbursementInfo> resultSearchs) {
        try {
            //create file template on local
            FileInputStream fileIS = new FileInputStream(file);
            Workbook workbookIn = new XSSFWorkbook(fileIS);
            Sheet sheetIn = workbookIn.getSheetAt(0);
            Cell c0 = sheetIn.getRow(3).getCell(0);
            c0.setCellValue("");
            Cell c1 = sheetIn.getRow(3).getCell(1);
            c1.setCellValue("");
            int rowIn = sheetIn.getLastRowNum() + 1;
            for (int i = 0; i < resultSearchs.size(); i++) {
                ResultDisbursementInfo result = resultSearchs.get(i);
                Cell c;
                if (i % 2 == 0)
                    c = c0;
                else
                    c = c1;
                Row row = sheetIn.createRow(rowIn + i);
                row.createCell(0).setCellStyle(c.getCellStyle());
                row.getCell(0).setCellValue(i + 1);

                row.createCell(1).setCellStyle(c.getCellStyle());
                if (result.getContractCode() != null)
                    row.getCell(1).setCellValue(result.getContractCode());

                row.createCell(2).setCellStyle(c.getCellStyle());
                if (result.getAccountName() != null)
                    row.getCell(2).setCellValue(result.getAccountName());

                row.createCell(3).setCellStyle(c.getCellStyle());
                if (result.getAccountNumber() != null)
                    row.getCell(3).setCellValue(result.getAccountNumber());

                row.createCell(4).setCellStyle(c.getCellStyle());
                if (result.getBankName() != null)
                    row.getCell(4).setCellValue(result.getBankName());

                row.createCell(5).setCellStyle(c.getCellStyle());
                if (result.getCreateAt() != null)
                    row.getCell(5).setCellValue(df.format(result.getCreateAt()));
            }
            c0.setCellStyle(null);
            c1.setCellStyle(null);
            String result = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getName());
            FileOutputStream fileOS = new FileOutputStream(result);
            workbookIn.write(fileOS);
            workbookIn.close();
            fileIS.close();
            return new File(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create file report contain info sign up loan form
     *
     * @param file          template to create file report
     * @param resultSearchs list ResultSearchSignUpLoan need info to create file report
     * @return file report created successfully
     */
    File generateReportLoanForm(File file, List<ResultSearchSignUpLoan> resultSearchs) {
        try {
            //create file template on local
            FileInputStream fileIS = new FileInputStream(file);
            Workbook workbookIn = new XSSFWorkbook(fileIS);
            Sheet sheetIn = workbookIn.getSheetAt(0);
            Cell c0 = sheetIn.getRow(3).getCell(0);
            c0.setCellValue("");
            Cell c1 = sheetIn.getRow(3).getCell(1);
            c1.setCellValue("");
            int rowIn = sheetIn.getLastRowNum() + 1;
            for (int i = 0; i < resultSearchs.size(); i++) {
                ResultSearchSignUpLoan result = resultSearchs.get(i);
                Cell c;
                if (i % 2 == 0)
                    c = c0;
                else
                    c = c1;
                Row row = sheetIn.createRow(rowIn + i);
                row.createCell(0).setCellStyle(c.getCellStyle());
                row.getCell(0).setCellValue(i + 1);

                row.createCell(1).setCellStyle(c.getCellStyle());
                if (result.getFullName() != null)
                    row.getCell(1).setCellValue(result.getFullName());

                row.createCell(2).setCellStyle(c.getCellStyle());
                if (result.getPhone() != null)
                    row.getCell(2).setCellValue(result.getPhone());

                row.createCell(3).setCellStyle(c.getCellStyle());
                if (result.getNationalId() != null)
                    row.getCell(3).setCellValue(result.getNationalId());

                row.createCell(4).setCellStyle(c.getCellStyle());
                if (result.getProductionName() != null)
                    row.getCell(4).setCellValue(result.getProductionName());

                row.createCell(5).setCellStyle(c.getCellStyle());
                if (result.getLoanAmount() != null)
                    row.getCell(5).setCellValue(numberFormatter.format(result.getLoanAmount()));

                row.createCell(6).setCellStyle(c.getCellStyle());
                row.getCell(6).setCellValue(result.getTenor());

                row.createCell(7).setCellStyle(c.getCellStyle());
                if (result.getProvinceCode() != null)
                    row.getCell(7).setCellValue(result.getProvinceCode());

                row.createCell(8).setCellStyle(c.getCellStyle());
                if (result.getDistrictCode() != null)
                    row.getCell(8).setCellValue(result.getDistrictCode());

                row.createCell(9).setCellStyle(c.getCellStyle());
                if (result.getLoanType() != null)
                    row.getCell(9).setCellValue(result.getLoanType());

                row.createCell(10).setCellStyle(c.getCellStyle());
                if (result.getCreatedAt() != null)
                    row.getCell(10).setCellValue(df.format(result.getCreatedAt()));

            }
            c0.setCellStyle(null);
            c1.setCellStyle(null);
            String result = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getName());
            FileOutputStream fileOS = new FileOutputStream(result);
            workbookIn.write(fileOS);
            workbookIn.close();
            fileIS.close();
            return new File(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create file report contain info sign up promotion form
     *
     * @param file          template to create file report
     * @param resultSearchs list ResultSearchSignUpPromotion need info to create file report
     * @return file report created successfully
     */
    File generateReportLoanFormPromotion(File file, List<ResultSearchSignUpPromotion> resultSearchs) {
        try {
            //create file template on local
            FileInputStream fileIS = new FileInputStream(file);
            Workbook workbookIn = new XSSFWorkbook(fileIS);
            Sheet sheetIn = workbookIn.getSheetAt(0);
            Cell c0 = sheetIn.getRow(3).getCell(0);
            c0.setCellValue("");
            Cell c1 = sheetIn.getRow(3).getCell(1);
            c1.setCellValue("");
            int rowIn = sheetIn.getLastRowNum() + 1;
            for (int i = 0; i < resultSearchs.size(); i++) {
                ResultSearchSignUpPromotion result = resultSearchs.get(i);
                Cell c;
                if (i % 2 == 0)
                    c = c0;
                else
                    c = c1;
                Row row = sheetIn.createRow(rowIn + i);
                row.createCell(0).setCellStyle(c.getCellStyle());
                row.getCell(0).setCellValue(i + 1);

                row.createCell(1).setCellStyle(c.getCellStyle());
                if (result.getProvinceCode() != null)
                    row.getCell(1).setCellValue(result.getProvinceCode());

                row.createCell(2).setCellStyle(c.getCellStyle());
                if (result.getTitle() != null)
                    row.getCell(2).setCellValue(result.getTitle());

                row.createCell(3).setCellStyle(c.getCellStyle());
                if (result.getPromotionType() != null)
                    row.getCell(3).setCellValue(result.getPromotionType());

                row.createCell(4).setCellStyle(c.getCellStyle());
                if (result.getFullName() != null)
                    row.getCell(4).setCellValue(result.getFullName());

                row.createCell(5).setCellStyle(c.getCellStyle());
                if (result.getPhone() != null)
                    row.getCell(5).setCellValue(result.getPhone());

                row.createCell(6).setCellStyle(c.getCellStyle());
                if (result.getProvinceCode() != null)
                    row.getCell(6).setCellValue(result.getProvinceCode());

                row.createCell(7).setCellStyle(c.getCellStyle());
                if (result.getDistrictCode() != null)
                    row.getCell(7).setCellValue(result.getDistrictCode());

                row.createCell(8).setCellStyle(c.getCellStyle());
                if (result.getCreatedAt() != null)
                    row.getCell(8).setCellValue(df.format(result.getCreatedAt()));

            }
            c0.setCellStyle(null);
            c1.setCellStyle(null);
            String result = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getName());
            FileOutputStream fileOS = new FileOutputStream(result);
            workbookIn.write(fileOS);
            workbookIn.close();
            fileIS.close();
            return new File(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create file report contain info contract adjustment
     *
     * @param file          template to create file report
     * @param resultSearchs list ObjectSendMailIT need info to create file report
     * @return file report created successfully
     */
    File generateReportAdjustmentToIT(File file, List<ObjectSendMailIT> resultSearchs) {
        try {
            //create file template on local
            FileInputStream fileIS = new FileInputStream(file);
            Workbook workbookIn = new XSSFWorkbook(fileIS);
            Sheet sheetIn = workbookIn.getSheetAt(0);
            Cell c0 = sheetIn.getRow(3).getCell(0);
            c0.setCellValue("");
            Cell c1 = sheetIn.getRow(3).getCell(1);
            c1.setCellValue("");
            int rowIn = sheetIn.getLastRowNum() + 1; //=6
            int stt = 1;
            String contractCode = "";
            boolean isMerge;
            int rowFrom = rowIn;
            int rowTo = rowIn;
            for (int i = 0; i < resultSearchs.size(); i++) {
                ObjectSendMailIT result = resultSearchs.get(i);
                isMerge = false;
                if (result.getContractCode().equals(contractCode))
                    isMerge = true;
                Cell c;
                if (i % 2 == 0)
                    c = c0;
                else
                    c = c1;
                int currentRow = rowIn + i;
                Row row = sheetIn.createRow(currentRow);
                if (stt % 2 == 0) {
                    row.createCell(0).setCellStyle(c1.getCellStyle());
                    row.createCell(1).setCellStyle(c1.getCellStyle());
                } else {
                    row.createCell(0).setCellStyle(c0.getCellStyle());
                    row.createCell(1).setCellStyle(c0.getCellStyle());
                }
                row.createCell(0).setCellStyle(c0.getCellStyle());
                row.createCell(1).setCellStyle(c0.getCellStyle());
                if (!isMerge) {
                    row.getCell(0).setCellValue(stt);
                    stt++;
                    if (result.getContractCode() != null) {
                        row.getCell(1).setCellValue(result.getContractCode());
                        contractCode = result.getContractCode();
                    }
                    if (rowFrom != rowTo) {
                        sheetIn.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, 0, 0));
                        sheetIn.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, 1, 1));
                    }
                    rowFrom = rowTo = currentRow;
                } else {
                    rowTo++;
                    if (i == resultSearchs.size() - 1 && rowFrom != rowTo) {
                        sheetIn.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, 0, 0));
                        sheetIn.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, 1, 1));
                    }
                }

                row.createCell(2).setCellStyle(c.getCellStyle());
                if (result.getKey() != null)
                    row.getCell(2).setCellValue(result.getKey());

                row.createCell(3).setCellStyle(c.getCellStyle());
                if (result.getName() != null)
                    row.getCell(3).setCellValue(result.getName());

                row.createCell(4).setCellStyle(c.getCellStyle());
                if (result.getValueOld() != null)
                    row.getCell(4).setCellValue(result.getValueOld());

                row.createCell(5).setCellStyle(c.getCellStyle());
                if (result.getValueAdjustmentInfo() != null)
                    row.getCell(5).setCellValue(result.getValueAdjustmentInfo());

                row.createCell(6).setCellStyle(c.getCellStyle());
                if (result.getStaffName() != null)
                    row.getCell(6).setCellValue(result.getStaffName());

                row.createCell(7).setCellStyle(c.getCellStyle());
                if (result.getConfirmDate() != null)
                    row.getCell(7).setCellValue(df.format(result.getConfirmDate()));

            }
            c0.setCellStyle(null);
            c1.setCellStyle(null);
            String result = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getName());
            FileOutputStream fileOS = new FileOutputStream(result);
            workbookIn.write(fileOS);
            workbookIn.close();
            fileIS.close();
            return new File(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Value("${service.filehandler.endpoint}")
    private String urlFileHandlerRequest;

    /**
     * Invoke file-handler service to download one file from aws s3 bucket
     *
     * @param uriRequest contain uri file s3 request
     * @return file downloaded successfully
     */
    File invokeFileHandlerS3_download(UriRequest uriRequest) {
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
                    Log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
