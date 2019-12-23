package com.tinhvan.hd.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.*;
import com.tinhvan.hd.service.*;
import com.tinhvan.hd.utils.ContractUtils;
import com.tinhvan.hd.utils.WriteLog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/contract")
public class ContractFileController extends HDController {

    @Autowired
    WriteLog log;
    @Autowired
    private ContractFileService contractFileService;
    @Autowired
    private ContractFileTemplateService contractFileTemplateService;
    @Autowired
    private ContractSendFileService contractSendFileService;
    @Autowired
    private ContractEsignedFileService eSignedFileService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private HDMiddleService hdMiddleService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private ContractFilePositionService contractFilePositionService;
    @Autowired
    private ContractEsignedService contractEsignedService;
    @Autowired
    private CustomerLogActionService customerLogActionService;
    @Autowired
    private StaffLogActionService staffLogActionService;

    @Autowired
    private ContractEditInfoService contractEditInfoService;

    @Value("${service.filehandler.endpoint}")
    private String urlFileHandlerRequest;
    @Value("${service.email.endpoint}")
    private String urlEmailRequest;
    @Value("${service.config_contract_type_background.endpoint}")
    private String configContractTypeBackgroundRequest;

    @Value("${service.config_adjustment_contract.endpoint}")
    private String configAdjustmentContract;

    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();
    private IdPayload idPayload = new IdPayload();

    @Value("${hd.url.request.converter}")
    private String converterURI;

    /*
    @PostMapping("/test_send_mail")
    public ResponseEntity<?> test_send_mail(@RequestBody RequestDTO<IdPayload> req) {
        try {
            //create file
            File file = new File("HDSaison_template_Adjustment_for_IT.xlsx");
            List<ObjectSendMailIT> resultSearchs = new ArrayList<>();
            resultSearchs.add(new ObjectSendMailIT("aaa", "key", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
            resultSearchs.add(new ObjectSendMailIT("aaa", "key1", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
            resultSearchs.add(new ObjectSendMailIT("aaa", "key2", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
            resultSearchs.add(new ObjectSendMailIT("bbb", "key3", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
            resultSearchs.add(new ObjectSendMailIT("bbb", "key4", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
            resultSearchs.add(new ObjectSendMailIT("ccc", "key3", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
            resultSearchs.add(new ObjectSendMailIT("ccc", "key4", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
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
                    row.createCell(0).setCellStyle(c0.getCellStyle());
                    row.createCell(1).setCellStyle(c0.getCellStyle());
                } else {
                    row.createCell(0).setCellStyle(c1.getCellStyle());
                    row.createCell(1).setCellStyle(c1.getCellStyle());
                }
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
                    row.getCell(7).setCellValue(new SimpleDateFormat("HH:mm dd/MM/yyyy").format(result.getConfirmDate()));

            }
            c0.setCellStyle(null);
            c1.setCellStyle(null);
            String result = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getName());
            FileOutputStream fileOS = new FileOutputStream(result);
            workbookIn.write(fileOS);
            workbookIn.close();
            fileIS.close();

            //send mail
            File report = new File(result);
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setLangCode("vi");
            emailRequest.setListEmail(Arrays.asList(req.init().getId()).toArray(new String[1]));
            emailRequest.setEmailType("adj_to_it");
            emailRequest.setListFile(new String[]{Base64.getEncoder().encodeToString(ContractUtils.loadFile(report))});
            ResponseDTO<Object> dto = invoker.call(urlEmailRequest + "/send", emailRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            System.out.println(dto.toString());
            report.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok();
    }
*/

    /**
     * Invoke middle to convert file word to pdf using input by base64 data
     *
     * @param lstBase64 list string of base 64 file word
     * @return list string of base 64 file pdf
     */
    @PostMapping("/word2pdf_b64")
    public List<String> word2pdf_b64(@RequestBody List<String> lstBase64) {
        if (lstBase64 == null || lstBase64.size() == 0) {
            throw new BadRequestException();
        }
        List<String> results = new ArrayList<>();
        try {
            results.addAll(ContractUtils.word2pdf_b64(converterURI + "/word2pdf", lstBase64));
            //results = ContractUtils.word2pdf("http://192.168.75.104:8810/api/v1/converter" + "/word2pdf", lstBase64);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
        if (results == null || results.size() == 0) {
            throw new InternalServerErrorException();
        }
        return results;
    }

    /**
     * Invoke middle to convert file word to pdf using upload multipart file
     *
     * @param files list file word upload
     * @return ResponseEntity contain result of request after invoke api of middle
     */
    @PostMapping(value = "/word2pdf")
    public ResponseEntity<?> word2pdf(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new BadRequestException();
        }
        StopWatch sw = new StopWatch("word2pdf");
        sw.start();
        List<File> lstFile = new ArrayList<>();
        try {
            for (int i = 0; i < files.length; i++) {
                File file = new File(UUID.randomUUID()+".docx");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(files[i].getBytes());
                fos.close();
                lstFile.add(file);
            }
            return ContractUtils.word2pdf(converterURI + "/word2pdf", lstFile);
            //return ContractUtils.word2pdf("http://192.168.75.104:8810/api/v1/converter/word2pdf", lstFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        } finally {
            lstFile.forEach(file -> {
                if (file != null)
                    file.delete();
            });
            sw.stop();
            System.out.println("contract word2pdf:"+sw.getTotalTimeMillis());
        }
    }

    /**
     * Get policy of Lending app
     *
     * @param req contain type of policy
     * @return UriResponse contain content of file policy
     */
    @PostMapping("/getPolicy")
    public ResponseEntity<?> getPolicy(@RequestBody RequestDTO<IdPayload> req) {
        String pType = (String) req.init().getId();
        if (!pType.toUpperCase().equals("P1") && !pType.toUpperCase().equals("P2"))
            throw new BadRequestException(1426);
        List<ContractFileTemplate> fileTemplates = contractFileTemplateService.findByType(pType);
        if (fileTemplates == null || fileTemplates.size() == 0)
            throw new BadRequestException(1426);
        return ok(invokeFileHandlerS3_download(new UriRequest(fileTemplates.get(0).getPath())));
    }

    /**
     * Find list file template
     *
     * @return list ContractFileTemplate
     */
    @PostMapping("/getFileTemplate")
    public ResponseEntity<?> getFileTemplate() {
        return ok(contractFileTemplateService.findAll());
    }

    /**
     * Create contract file when customer sign or adjustment contract
     *
     * @param req GetFileContract contain info need to generate contract file
     * @return list String uri s3 of file created
     */
    @PostMapping("/contract_file/create")
    @Transactional
    public ResponseEntity<?> contractFile(@RequestBody RequestDTO<GetFileContract> req) {
        StopWatch sw = new StopWatch("/contract_file/create");
        GetFileContract getFileContract = req.init();
        System.out.println("contract_file/create:" + getFileContract.toString());
        String contractCode = getFileContract.getContractCode();
        UUID customerUuid = getFileContract.getCustomerUuid();

        sw.start("getContractByContractCode");
        //valid contract from middle server
        Contract contract = contractService.getContractByContractCode(contractCode);
        sw.stop();
        if (contract == null) {
            throw new NotFoundException(1406, "Contract does not exits");
        }
        sw.start("getContractInfoFromMidServer");
        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contractCode);
        sw.stop();
        //ContractInfo contractInfo = getFileContract.getContractInfo();
        if (contractInfo == null) {
            throw new NotFoundException(1406, "ContractInfo does not exits");
        }
        sw.start("getConfigContractTypeBackground");
        // get contract file template (call config service)
        ConfigContractTypeBackground configContractType = getConfigContractType(contract.getLendingCoreContractId());
        sw.stop();
        if (configContractType == null) {
            throw new NotFoundException(1427, "ConfigContractTypeBackground file not found");
        }
        List<ContractFileTemplate> fileTemplates = new ArrayList<>();
        List<ContractFile> contractFileOlds = new ArrayList<>();
        sw.start("getContractEsigned");
        ContractEsigned contractEsigned;
        try {
            contractEsigned = contractEsignedService.findByContractId(contract.getContractUuid());
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(1428, "can not create contract file");

        }
        sw.stop();
        if (getFileContract.getSignType() == Contract.SIGN_TYPE.E_SIGN) {
            if (contractEsigned != null) {
                throw new BadRequestException(1428, "contractEsigned already exist for e-sign");
            }
            sw.start("get fileTemplates");
            fileTemplates = contractFileTemplateService.findByType(configContractType.getContractType());
            sw.stop();
            sw.start("get contractFileOlds");
            contractFileOlds = contractFileService.findByContractUuid(contract.getContractUuid(), Contract.FILE_TYPE.E_SIGN);
            sw.stop();
        }
        if (getFileContract.getSignType() == Contract.SIGN_TYPE.ADJUSTMENT) {
            if (contractEsigned == null) {
                throw new BadRequestException(1428, "contractEsigned not exist for adjustment");
            }
            sw.start("get fileTemplates");
            fileTemplates = contractFileTemplateService.findByType(Contract.FILE_TYPE.ADJUSTMENT);
            sw.stop();
            sw.start("get contractFileOlds");
            contractFileOlds = contractFileService.findByContractUuid(contract.getContractUuid(), Contract.FILE_TYPE.ADJUSTMENT);
            sw.stop();
        }

        if (fileTemplates == null || fileTemplates.isEmpty()) {
            throw new NotFoundException(1427, "contract file templates not found");
        }
        //get contract scheme file
        if (!HDUtil.isNullOrEmpty(contractInfo.getSchemeCode())) {
            sw.start("findBySchemeCode");
            List<Scheme> schemes = schemeService.findBySchemeCode(contractInfo.getSchemeCode());
            if (schemes != null && schemes.size() > 0) {
                for (Scheme scheme : schemes) {
                    ContractFileTemplate fileTemplate = new ContractFileTemplate();
                    fileTemplate.setFileName(scheme.getSchemeName());
                    fileTemplate.setPath(scheme.getFileLink());
                    fileTemplates.add(fileTemplate);
                }
            }
            sw.stop();
            contract.setScheme(contractInfo.getSchemeCode());
            contractService.updateContract(contract);
        }

        List<ContractFile> contractFiles = new ArrayList<>();
        List<String> fileOlds = new ArrayList<>();
        if (contractFileOlds != null && contractFileOlds.size() > 0) {
            sw.start("delete contract file old");
            contractFileOlds.forEach(contractFile -> {
                fileOlds.add(contractFile.getFilePath());
                contractFileService.delete(contractFile);
            });
            sw.stop();
        }

        List<ContractFileResponse> fileResponses;
        GenerateContractFileResponse contractFileResponse = null;
        /**
         * begin create request object to call file-handler generate contract file e-sign
         */
        if (getFileContract.getSignType() == Contract.SIGN_TYPE.E_SIGN) {
            if (configContractType.getContractType().toUpperCase().equals("CL") ||
                    configContractType.getContractType().toUpperCase().equals("CLO")) {
                SearchDisbursementInfo search = new SearchDisbursementInfo();
                search.setCustomerUuid(customerUuid.toString());
                search.setIsSent(-1);
                List<ResultDisbursementInfo> resultDisbursementInfos = null;
                ResultDisbursementInfo resultDisbursementInfo = null;
                try {
                    sw.start("SearchDisbursementInfo");
                    resultDisbursementInfos = hdMiddleService.getDisbursementInfoByIsSent(search);
                    if (resultDisbursementInfos != null && resultDisbursementInfos.size() > 0) {
                        // sort payment
                        Collections.sort(resultDisbursementInfos, new Comparator<ResultDisbursementInfo>() {
                            public int compare(ResultDisbursementInfo p1, ResultDisbursementInfo p2) {
                                return Long.valueOf(p2.getCreateAt().getTime()).compareTo(p1.getCreateAt().getTime());
                            }
                        });
                        for (ResultDisbursementInfo item : resultDisbursementInfos) {
                            if (!item.getContractCode().equals(contractCode)) {
                                continue;
                            }
                            resultDisbursementInfo = item;
                            break;
                        }
                    }
                    sw.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new InternalServerErrorException(1428, "can not get SearchDisbursementInfo");
                }
                if (resultDisbursementInfo != null) {
                    if (!HDUtil.isNullOrEmpty(resultDisbursementInfo.getAccountName()))
                        contractInfo.setAccountName(resultDisbursementInfo.getAccountName());
                    if (!HDUtil.isNullOrEmpty(resultDisbursementInfo.getAccountNumber()))
                        contractInfo.setAccountNo(resultDisbursementInfo.getAccountNumber());
                    if (!HDUtil.isNullOrEmpty(resultDisbursementInfo.getBankName()))
                        contractInfo.setBankName(resultDisbursementInfo.getBankName());
                }
            }
            GenerateFileRequest generateFileRequest = new GenerateFileRequest();
            generateFileRequest.setFileOlds(fileOlds);
            generateFileRequest.setContractInfo(contractInfo);
            generateFileRequest.setContractId(contract.getContractUuid().toString());
            generateFileRequest.setFileType(configContractType.getContractType());
            sw.start("contract file background");
            generateFileRequest.setFileBackground(contractFileTemplateService.findByType(Contract.FILE_TYPE.BACKGROUND).get(0).getPath());
            sw.stop();
            List<String> fileRequests = new ArrayList<>();
            fileTemplates.forEach(fileTemplate -> {
                fileRequests.add(fileTemplate.getPath());
                ContractFile contractFile = new ContractFile();
                contractFile.setContractUuid(contract.getContractUuid());
                contractFile.setFileType(Contract.FILE_TYPE.E_SIGN);
                contractFile.setCreatedAt(req.now());
                contractFile.setFileName(fileTemplate.getFileName());
                contractFile.setIdx(fileTemplate.getIdx());
                contractFiles.add(contractFile);
            });
            generateFileRequest.setFileTemplates(fileRequests);
            sw.start("invokeFileHandlerS3_generate_file");
            contractFileResponse = invokeFileHandlerS3_generate_file(generateFileRequest);
            sw.stop();
        }

        /**
         * begin create request object to call file-handler generate contract file adjustment
         */
        if (getFileContract.getSignType() == Contract.SIGN_TYPE.ADJUSTMENT) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Locale locale = new Locale("vn", "VN");
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

            AdjustmentFileRequest adjustmentFileRequest = new AdjustmentFileRequest();
            adjustmentFileRequest.setFileOlds(fileOlds);

            //generate AdjustmentFrom
            AdjustmentFrom adjustmentFrom = new AdjustmentFrom();
            adjustmentFrom.setCurrentDate(dateFormat.format(req.now()));
            adjustmentFrom.setFullName(ContractUtils.generateFullName(contractInfo));
            adjustmentFrom.setBirthday(dateFormat.format(contractInfo.getBirthday()));
            adjustmentFrom.setNationalID(contractInfo.getNationalID());
            adjustmentFrom.setAddressFamilyBookNo(contractInfo.getAddressFamilyBookNo());
            adjustmentFrom.setAddress(contractInfo.getAddress());
            adjustmentFrom.setPhoneNumber(contractInfo.getPhoneNumber());
            adjustmentFrom.setEmail("");
            adjustmentFrom.setProfession(contractInfo.getProfession());
            adjustmentFrom.setMonthlyNetSalary(numberFormatter.format(contractInfo.getMonthlyNetSalary()));
            adjustmentFrom.setSuggestDate(dateFormat.format(contractEsigned.getCreatedAt()));
            adjustmentFrom.setCreditNo(contract.getLendingCoreContractId());
            adjustmentFrom.setCreditDate(dateFormat.format(contractEsigned.getCreatedAt()));
            adjustmentFrom.setMortgageNo(contract.getLendingCoreContractId());
            adjustmentFrom.setMortgageDate(dateFormat.format(contractEsigned.getCreatedAt()));
            adjustmentFrom.setInsuranceDate(dateFormat.format(contractEsigned.getCreatedAt()));

            sw.start("getAdjConfirmByContractCode");
            List<AdjConfirmDto> adjConfigs = contractService.getAdjConfirmByContractCode(contract.getLendingCoreContractId());
            sw.stop();
            // bo sung 25/11/2019
            List<ConfigCheckRecords> checkRecords = contractInfo.getConfigRecords();

            if (adjConfigs != null && adjConfigs.size() > 0) {
                for (AdjConfirmDto adjConfirmDto : adjConfigs) {
                    for (ConfigCheckRecords records : checkRecords) {
                        if (adjConfirmDto.getKey().equals(records.getKey())) {
                            adjConfirmDto.setOldValue(records.getValue());
                            break;
                        }
                    }
                }
            }

            sw.start("getContractEditInfoByContractCode");
            ContractEditInfo contractEditInfo = contractEditInfoService.getContractEditInfoByContractCode(contract.getLendingCoreContractId());
            sw.stop();
            if (contractEditInfo != null && contractEditInfo.getMonthlyDueDate() != null) {

                if (contractInfo.getMonthlyDueDate() != null && contractInfo.getMonthlyDueDate().intValue() != contractEditInfo.getMonthlyDueDate()) {
                    AdjConfirmDto adjConfirmDto = new AdjConfirmDto();
                    adjConfirmDto.setName("Ngày thanh toán hàng tháng");
                    adjConfirmDto.setKey("CYCLEDAY");
                    adjConfirmDto.setOldValue(contractEditInfo.getMonthlyDueDate().toString());
                    adjConfirmDto.setNewValue(contractInfo.getMonthlyDueDate().toString());
                    adjConfigs.add(adjConfirmDto);
                }
            }

            adjustmentFileRequest.setAdjConfirmDtoList(adjConfigs);
            adjustmentFileRequest.setAdjustmentFrom(adjustmentFrom);

            //create e sign adjustment file
            ContractFileTemplate fileTemplate = fileTemplates.get(0);
            sw.start("contract file background");
            adjustmentFileRequest.setFileBackground(contractFileTemplateService.findByType(Contract.FILE_TYPE.BACKGROUND).get(0).getPath());
            sw.stop();
            adjustmentFileRequest.setFileTemplate(fileTemplate.getPath());
            adjustmentFileRequest.setContractId(contract.getContractUuid().toString());
            adjustmentFileRequest.setFileType(Contract.FILE_TYPE.ADJUSTMENT);

            ContractFile contractFile = new ContractFile();
            contractFile.setContractUuid(contract.getContractUuid());
            contractFile.setFileType(Contract.FILE_TYPE.ADJUSTMENT);
            contractFile.setCreatedAt(req.now());
            contractFile.setFileName(fileTemplate.getFileName());
            contractFile.setIdx(fileTemplate.getIdx());
            contractFiles.add(contractFile);
            sw.start("invokeFileHandlerS3_generate_file_adjustment");
            contractFileResponse = invokeFileHandlerS3_generate_file_adjustment(adjustmentFileRequest);
            sw.stop();
        }

        if (contractFileResponse == null) {
            throw new InternalServerErrorException(1428, "filehandler-service can not create contract file");
        }
        fileResponses = contractFileResponse.getFiles();
        /**
         * end
         */

        //insert file new
        List<String> urlFiles = new ArrayList<>();
        int bound = contractFiles.size();
        for (int i = 0; i < bound; i++) {
            urlFiles.add(i, fileResponses.get(i).getFileLink());
            ContractFile contractFile = contractFiles.get(i);
            contractFile.setFilePath(fileResponses.get(i).getFileLink());
            contractFile.setFileSize(fileResponses.get(i).getFileSize());
            contractFile.setFileCountPage(fileResponses.get(i).getFileCountPage());
            contractFileService.create(contractFile);
        }

        //write log customer
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống HDSaison khởi tạo bộ chứng từ Hợp đồng tín dụng");
        joiner.add("- Mã số Hợp đồng: " + contractCode);
        joiner.add("- Người khởi tạo: Công ty Tài chính TNHH HD SAISON");
        joiner.add("- Địa điểm khởi tạo: Trụ sở văn phòng Công ty Tài chính TNHH HD SAISON - Lầu 8, 9, 10, Tòa nhà Gilimex - 24C Phan Đăng Lưu, Phường 6, Quận Bình Thạnh, Tp. HCM");
        log.writeLogAction(req, "Khởi tạo chứng từ Hợp đồng", joiner.toString(), getFileContract.toString(), "", "", contractCode, "esign");
        System.out.println(sw.prettyPrint());
        return ok(urlFiles);
    }

    /**
     * Create contract file when customer sign or adjustment contract
     *
     * @param req GetFileContract contain info need to generate contract file
     * @return string data base 64 of all file has joined
     */
    @PostMapping("/contract_file/create_b64")
    @Transactional
    public ResponseEntity<?> create_b64(@RequestBody RequestDTO<GetFileContract> req) {
        StopWatch sw = new StopWatch("/contract_file/create");
        GetFileContract getFileContract = req.init();
        System.out.println("contract_file/create:" + getFileContract.toString());
        String contractCode = getFileContract.getContractCode();
        UUID customerUuid = getFileContract.getCustomerUuid();

        sw.start("getContractByContractCode");
        //valid contract from middle server
        Contract contract = contractService.getContractByContractCode(contractCode);
        sw.stop();
        if (contract == null) {
            throw new NotFoundException(1406, "Contract does not exits");
        }
        sw.start("getContractInfoFromMidServer");
        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contractCode);
        sw.stop();
        //ContractInfo contractInfo = getFileContract.getContractInfo();
        if (contractInfo == null) {
            throw new NotFoundException(1406, "ContractInfo does not exits");
        }
        sw.start("getConfigContractTypeBackground");
        // get contract file template (call config service)
        ConfigContractTypeBackground configContractType = getConfigContractType(contract.getLendingCoreContractId());
        sw.stop();
        if (configContractType == null) {
            throw new NotFoundException(1427, "ConfigContractTypeBackground file not found");
        }
        List<ContractFileTemplate> fileTemplates = new ArrayList<>();
        List<ContractFile> contractFileOlds = new ArrayList<>();
        sw.start("getContractEsigned");
        ContractEsigned contractEsigned;
        try {
            contractEsigned = contractEsignedService.findByContractId(contract.getContractUuid());
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(1428, "can not create contract file");

        }
        sw.stop();
        if (getFileContract.getSignType() == Contract.SIGN_TYPE.E_SIGN) {
            if (contractEsigned != null) {
                throw new BadRequestException(1428, "contractEsigned already exist for e-sign");
            }
            sw.start("get fileTemplates");
            fileTemplates = contractFileTemplateService.findByType(configContractType.getContractType());
            sw.stop();
            sw.start("get contractFileOlds");
            contractFileOlds = contractFileService.findByContractUuid(contract.getContractUuid(), Contract.FILE_TYPE.E_SIGN);
            sw.stop();
        }
        if (getFileContract.getSignType() == Contract.SIGN_TYPE.ADJUSTMENT) {
            if (contractEsigned == null) {
                throw new BadRequestException(1428, "contractEsigned not exist for adjustment");
            }
            sw.start("get fileTemplates");
            fileTemplates = contractFileTemplateService.findByType(Contract.FILE_TYPE.ADJUSTMENT);
            sw.stop();
            sw.start("get contractFileOlds");
            contractFileOlds = contractFileService.findByContractUuid(contract.getContractUuid(), Contract.FILE_TYPE.ADJUSTMENT);
            sw.stop();
        }

        if (fileTemplates == null || fileTemplates.isEmpty()) {
            throw new NotFoundException(1427, "contract file templates not found");
        }
        //get contract scheme file
        if (!HDUtil.isNullOrEmpty(contractInfo.getSchemeCode())) {
            sw.start("findBySchemeCode");
            List<Scheme> schemes = schemeService.findBySchemeCode(contractInfo.getSchemeCode());
            if (schemes != null && schemes.size() > 0) {
                for (Scheme scheme : schemes) {
                    ContractFileTemplate fileTemplate = new ContractFileTemplate();
                    fileTemplate.setFileName(scheme.getSchemeName());
                    fileTemplate.setPath(scheme.getFileLink());
                    fileTemplates.add(fileTemplate);
                }
            }
            contract.setScheme(contractInfo.getSchemeCode());
            contractService.updateContract(contract);
            sw.stop();
        }

        List<ContractFile> contractFiles = new ArrayList<>();
        List<String> fileOlds = new ArrayList<>();
        if (contractFileOlds != null && contractFileOlds.size() > 0) {
            sw.start("delete contract file old");
            contractFileOlds.forEach(contractFile -> {
                fileOlds.add(contractFile.getFilePath());
                contractFileService.delete(contractFile);
            });
            sw.stop();
        }

        List<ContractFileResponse> fileResponses;
        GenerateContractFileResponse contractFileResponse = null;
        /**
         * begin create request object to call file-handler generate contract file e-sign
         */
        if (getFileContract.getSignType() == Contract.SIGN_TYPE.E_SIGN) {
            if (configContractType.getContractType().toUpperCase().equals("CL") ||
                    configContractType.getContractType().toUpperCase().equals("CLO")) {
                SearchDisbursementInfo search = new SearchDisbursementInfo();
                search.setCustomerUuid(customerUuid.toString());
                search.setIsSent(-1);
                List<ResultDisbursementInfo> resultDisbursementInfos = null;
                ResultDisbursementInfo resultDisbursementInfo = null;
                try {
                    sw.start("SearchDisbursementInfo");
                    resultDisbursementInfos = hdMiddleService.getDisbursementInfoByIsSent(search);
                    if (resultDisbursementInfos != null && resultDisbursementInfos.size() > 0) {
                        // sort payment
                        Collections.sort(resultDisbursementInfos, new Comparator<ResultDisbursementInfo>() {
                            public int compare(ResultDisbursementInfo p1, ResultDisbursementInfo p2) {
                                return Long.valueOf(p2.getCreateAt().getTime()).compareTo(p1.getCreateAt().getTime());
                            }
                        });
                        for (ResultDisbursementInfo item : resultDisbursementInfos) {
                            if (!item.getContractCode().equals(contractCode)) {
                                continue;
                            }
                            resultDisbursementInfo = item;
                            break;
                        }
                    }
                    sw.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new InternalServerErrorException(1428, "can not get SearchDisbursementInfo");
                }
                if (resultDisbursementInfo != null) {
                    if (!HDUtil.isNullOrEmpty(resultDisbursementInfo.getAccountName()))
                        contractInfo.setAccountName(resultDisbursementInfo.getAccountName());
                    if (!HDUtil.isNullOrEmpty(resultDisbursementInfo.getAccountNumber()))
                        contractInfo.setAccountNo(resultDisbursementInfo.getAccountNumber());
                    if (!HDUtil.isNullOrEmpty(resultDisbursementInfo.getBankName()))
                        contractInfo.setBankName(resultDisbursementInfo.getBankName());
                }
            }
            GenerateFileRequest generateFileRequest = new GenerateFileRequest();
            generateFileRequest.setFileOlds(fileOlds);
            generateFileRequest.setContractInfo(contractInfo);
            generateFileRequest.setContractId(contract.getContractUuid().toString());
            generateFileRequest.setFileType(configContractType.getContractType());
            sw.start("contract file background");
            generateFileRequest.setFileBackground(contractFileTemplateService.findByType(Contract.FILE_TYPE.BACKGROUND).get(0).getPath());
            sw.stop();
            List<String> fileRequests = new ArrayList<>();
            fileTemplates.forEach(fileTemplate -> {
                fileRequests.add(fileTemplate.getPath());
                ContractFile contractFile = new ContractFile();
                contractFile.setContractUuid(contract.getContractUuid());
                contractFile.setFileType(Contract.FILE_TYPE.E_SIGN);
                contractFile.setCreatedAt(req.now());
                contractFile.setFileName(fileTemplate.getFileName());
                contractFile.setIdx(fileTemplate.getIdx());
                contractFiles.add(contractFile);
            });
            generateFileRequest.setFileTemplates(fileRequests);
            sw.start("invokeFileHandlerS3_generate_file");
            contractFileResponse = invokeFileHandlerS3_generate_file(generateFileRequest);
            sw.stop();
        }

        /**
         * begin create request object to call file-handler generate contract file adjustment
         */
        if (getFileContract.getSignType() == Contract.SIGN_TYPE.ADJUSTMENT) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Locale locale = new Locale("vn", "VN");
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

            AdjustmentFileRequest adjustmentFileRequest = new AdjustmentFileRequest();
            adjustmentFileRequest.setFileOlds(fileOlds);

            //generate AdjustmentFrom
            AdjustmentFrom adjustmentFrom = new AdjustmentFrom();
            adjustmentFrom.setCurrentDate(dateFormat.format(req.now()));
            adjustmentFrom.setFullName(ContractUtils.generateFullName(contractInfo));
            adjustmentFrom.setBirthday(dateFormat.format(contractInfo.getBirthday()));
            adjustmentFrom.setNationalID(contractInfo.getNationalID());
            adjustmentFrom.setAddressFamilyBookNo(contractInfo.getAddressFamilyBookNo());
            adjustmentFrom.setAddress(contractInfo.getAddress());
            adjustmentFrom.setPhoneNumber(contractInfo.getPhoneNumber());
            adjustmentFrom.setEmail("");
            adjustmentFrom.setProfession(contractInfo.getProfession());
            adjustmentFrom.setMonthlyNetSalary(numberFormatter.format(contractInfo.getMonthlyNetSalary()));
            adjustmentFrom.setSuggestDate(dateFormat.format(contractEsigned.getCreatedAt()));
            adjustmentFrom.setCreditNo(contract.getLendingCoreContractId());
            adjustmentFrom.setCreditDate(dateFormat.format(contractEsigned.getCreatedAt()));
            adjustmentFrom.setMortgageNo(contract.getLendingCoreContractId());
            adjustmentFrom.setMortgageDate(dateFormat.format(contractEsigned.getCreatedAt()));
            adjustmentFrom.setInsuranceDate(dateFormat.format(contractEsigned.getCreatedAt()));

            sw.start("getAdjConfirmByContractCode");
            List<AdjConfirmDto> adjConfigs = contractService.getAdjConfirmByContractCode(contract.getLendingCoreContractId());
            sw.stop();
            // bo sung 25/11/2019
            List<ConfigCheckRecords> checkRecords = contractInfo.getConfigRecords();

            if (adjConfigs != null && adjConfigs.size() > 0) {
                for (AdjConfirmDto adjConfirmDto : adjConfigs) {
                    for (ConfigCheckRecords records : checkRecords) {
                        if (adjConfirmDto.getKey().equals(records.getKey())) {
                            adjConfirmDto.setOldValue(records.getValue());
                            break;
                        }
                    }
                }
            }

            sw.start("getContractEditInfoByContractCode");
            ContractEditInfo contractEditInfo = contractEditInfoService.getContractEditInfoByContractCode(contract.getLendingCoreContractId());
            sw.stop();
            if (contractEditInfo != null && contractEditInfo.getMonthlyDueDate() != null) {

                if (contractInfo.getMonthlyDueDate() != null && contractInfo.getMonthlyDueDate().intValue() != contractEditInfo.getMonthlyDueDate()) {
                    AdjConfirmDto adjConfirmDto = new AdjConfirmDto();
                    adjConfirmDto.setName("Ngày thanh toán hàng tháng");
                    adjConfirmDto.setKey("CYCLEDAY");
                    adjConfirmDto.setOldValue(contractEditInfo.getMonthlyDueDate().toString());
                    adjConfirmDto.setNewValue(contractInfo.getMonthlyDueDate().toString());
                    adjConfigs.add(adjConfirmDto);
                }
            }

            adjustmentFileRequest.setAdjConfirmDtoList(adjConfigs);
            adjustmentFileRequest.setAdjustmentFrom(adjustmentFrom);

            //create e sign adjustment file
            ContractFileTemplate fileTemplate = fileTemplates.get(0);
            sw.start("contract file background");
            adjustmentFileRequest.setFileBackground(contractFileTemplateService.findByType(Contract.FILE_TYPE.BACKGROUND).get(0).getPath());
            sw.stop();
            adjustmentFileRequest.setFileTemplate(fileTemplate.getPath());
            adjustmentFileRequest.setContractId(contract.getContractUuid().toString());
            adjustmentFileRequest.setFileType(Contract.FILE_TYPE.ADJUSTMENT);

            ContractFile contractFile = new ContractFile();
            contractFile.setContractUuid(contract.getContractUuid());
            contractFile.setFileType(Contract.FILE_TYPE.ADJUSTMENT);
            contractFile.setCreatedAt(req.now());
            contractFile.setFileName(fileTemplate.getFileName());
            contractFile.setIdx(fileTemplate.getIdx());
            contractFiles.add(contractFile);
            sw.start("invokeFileHandlerS3_generate_file_adjustment");
            contractFileResponse = invokeFileHandlerS3_generate_file_adjustment(adjustmentFileRequest);
            sw.stop();
        }

        if (contractFileResponse == null) {
            throw new InternalServerErrorException(1428, "filehandler-service can not create contract file");
        }
        fileResponses = contractFileResponse.getFiles();
        /**
         * end
         */

        //insert file new
        List<String> urlFiles = new ArrayList<>();
        int bound = contractFiles.size();
        for (int i = 0; i < bound; i++) {
            urlFiles.add(i, fileResponses.get(i).getFileLink());
            ContractFile contractFile = contractFiles.get(i);
            contractFile.setFilePath(fileResponses.get(i).getFileLink());
            contractFile.setFileSize(fileResponses.get(i).getFileSize());
            contractFile.setFileCountPage(fileResponses.get(i).getFileCountPage());
            contractFileService.create(contractFile);
        }

        //write log customer
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống HDSaison khởi tạo bộ chứng từ Hợp đồng tín dụng");
        joiner.add("- Mã số Hợp đồng: " + contractCode);
        joiner.add("- Người khởi tạo: Công ty Tài chính TNHH HD SAISON");
        joiner.add("- Địa điểm khởi tạo: Trụ sở văn phòng Công ty Tài chính TNHH HD SAISON - Lầu 8, 9, 10, Tòa nhà Gilimex - 24C Phan Đăng Lưu, Phường 6, Quận Bình Thạnh, Tp. HCM");
        log.writeLogAction(req, "Khởi tạo chứng từ Hợp đồng", joiner.toString(), getFileContract.toString(), "", "", contractCode, "esign");
        System.out.println(sw.prettyPrint());
        return ok(contractFileResponse.getData());
    }

    /**
     * Send files contract to customer
     *
     * @param req ContractSendFileRequest contain info request
     * @return ContractSendFile contain result info
     */
    @PostMapping("/send_file")
    @Transactional
    public ResponseEntity<?> contractSendFile(@RequestBody RequestDTO<ContractSendFileRequest> req) {

        ContractSendFileRequest request = req.init();

        Contract contract = contractService.getById(UUID.fromString(request.getContractUuid()));
        if (contract == null) {
            throw new NotFoundException(1406, "Contract does not exits");
        }
        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contract.getLendingCoreContractId());
        //ContractInfo contractInfo = contractEsignedRequest.getContractInfo();
        if (contractInfo == null) {
            throw new NotFoundException(1406, "ContractInfo does not exits");
        }
        //write log customer
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Địa chỉ email nhận chứng từ điện tử Hợp đồng tín dụng: " + request.getEmail());
        log.writeLogAction(req, "Yêu cầu gửi bộ chứng từ Hợp đồng đến email các nhân", joiner.toString(), request.toString(), "", "", "", "esign");

        ContractSendFile contractSendFile = new ContractSendFile();
        contractSendFile.setContractUuid(contract.getContractUuid());
        contractSendFile.setCustomerUuid(UUID.fromString(request.getCustomerUuid()));
        contractSendFile.setEmail(request.getEmail());
        contractSendFile.setCreatedAt(req.now());
        contractSendFile.setStatus(0);
        contractSendFile.setIsSignedAdjustment(request.getSignType());
        List<String> attachments = new ArrayList<>();
        if (request.getSignType() == Contract.SIGN_TYPE.E_SIGN) {
            attachments = eSignedFileService.getFile(contractSendFile.getContractUuid(), Contract.FILE_TYPE.E_SIGN);
            contractSendFile.setMailType("esign_to_customer");
        }
        if (request.getSignType() == Contract.SIGN_TYPE.ADJUSTMENT) {
            attachments = eSignedFileService.getFile(contractSendFile.getContractUuid(), Contract.FILE_TYPE.ADJUSTMENT);
            contractSendFile.setMailType("adj_to_customer");
        }
        if (request.getSignType() == Contract.SIGN_TYPE.OTHER) {
            attachments.addAll(eSignedFileService.getFile(contractSendFile.getContractUuid(), Contract.FILE_TYPE.E_SIGN));
            attachments.addAll(eSignedFileService.getFile(contractSendFile.getContractUuid(), Contract.FILE_TYPE.ADJUSTMENT));
            contractSendFile.setMailType("esignadj_to_customer");
        }

        if (attachments != null && attachments.size() > 0) {
            contractSendFile.setFileName(attachments.toString());

            Locale locale = new Locale("vn", "VN");
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

            //params[customerName, contractCode]
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setListEmail(new String[]{contractSendFile.getEmail()});
            emailRequest.setListFile(attachments.toArray(new String[attachments.size()]));
            emailRequest.setLangCode(req.langCode());
            emailRequest.setEmailType(contractSendFile.getMailType());
            emailRequest.setParams(Arrays.asList(
                    ContractUtils.generateFullName(contractInfo),
                    contractInfo.getContractNumber())
                    .toArray(new String[2]));

            if (invokeEmailService_sendEmail_Success(emailRequest)) {
                contractSendFile.setStatus(1);
                // format email
                contractSendFile.setEmail(HDUtil.formatEmailSave(contractSendFile.getEmail()));
            }
        }
        contractSendFileService.create(contractSendFile);

        //write log customer
        StringJoiner joinerResult = new StringJoiner("\r\n");
        joinerResult.add("Hệ thống HD SAISON hoàn tất gửi chứng từ Hợp đồng đến email " + request.getEmail());
        log.writeLogAction(req, "Gửi email thành công", joinerResult.toString(), request.toString(), "", "", "", "esign");

        return ok(contractSendFile);

    }

    /**
     * Invoke file-handler service to create contract file
     *
     * @param fileRequest GenerateFileRequest contain info require to create contract file
     * @return GenerateContractFileResponse contain result info
     */
    GenerateContractFileResponse invokeFileHandlerS3_generate_file(GenerateFileRequest fileRequest) {
        if (fileRequest != null) {
            //upload file new
            try {
                ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/contract/generate", fileRequest,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                    GenerateContractFileResponse response = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<GenerateContractFileResponse>() {
                            });
                    return response;
                }
            } catch (IOException e) {
                Log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Invoke file-handler service to create contract adjustment file
     *
     * @param adjustmentFileRequest AdjustmentFileRequest contain info needed to generate adjustment file
     * @return GenerateContractFileResponse contain result info
     */
    GenerateContractFileResponse invokeFileHandlerS3_generate_file_adjustment(AdjustmentFileRequest adjustmentFileRequest) {
        if (adjustmentFileRequest != null) {
            //upload file new
            try {
                ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/contract/adjustment", adjustmentFileRequest,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                    GenerateContractFileResponse response = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<GenerateContractFileResponse>() {
                            });
                    return response;
                }
            } catch (IOException e) {
                Log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Invoke file-handler to download one file from aws s3 bucket
     *
     * @param uriRequest contain uri request
     * @return contain data of file downloaded
     */
    UriResponse invokeFileHandlerS3_download(UriRequest uriRequest) {
        try {
            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/download", uriRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                try {
                    UriResponse fileResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<UriResponse>() {
                            });
                    return fileResponse;
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

    /**
     * Invoke email service to send email
     *
     * @param emailRequest contain info need to send an email
     * @return result send email success or not
     */
    boolean invokeEmailService_sendEmail_Success(EmailRequest emailRequest) {
        try {
            if (emailRequest != null) {
                ResponseDTO<Object> dto = invoker.call(urlEmailRequest + "/send_s3", emailRequest,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
