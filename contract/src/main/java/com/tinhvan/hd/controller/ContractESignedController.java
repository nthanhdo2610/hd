package com.tinhvan.hd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.*;
import com.tinhvan.hd.service.*;
import com.tinhvan.hd.utils.ContractUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/contract/contract_e_signed")
public class ContractESignedController extends HDController {

    @Autowired
    private ContractEsignedService contractEsignedService;

    @Autowired
    private ContractFileService contractFileService;

    @Autowired
    private ContractEsignedFileService eSignedFileService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractFileTemplateService contractFileTemplateService;

    @Autowired
    private ContractFilePositionService contractFilePositionService;

    @Autowired
    private HDMiddleService hdMiddleService;

    @Autowired
    private ContractSendFileService contractSendFileService;

    /**
     * Create object ContractESigned contain info of contract after customer signed
     *
     * @param req object ContractESignedRequest contain info need to verified and create e-signed object
     * @return http status code
     */
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> create(@RequestBody RequestDTO<ContractESignedRequest> req) {

        //validate data
        ContractESignedRequest contractEsignedRequest = req.init();
        System.out.println("request_:" + contractEsignedRequest.toString());
        Contract contract = contractService.getById(contractEsignedRequest.getContractUuid());
        if (contract == null) {
            throw new NotFoundException(1406, "Contract does not exits");
        }
        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contract.getLendingCoreContractId());
        //ContractInfo contractInfo = contractEsignedRequest.getContractInfo();
        if (contractInfo == null) {
            throw new NotFoundException(1406, "ContractInfo does not exits");
        }

        //send_file_to_SI
        ContractSendFile contractSendFile = new ContractSendFile();
        contractSendFile.setContractUuid(contractEsignedRequest.getContractUuid());
        contractSendFile.setCustomerUuid(contractEsignedRequest.getCustomerUuid());
        if (!HDUtil.isNullOrEmpty(contractInfo.getCaAd()))
            contractSendFile.setEmail(contractInfo.getCaAd() + "@hdsaison.com.vn");
        contractSendFile.setCreatedAt(req.now());
        contractSendFile.setStatus(0);
        List<String> attachments = new ArrayList<>();

        List<ContractFile> contractFiles = new ArrayList<>();
        ContractEsigned contractEsigned;
        if (contractEsignedRequest.getIsSigned() == Contract.SIGN_TYPE.E_SIGN) {
            contractSendFile.setIsSignedAdjustment(Contract.SIGN_TYPE.E_SIGN);
            contractSendFile.setMailType("esign_to_si");
            contractFiles = contractFileService.findByContractUuid(contractEsignedRequest.getContractUuid(), Contract.FILE_TYPE.E_SIGN);
            try {
                contractEsigned = contractEsignedService.findByContractId(contractEsignedRequest.getContractUuid());
                if (contractEsigned != null)
                    throw new BadRequestException(1424, "contractEsigned already exits");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException(1424);
            }
        }
        if (contractEsignedRequest.getIsSigned() == Contract.SIGN_TYPE.ADJUSTMENT) {
            contractSendFile.setIsSignedAdjustment(Contract.SIGN_TYPE.ADJUSTMENT);
            contractSendFile.setMailType("adj_to_si");
            contractFiles = contractFileService.findByContractUuid(contractEsignedRequest.getContractUuid(), Contract.FILE_TYPE.ADJUSTMENT);
        }
        if (contractFiles == null || contractFiles.isEmpty()) {
            throw new BadRequestException(1424, "contractFiles not found");
        }
        ConfigContractTypeBackground configContractType = getConfigContractType(contract.getLendingCoreContractId());
        //save contract e signed
        contractEsigned = new ContractEsigned();
        contractEsigned.setCustomerUuid(contractEsignedRequest.getCustomerUuid());
        contractEsigned.setContractUuid(contractEsignedRequest.getContractUuid());
        contractEsigned.setContractCode(contract.getLendingCoreContractId());
        contractEsigned.setIsSignedAdjustment(contractEsignedRequest.getIsSigned());
        if (contractEsignedRequest.getOtpCode() != null)
            contractEsigned.setOtpCode(contractEsignedRequest.getOtpCode());
        if (contractEsignedRequest.geteSignedPhone() != null){
            String phoneNumber = contractEsignedRequest.geteSignedPhone();
            if (phoneNumber.length() == 10) {
                contractEsigned.setEsignedPhone(HDUtil.maskNumber(phoneNumber, "*** *** ####"));
            } else {
                contractEsigned.setEsignedPhone(HDUtil.maskNumber(phoneNumber, "*** **** ####"));
            }
        }

        contractEsigned.setCreatedBy(contractEsignedRequest.getCustomerUuid());
        contractEsigned.setCreatedAt(req.now());

        contractEsigned.setContractPrintingDate(contract.getContractPrintingDate());//2
        contractEsigned.setContractFileCreatedAt(contractFiles.get(0).getCreatedAt());//5
        if (contractEsignedRequest.geteSignedAt() != null)
            contractEsigned.seteSignedAt(contractEsignedRequest.geteSignedAt());//8
        contractEsigned.setEnvironment(req.environment());//10
        if (configContractType != null)
            contractEsigned.setContractType(configContractType.getContractType());
        contractEsignedService.create(contractEsigned);


        //save contract e signed file
        List<String> files = new ArrayList<>();
        int contractESignedId = contractEsigned.getId();
        int totalPage = 0;
        long size = 0;
        for (ContractFile contractFile : contractFiles) {
            totalPage += contractFile.getFileCountPage();
            size += contractFile.getFileSize();
            //save e_signed_file
            ContractEsignedFile eSignedFile = new ContractEsignedFile();
            eSignedFile.setContractId(contractFile.getContractUuid());
            eSignedFile.setContractFileId(contractFile.getId());
            eSignedFile.setContractEsignedId(contractESignedId);
            eSignedFile.setContractFileName(contractFile.getFileName());
            eSignedFile.setFileName(contractFile.getFilePath());
            if (contractEsignedRequest.getIsSigned() == Contract.SIGN_TYPE.E_SIGN) {
                eSignedFile.setContractFileType(Contract.FILE_TYPE.E_SIGN);
            }
            if (contractEsignedRequest.getIsSigned() == Contract.SIGN_TYPE.ADJUSTMENT) {
                eSignedFile.setContractFileType(Contract.FILE_TYPE.ADJUSTMENT);
            }
            eSignedFile.setIdx(contractFile.getIdx());
            eSignedFile.setCreatedAt(req.now());
            eSignedFileService.create(eSignedFile);
            files.add(contractFile.getFileName());
            attachments.add(eSignedFile.getFileName());
        }
        /**
         * begin create file transaction history
         */
        if (contractEsignedRequest.getIsSigned() == Contract.SIGN_TYPE.E_SIGN) {
            //create form request
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy_hh/mm/ss");
            //create e sign history file
            ESignFileTransactionRequest eSignHistoryFrom = new ESignFileTransactionRequest();
            ContractFileTemplate fileTemplate = contractFileTemplateService.findByType(Contract.FILE_TYPE.HISTORY).get(0);
            eSignHistoryFrom.setFileBackground(contractFileTemplateService.findByType(Contract.FILE_TYPE.BACKGROUND).get(0).getPath());
            eSignHistoryFrom.setFileTemplate(fileTemplate.getPath());
            eSignHistoryFrom.setContractId(contractEsignedRequest.getContractUuid().toString());
            eSignHistoryFrom.setFileType(Contract.FILE_TYPE.HISTORY);
            eSignHistoryFrom.setFileNames(files);
            if (contractEsigned.getContractPrintingDate() != null)
                eSignHistoryFrom.setContractPrinting(df.format(contractEsigned.getContractPrintingDate()));//2
            if (contractEsigned.getContractFileCreatedAt() != null)
                eSignHistoryFrom.setReceiveAt(df.format(contractEsigned.getContractFileCreatedAt()));//5
            eSignHistoryFrom.setFullName(ContractUtils.generateFullName(contractInfo));//6
            if (contractInfo.getAddressFamilyBookNo() != null)
                eSignHistoryFrom.setAddressFamilyBookNo(contractInfo.getAddressFamilyBookNo());//7
            if (contractEsigned.geteSignedAt() != null)
                eSignHistoryFrom.setCreatedAt(df.format(contractEsigned.geteSignedAt()));//8
            if (contractEsigned.getEnvironment() != null)
                if (contractEsigned.getEnvironment().equals(HDConstant.ENVIRONMENT.APP))//10
                    eSignHistoryFrom.setEnvironment("Ứng dụng di động (mobile app)");
                else
                    eSignHistoryFrom.setEnvironment("Trang thông tin điện tử (website)");
            Locale locale = new Locale("vn", "VN");
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
            eSignHistoryFrom.setFileSize(totalPage + " trang + " + numberFormatter.format(size) + " KB");//12

            ContractFileResponse fileResponse = invokeFileHandlerS3_generate_file_history(eSignHistoryFrom);
            if (fileResponse == null) {
                throw new InternalServerErrorException(1428, "can not create contract file");
            }


            //save e_signed_file_history
            ContractEsignedFile eSignedFile = new ContractEsignedFile();
            eSignedFile.setContractId(contractEsigned.getContractUuid());
            eSignedFile.setContractEsignedId(contractESignedId);
            eSignedFile.setContractFileName(fileTemplate.getFileName());
            eSignedFile.setContractFileType(Contract.FILE_TYPE.HISTORY);
            eSignedFile.setFileName(fileResponse.getFileLink());
            eSignedFile.setIdx(fileTemplate.getIdx());
            eSignedFile.setCreatedAt(req.now());
            eSignedFileService.create(eSignedFile);
            attachments.add(eSignedFile.getFileName());
            invokeEmailService_sendEmail_to_SI(contractInfo, contractSendFile, attachments);
            return ok(fileResponse.getFileLink());
        }
        invokeEmailService_sendEmail_to_SI(contractInfo, contractSendFile, attachments);
        return ok();

    }

    @Value("${service.filehandler.endpoint}")
    private String urlFileHandlerRequest;
    @Value("${service.config_contract_type_background.endpoint}")
    private String configContractTypeBackgroundRequest;
    @Value("${service.email.endpoint}")
    private String urlEmailRequest;
    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();
    private IdPayload idPayload = new IdPayload();

    /**
     * Invoke file-handler service to create file e-signed history
     *
     * @param adjustmentFileRequest object ESignFileTransactionRequest contain date needed to create file history
     * @return object ContractFileResponse contain info result file
     */
    ContractFileResponse invokeFileHandlerS3_generate_file_history(ESignFileTransactionRequest adjustmentFileRequest) {
        if (adjustmentFileRequest != null) {
            //upload file new
            try {
                ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/contract/history", adjustmentFileRequest,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {

                    ContractFileResponse fileResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<ContractFileResponse>() {
                            });
                    return fileResponse;
                }
            } catch (IOException e) {
                Log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Invoke email service to send an email to SI
     *
     * @param contractInfo info of contract
     * @param contractSendFile contain info need to send mail
     * @param attachments list of uri s3
     */
    void invokeEmailService_sendEmail_to_SI(ContractInfo contractInfo, ContractSendFile contractSendFile, List<String> attachments) {

        Locale locale = new Locale("vn", "VN");
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
        EmailRequest emailRequest = new EmailRequest();
        List<String> listEmail = Arrays.asList(contractSendFile.getEmail());
        emailRequest.setEmailType(contractSendFile.getMailType());
        emailRequest.setListEmail(listEmail.toArray(new String[listEmail.size()]));
        emailRequest.setLangCode("vi");
        if (contractSendFile.getMailType().equals("adj_to_si")) {
            emailRequest.setListFile(attachments.toArray(new String[attachments.size()]));
            contractSendFile.setFileName(attachments.toString());
            emailRequest.setParams(Arrays.asList(
                    ContractUtils.generateFullName(contractInfo),
                    contractInfo.getContractNumber())
                    .toArray(new String[2]));
        }
        if (contractSendFile.getMailType().equals("esign_to_si")) {
            emailRequest.setParams(Arrays.asList(
                    contractInfo.getContractNumber(),
                    ContractUtils.generateFullName(contractInfo),
                    contractInfo.getNationalID(),
                    numberFormatter.format(contractInfo.getLoanAmount()),
                    contractInfo.getProductName())
                    .toArray(new String[5]));
        }
        try {
            ResponseDTO<Object> dto = invoker.call(urlEmailRequest + "/send_s3", emailRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                contractSendFile.setStatus(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        contractSendFileService.create(contractSendFile);
    }

    /**
     * Invoke config service contract type info of one contract by contract code
     *
     * @param contractCode
     * @return ConfigContractTypeBackground contain result needed
     */
    ConfigContractTypeBackground getConfigContractType(String contractCode) {
        idPayload.setId(contractCode);
        ResponseDTO<Object> dto = invoker.call(configContractTypeBackgroundRequest + "/find", idPayload, new ParameterizedTypeReference<ResponseDTO<Object>>() {
        });
        if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
            try {
                List<ConfigContractTypeBackground> lst = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<List<ConfigContractTypeBackground>>() {
                        });
                if (lst != null && lst.size() > 0)
                    return lst.get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
