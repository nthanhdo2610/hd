package com.tinhvan.hd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.file.FileS3DTORequest;
import com.tinhvan.hd.base.file.FileS3DTOResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.bean.*;
import com.tinhvan.hd.file.service.FileStorageService;
import com.tinhvan.hd.model.ConfigContractTypeBackground;
import com.tinhvan.hd.model.ConfigSendMail;
import com.tinhvan.hd.model.ConfigSendMailDtl;
import com.tinhvan.hd.model.ConfigStaff;
import com.tinhvan.hd.payload.ResultSearchSignUpPromotion;
import com.tinhvan.hd.service.*;
import com.tinhvan.hd.utils.WriteLog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/config_staff")
public class ConfigStaffController extends HDController {

    private final String logName = "Cấu hình chung";
    @Autowired
    WriteLog log;

    @Autowired
    private ConfigStaffService configStaffService;

    @Autowired
    private CacheService cacheService;


    @Autowired
    private ConfigSendMailService configSendMailService;

    @Autowired
    private ConfigSendMailDtlService configSendMailDtlService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    ConfigContractTypeBackgroundService configContractTypeBackgroundService;

    @Value("${app.module.filehandler.service.url}")
    private String urlFileHandlerRequest;

    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    @Value("${app.module.contract.service.url}")
    private String urlContractRequest;

    private Invoker invoker = new Invoker();
    private ObjectMapper mapper = new ObjectMapper();
    private Locale locale = new Locale("vn", "VN");
    private NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    /**
     * Update list ConfigStaff exist lending app
     *
     * @param req ConfigStaffUpdate contain information update
     * @return http status code and list ConfigStaff
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<ConfigStaffUpdate> req) {
        ConfigStaffUpdate configStaffUpdate = req.init();
        List<ConfigStaff> oldValues = configStaffService.list();
        List<ConfigStaff> list = configStaffUpdate.getList();
        UUID staffUUID = null;
        if (req.jwt() != null) {
            staffUUID = req.jwt().getUuid();
        }
        List<ConfigStaff> listResult = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (ConfigStaff configStaff : list) {
                configStaff.setModifiedAt(req.now());
                configStaff.setModifiedBy(staffUUID);
                configStaffService.insertOrUpdate(configStaff);
                listResult.add(configStaff);
            }
            //write log
            log.writeLogAction(req, logName, "update", configStaffUpdate.toString(), oldValues.toString(), listResult.toString(), "", "");
        }
        cacheService.forgetAboutThis(HDConstant.SYSTEM_CONFIG);

        return ok(listResult);
    }

    /**
     * get list ConfigStaff exist lending app
     *
     * @param req EmptyPayload
     * @return http status code and list ConfigStaff
     */
    @PostMapping("/list")
    public ResponseEntity<?> list(@RequestBody RequestDTO<EmptyPayload> req) {
        List<ConfigStaff> listResult = configStaffService.list();
        //write log
        log.writeLogAction(req, logName, "list", "", "", listResult.toString(), "", "");

        return ok(listResult);
    }

    /**
     * get value by key on ConfigStaff exist lending app
     *
     * @param req ConfigStaffGetValue content information getValueByKey
     * @return http status code and value
     */
    @PostMapping("/get_value")
    public ResponseEntity<?> getValueByKey(@RequestBody RequestDTO<ConfigStaffGetValue> req) {
        ConfigStaffGetValue configStaffGetValue = req.init();
        String result = configStaffService.getValueByKey(configStaffGetValue.getKey());
        //write log
        log.writeLogAction(req, logName, "get_value", configStaffGetValue.toString(), "", result, "", "invoke");
        return ok(result);
    }

    /**
     * get kind offer
     *
     * @param req IdPayload content information getKindOfferByContract
     * @return http status code and kind offer
     */
    @PostMapping("/get_kind_offer")
    public ResponseEntity<?> getKindOfferByContract(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload idPayload = req.init();
        String contractCode = (String) idPayload.getId();
        List<ConfigStaffListContractTypeRespon> list = configStaffService.getListByContractType();
        String result = getKindOffer(list, contractCode);
        //write log
        log.writeLogAction(req, logName, "get_kind_offer", idPayload.getId().toString(), "", result, "", "invoke");
        return ok(result);
    }

    /**
     * check time register eSign
     *
     * @param req TypeAndContractType
     * @return path
     */
    @PostMapping("/downloadFileByType")
    public ResponseEntity<?> downloadFileByType(@RequestBody RequestDTO<TypeAndContractType> req) {
        File result = null;
        File fileTemplate = null;
        HashMap<Object, String> data = new HashMap<>();
        try {
            TypeAndContractType typeAndContractType = req.init();
            int type = typeAndContractType.getType();
            String contractType = typeAndContractType.getContractType();
            String s3Uri = "https://hdsaison-static.s3-ap-southeast-1.amazonaws.com/";
            if (type == HDConstant.LOAN_TYPE.LOAN_FORM) {
                if (contractType.toUpperCase().equals("MC"))
                    s3Uri += "HDSaison_Template_Mail/HDSaison_Mail_DangKyVay_MC.xlsx";
                if (contractType.toUpperCase().equals("ED") || contractType.toUpperCase().equals("MB"))
                    s3Uri += "HDSaison_Template_Mail/HDSaison_Mail_DangKyVay_ED.xlsx";
                if (contractType.toUpperCase().equals("CL") || contractType.toUpperCase().equals("CLO"))
                    s3Uri += "HDSaison_Template_Mail/HDSaison_Mail_DangKyVay_CL.xlsx";
            }
            if (type == HDConstant.LOAN_TYPE.LOAN_PROMOTION) {
                if (contractType.toUpperCase().equals("MC"))
                    s3Uri += "HDSaison_Template_Mail/HDSaison_Mail_DangKyVay_UuDai_MC.xlsx";
                if (contractType.toUpperCase().equals("ED"))
                    s3Uri += "HDSaison_Template_Mail/HDSaison_Mail_DangKyVay_UuDai_ED.xlsx";
                if (contractType.toUpperCase().equals("MB"))
                    s3Uri += "HDSaison_Template_Mail/HDSaison_Mail_DangKyVay_UuDai_VeMayBay.xlsx";
                if (contractType.toUpperCase().equals("CL") || contractType.toUpperCase().equals("CLO"))
                    s3Uri += "HDSaison_Template_Mail/HDSaison_Mail_DangKyVay_UuDai_CL.xlsx";
            }
            data.put("path", s3Uri);
            fileTemplate = invokeFileHandlerS3_download(new UriRequest(s3Uri));
            if (fileTemplate == null)
                return badRequest(1252);
            ConfigSendMail configSendMail = configSendMailService.getByTypeAndContractType(type, contractType);
            List<ConfigSendMailDtl> configSendMailDtls;
            if (configSendMail == null) {
                configSendMailDtls = configSendMailDtlService.generateTemplate();
            } else {
                configSendMailDtls = configSendMailDtlService.findByConfigSendMailId(configSendMail.getId());
            }
            if (configSendMailDtls == null || configSendMailDtls.size() == 0) {
                configSendMailDtls = configSendMailDtlService.generateTemplate();
            }
            result = generateFileMailConfig(fileTemplate, configSendMailDtls);
            data.put("base64", Base64.getEncoder().encodeToString(loadFile(result)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /*if (result != null)
                result.delete();*/
            if (fileTemplate != null)
                fileTemplate.delete();
        }
        return ok(data);
    }

    //@PostMapping("/downloadFileLoanForm")
    public ResponseEntity<?> downloadFileLoanForm(@RequestBody RequestDTO<TypeAndContractType> req) {
        TypeAndContractType typeAndContractType = req.init();
        File result = null;
        File fileTemplate = null;
        ResponseDTO<Object> dto = invoker.call(urlContractRequest + "/loan/find", typeAndContractType,
                new ParameterizedTypeReference<ResponseDTO<Object>>() {
                });
        if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
            try {
                if (typeAndContractType.getType() == HDConstant.LOAN_TYPE.LOAN_FORM) {
                    List<ResultSearchSignUpLoan> signUpLoans = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<ResultSearchSignUpLoan>>() {
                            });
                    if (signUpLoans != null && signUpLoans.size() > 0) {
                        fileTemplate = invokeFileHandlerS3_download(new UriRequest("https://hdsaison-static.s3-ap-southeast-1.amazonaws.com/contract/template/HDSaison_template_LoanForm.xlsx"));
                        if (fileTemplate != null) {
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
                            if (typeAndContractType.getContractType().toUpperCase().equals("MC"))
                                result = generateReportLoanForm(fileTemplate, signUpLoans);
                            if (typeAndContractType.getContractType().toUpperCase().equals("ED") || typeAndContractType.getContractType().toUpperCase().equals("MB"))
                                result = generateReportLoanForm(fileTemplate, signUpLoans);
                            if (typeAndContractType.getContractType().toUpperCase().equals("CL") || typeAndContractType.getContractType().toUpperCase().equals("CLO"))
                                result = generateReportLoanForm(fileTemplate, signUpLoans);
                        } else {
                            System.out.println("file template is not found");
                        }
                    }
                }
                if (typeAndContractType.getType() == HDConstant.LOAN_TYPE.LOAN_PROMOTION) {
                    List<ResultSearchSignUpPromotion> signUpPromotions = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<List<ResultSearchSignUpPromotion>>() {
                            });
                    if (signUpPromotions != null && signUpPromotions.size() > 0) {
                        fileTemplate = invokeFileHandlerS3_download(new UriRequest("https://hdsaison-static.s3-ap-southeast-1.amazonaws.com/contract/template/HDSaison_template_LoanPromotion.xlsx"));
                        if (fileTemplate != null) {
                            List<ResultSearchSignUpPromotion> signUpPromotions_MC = new ArrayList<>();
                            List<ResultSearchSignUpPromotion> signUpPromotions_ED_MB = new ArrayList<>();
                            List<ResultSearchSignUpPromotion> signUpPromotions_CL_CLO = new ArrayList<>();
                            List<ResultSearchSignUpPromotion> signUpPromotions_PL = new ArrayList<>();
                            signUpPromotions.forEach(signUpPromotion -> {
                                if (signUpPromotion.getPromotionType() != null) {
                                    if (signUpPromotion.getPromotionType().toUpperCase().equals("MC"))
                                        signUpPromotions_MC.add(signUpPromotion);
                                    if (signUpPromotion.getPromotionType().toUpperCase().equals("ED") || signUpPromotion.getPromotionType().toUpperCase().equals("MB"))
                                        signUpPromotions_ED_MB.add(signUpPromotion);
                                    if (signUpPromotion.getPromotionType().toUpperCase().equals("CL") || signUpPromotion.getPromotionType().toUpperCase().equals("CLO"))
                                        signUpPromotions_CL_CLO.add(signUpPromotion);
                                    if (signUpPromotion.getPromotionType().toUpperCase().equals("PL"))
                                        signUpPromotions_PL.add(signUpPromotion);
                                }
                            });
                            if (typeAndContractType.getContractType().toUpperCase().equals("MC"))
                                result = generateReportLoanFormPromotion(fileTemplate, signUpPromotions_MC);
                            if (typeAndContractType.getContractType().toUpperCase().equals("ED") || typeAndContractType.getContractType().toUpperCase().equals("MB"))
                                result = generateReportLoanFormPromotion(fileTemplate, signUpPromotions_ED_MB);
                            if (typeAndContractType.getContractType().toUpperCase().equals("CL") || typeAndContractType.getContractType().toUpperCase().equals("CLO"))
                                result = generateReportLoanFormPromotion(fileTemplate, signUpPromotions_CL_CLO);
                            if (typeAndContractType.getContractType().toUpperCase().equals("PL"))
                                result = generateReportLoanFormPromotion(fileTemplate, signUpPromotions_PL);
                        } else {
                            System.out.println("file template is not found");
                        }
                    }
                }
                if (result != null) {
                    byte[] bytes = loadFile(result);
                    String encodedString = Base64.getEncoder().encodeToString(bytes);
                    return ok(encodedString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (result != null) {
                    result.delete();
                }
                if (fileTemplate != null) {
                    fileTemplate.delete();
                }
            }
        }
        return ok();
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    /**
     * check time register eSign
     *
     * @param req MailFilter
     * @return path
     */
    @PostMapping("/getMailListByFilter")
    public ResponseEntity<?> getMailListByFilter(@RequestBody RequestDTO<GetMailListByFilter> req) {

        GetMailListByFilter filter = req.init();

        List<MailFilter> mailFilters = filter.getFilters();
        List<String> list = new ArrayList<>();
        if (mailFilters != null && mailFilters.size() > 0) {
            for (MailFilter mailFilter : mailFilters) {
                Integer type = mailFilter.getType();

                String contractType = mailFilter.getContractType();

                String province = mailFilter.getProvince();

                String district = mailFilter.getDistrict();


                ConfigSendMail configSendMail = configSendMailService.getByTypeAndContractType(type, contractType);

                if (configSendMail != null) {

                    ConfigSendMailDtl configSendMailDtl = configSendMailDtlService.getListByConfigSendMailIdAndProAndDis(configSendMail.getId(), province, district);
                    if (configSendMailDtl != null) {
                        list.add(configSendMailDtl.getMailList());
                    }
                }
            }
        }

        return ok(list);
    }

    /**
     * check time register eSign
     *
     * @param req MailFilter
     * @return path
     */
    @PostMapping("/getMailListByFilterAndId")
    public ResponseEntity<?> getMailListByFilterAndData(@RequestBody RequestDTO<MailFilter> req) {

        MailFilter mailFilter = req.init();

        ResponseDataSendMail responseDataSendMail = new ResponseDataSendMail();
        try {

            Integer type = mailFilter.getType();

            String contractType = mailFilter.getContractType();

            String province = mailFilter.getProvince();

            String district = mailFilter.getDistrict();

            ConfigSendMail configSendMail = configSendMailService.getByTypeAndContractType(type, contractType);

            if (configSendMail != null) {

                ConfigSendMailDtl configSendMailDtl = configSendMailDtlService.getListByConfigSendMailIdAndProAndDis(configSendMail.getId(), province, district);
                if (configSendMailDtl != null && !HDUtil.isNullOrEmpty(configSendMailDtl.getMailList())) {
                    responseDataSendMail.setMail(configSendMailDtl.getMailList());
                    responseDataSendMail.setId(configSendMailDtl.getId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok(null);
    }

    /**
     * check time register eSign
     *
     * @param req EmptyPayload
     * @return http status code and object ConfigStaffCheckTimeRegisterESignResponse
     */
    @PostMapping("/check_time_register_esign")
    public ResponseEntity<?> checkTime(@RequestBody RequestDTO<EmptyPayload> req) {
        ConfigStaffCheckTimeRegisterEsignRespone result = convertValue(req.now());
        //write log
        log.writeLogAction(req, logName, "check_time_register_esign", "", "", result.toString(), "", "");
        return ok(result);
    }

    /**
     * insert send mail from excel file
     *
     * @param req file excel
     * @return http status code
     */
    @PostMapping("/insert_config_send_mail")
    public ResponseEntity<?> insertConfSendMail(@RequestBody RequestDTO<RequestConfigSendMail> req) {

        RequestConfigSendMail request = req.init();


        List<ConfigSendMailByType> list = request.getData();

        if (list == null || list.size() == 0) {
            return ok();
        }

        for (ConfigSendMailByType item : list) {

            List<FileAndType> fileAndTypes = item.getFileByTypes();

            if (fileAndTypes == null || fileAndTypes.size() <= 0) {
                continue;
            }

            Integer type = item.getType();

            for (FileAndType fileAndType : fileAndTypes) {
                String pathNew = fileAndType.getPath();
                String contractType = fileAndType.getType();
                ConfigSendMail configSendMail = configSendMailService.getByTypeAndContractType(type, contractType);

                if (configSendMail == null) {
                    configSendMail = new ConfigSendMail();
                    configSendMail.setCreatedAt(new Date());
                    configSendMail.setType(type);
                    configSendMail.setContractType(contractType);
                } else {
                    configSendMail.setUpdatedAt(new Date());
                }

                configSendMailService.saveOrUpdate(configSendMail);

                String pathOld = configSendMail.getPathFile();

                String pathFile = "";
                if (HDUtil.isNullOrEmpty(pathOld)) {

                    pathFile = invokeFileHandlerS3_upload(configSendMail, pathNew, "");
                }

                if (!HDUtil.isNullOrEmpty(pathOld) && !HDUtil.isNullOrEmpty(pathNew) && !pathOld.equals(pathNew)) {
                    pathFile = invokeFileHandlerS3_upload(configSendMail, pathNew, pathOld);
                }

                if (HDUtil.isNullOrEmpty(pathFile)) {
                    continue;
                }

                File fileFilter = null;
                FileInputStream fileIS = null;
                Workbook workbookIn = null;
                // detach file ex
                try {
                    fileFilter = invokeFileHandlerS3_download(new UriRequest(pathFile));
                    if (fileFilter == null) {
                        throw new InternalServerErrorException("configSendMail.getId().toString() + \": file filter not found\"");
                    }

                    fileIS = new FileInputStream(fileFilter);
                    workbookIn = new XSSFWorkbook(fileIS);
                    Sheet sheetIn = workbookIn.getSheetAt(0);
                    Iterator<Row> rowIteratorIn = sheetIn.iterator();
                    List<ConfigSendMailDtl> configSendMailDtls = new ArrayList<>();
                    while (rowIteratorIn.hasNext()) {
                        Row rowIn = rowIteratorIn.next();

                        if (rowIn.getRowNum() >= 6) {
                            Iterator<Cell> cellIteratorIn = rowIn.iterator();
                            Double stt = null;
                            String province = "";
                            String district = "";
                            String listMail = "";
                            while (cellIteratorIn.hasNext()) {
                                Cell cellIn = cellIteratorIn.next();

                                if (stt == null) {
                                    stt = cellIn.getNumericCellValue();
                                    continue;
                                }
                                if (HDUtil.isNullOrEmpty(province)) {
                                    if (cellIn.getCellType() == CellType.STRING) {
                                        province = cellIn.getStringCellValue();
                                    } else {
                                        province = String.valueOf(cellIn.getNumericCellValue());
                                    }

                                    continue;
                                }

                                if (HDUtil.isNullOrEmpty(district)) {
                                    if (cellIn.getCellType() == CellType.STRING) {
                                        district = cellIn.getStringCellValue();
                                    } else {
                                        district = String.valueOf(cellIn.getNumericCellValue());
                                    }
                                    continue;
                                }

                                if (HDUtil.isNullOrEmpty(listMail)) {
                                    listMail = cellIn.getStringCellValue();
                                    continue;
                                }
                            }
                            ConfigSendMailDtl dtl = configSendMailDtlService.getListByConfigSendMailIdAndProAndDis(configSendMail.getId(), province, district);

                            if (dtl == null) {
                                dtl = new ConfigSendMailDtl();
                                dtl.setCreatedAt(new Date());
                                dtl.setConfigSendMailId(configSendMail.getId());
                                dtl.setDistrict(district);
                                dtl.setProvince(province);
                                dtl.setMailList(listMail);
                            } else {
                                dtl.setMailList(listMail);
                                dtl.setUpdatedAt(new Date());
                            }

                            configSendMailDtls.add(dtl);
                        }
                    }
                    workbookIn.close();
                    fileIS.close();

                    if (configSendMailDtls != null && configSendMailDtls.size() > 0) {
                        configSendMailDtlService.saveOrUpdateAll(configSendMailDtls);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new InternalServerErrorException();
                }
            }
        }
        return ok(null);
    }

    /**
     * get kind offer on lending app
     *
     * @param list
     * @param contractCode
     * @return kind offer
     */
    private String getKindOffer(List<ConfigStaffListContractTypeRespon> list, String contractCode) {
        boolean check = false;
        String result = "ED";
        if (list != null && !list.isEmpty()) {
            for (ConfigStaffListContractTypeRespon cs : list) {
                String[] arr = cs.getValue().split(";");
                for (int i = 0; i < arr.length; i++) {
                    if (contractCode.startsWith(arr[i])) {
                        switch (cs.getKey()) {
                            case "contract_type_clo_begin_with":
                                result = "CLO";
                                check = true;
                                break;
                            case "contract_type_cl_begin_with":
                                result = "CL";
                                check = true;
                                break;
                            case "contract_type_ed_begin_with":
                                result = "ED";
                                check = true;
                                break;
                            case "contract_type_mc_begin_with":
                                result = "MC";
                                check = true;
                                break;
                            default:
                                break;
                        }
                        if (check)
                            return result;
                    }
                }

            }
        }
        return result;
    }

    /**
     * convert date values eSign_from and eSign_to
     *
     * @param dateRequest
     * @return object ConfigStaffCheckTimeRegisterEsignRespone
     */
    private ConfigStaffCheckTimeRegisterEsignRespone convertValue(Date dateRequest) {
        String esignFrom = configStaffService.getValueByKey("esign_from");
        String[] arrEsignFrom = esignFrom.split(":");
        ConvertValue convertValueEsignFrom = null, convertValueEsignTo = null;
        if (arrEsignFrom.length == 2)
            convertValueEsignFrom = new ConvertValue(Integer.parseInt(arrEsignFrom[0]), Integer.parseInt(arrEsignFrom[1]));
        String esignTo = configStaffService.getValueByKey("esign_to");
        String[] arrEsignTo = esignTo.split(":");
        if (arrEsignTo.length == 2)
            convertValueEsignTo = new ConvertValue(Integer.parseInt(arrEsignTo[0]), Integer.parseInt(arrEsignTo[1]));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, dateRequest.getDate());
        if (convertValueEsignFrom != null) {
            c.set(Calendar.HOUR_OF_DAY, convertValueEsignFrom.getHour());
            c.set(Calendar.MINUTE, convertValueEsignFrom.getMinute());
        }
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date dateFrom = c.getTime();
        if (convertValueEsignTo != null) {
            c.set(Calendar.HOUR_OF_DAY, convertValueEsignTo.getHour());
            c.set(Calendar.MINUTE, convertValueEsignTo.getMinute());
        }
        Date dateTo = c.getTime();
        //dateFrom > dateTo
        if (dateFrom.after(dateTo)) {
            c.set(Calendar.DAY_OF_MONTH, dateRequest.getDate() + 1);
            dateTo = c.getTime();
        }
        ConfigStaffCheckTimeRegisterEsignRespone checkTimeRegisterEsign;
        if (dateRequest.before(dateTo) && dateRequest.after(dateFrom)) {
            checkTimeRegisterEsign = new ConfigStaffCheckTimeRegisterEsignRespone(1, esignFrom, esignTo);
        } else {
            checkTimeRegisterEsign = new ConfigStaffCheckTimeRegisterEsignRespone(0, esignFrom, esignTo);
        }
        return checkTimeRegisterEsign;
    }


    /**
     * Invoke file-handler service to upload file of ContractFileTemplate
     *
     * @param configSendMail info of ContractFileTemplate
     * @param fileNew        file need to upload
     * @param fileOld        file old need to delete and replace by fileNew
     * @return result of function is success or not
     */
    String invokeFileHandlerS3_upload(ConfigSendMail configSendMail, String fileNew, String fileOld) {

        String path = "";
        if (!HDUtil.isNullOrEmpty(fileOld)) {
            //create object to request upload s3 server
            FileS3DTOResponse s3DTO = new FileS3DTOResponse();
            List<FileS3DTOResponse.FileRep> lst = new ArrayList<>();
            FileS3DTOResponse.FileRep fileRep = new FileS3DTOResponse.FileRep();
            fileRep.setUri(fileOld);
            lst.add(fileRep);
            s3DTO.setFiles(lst);

            //delete file old
            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/delete", s3DTO,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto.getCode() != HttpStatus.OK.value()) {
                configSendMail.setPathFile(fileOld);
                configSendMailService.saveOrUpdate(configSendMail);

                path = fileOld;
            }
        }
        if (!HDUtil.isNullOrEmpty(fileNew)) {
            //create object to request upload s3 server
            FileS3DTORequest s3DTO = new FileS3DTORequest();
            List<FileS3DTORequest.FileReq> lst = new ArrayList<>();
            String b64 = fileStorageService.loadFileAsBase64(fileNew);
            lst.add(new FileS3DTORequest.FileReq(String.valueOf(configSendMail.getId()),
                    "", "config_send_mail_upload_file",
                    MimeTypes.lookupMimeType(FilenameUtils.getExtension(fileNew)),
                    b64, fileOld));
            s3DTO.setFiles(lst);

            //upload file new
            ObjectMapper mapper = new ObjectMapper();
            Invoker invoker = new Invoker();
            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/upload", s3DTO,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });

            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                try {
                    FileS3DTOResponse fileResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<FileS3DTOResponse>() {
                            });
                    if (fileResponse.getFiles().size() > 0) {
                        configSendMail.setPathFile(fileResponse.getFiles().get(0).getUri());
                        path = fileResponse.getFiles().get(0).getUri();
                    } else {
                        configSendMail.setPathFile("");
                    }
                } catch (IOException e) {
                    configSendMail.setPathFile("");
                }
            } else {
                configSendMail.setPathFile("");
            }
            fileStorageService.deleteFile(fileNew);
            configSendMailService.saveOrUpdate(configSendMail);
            if (HDUtil.isNullOrEmpty(configSendMail.getPathFile())) {
                return "";
            }
        }
        return path;
    }


    /**
     * Invoke file-handler service to download a file
     *
     * @param uriRequest contain uri of file s3
     * @return a file after downloaded
     */
    File invokeFileHandlerS3_download(UriRequest uriRequest) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/download", uriRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                try {
                    UriResponse fileResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<UriResponse>() {
                            });
                    if (!HDUtil.isNullOrEmpty(fileResponse.getData())) {
                        String fileNameIn = "SEND_MAIL_FILTER_" + UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + "." + FilenameUtils.getExtension(uriRequest.getUri());
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

    /**
     * Create file report contain info sign up loan form
     *
     * @param file          template to create file report
     * @param resultSearchs list ResultSearchSignUpLoan need info to create file report
     * @return file report created successfully
     */
    File generateReportLoanForm(File file, List<ResultSearchSignUpLoan> resultSearchs) {
        try {
            List<ConfigContractTypeBackground> typeBackgrounds = configContractTypeBackgroundService.list();
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

                /*row.createCell(3).setCellStyle(c.getCellStyle());
                if (result.getNationalId() != null)
                    row.getCell(3).setCellValue(result.getNationalId());*/

                row.createCell(3).setCellStyle(c.getCellStyle());
                if (result.getProductionName() != null)
                    row.getCell(3).setCellValue(result.getProductionName());

                row.createCell(4).setCellStyle(c.getCellStyle());
                if (result.getLoanAmount() != null)
                    row.getCell(4).setCellValue(numberFormatter.format(result.getLoanAmount()));

                row.createCell(5).setCellStyle(c.getCellStyle());
                row.getCell(5).setCellValue(result.getTenor());

                row.createCell(6).setCellStyle(c.getCellStyle());
                if (result.getProvinceCode() != null)
                    row.getCell(6).setCellValue(result.getProvinceCode());

                row.createCell(7).setCellStyle(c.getCellStyle());
                if (result.getDistrictCode() != null)
                    row.getCell(7).setCellValue(result.getDistrictCode());

                row.createCell(8).setCellStyle(c.getCellStyle());
                if (result.getLoanType() != null) {
                    for (ConfigContractTypeBackground type : typeBackgrounds) {
                        if (type.getContractType().toUpperCase().equals(result.getLoanType().toUpperCase())) {
                            row.getCell(8).setCellValue(type.getContractName());
                            break;
                        }
                    }
                }

                row.createCell(9).setCellStyle(c.getCellStyle());
                if (result.getCreatedAt() != null)
                    row.getCell(9).setCellValue(df.format(result.getCreatedAt()));

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
            List<ConfigContractTypeBackground> typeBackgrounds = configContractTypeBackgroundService.list();
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
                if (result.getPromotionCode() != null)
                    row.getCell(1).setCellValue(result.getPromotionCode());

                row.createCell(2).setCellStyle(c.getCellStyle());
                if (result.getTitle() != null)
                    row.getCell(2).setCellValue(result.getTitle());

                row.createCell(3).setCellStyle(c.getCellStyle());
                if (result.getPromotionType() != null) {
                    for (ConfigContractTypeBackground type : typeBackgrounds) {
                        if (type.getContractType().toUpperCase().equals(result.getPromotionType().toUpperCase())) {
                            row.getCell(3).setCellValue(type.getContractName());
                            break;
                        }
                    }
                }

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
                if (result.getCreateAt() != null)
                    row.getCell(8).setCellValue(df.format(result.getCreateAt()));

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

    File generateFileMailConfig(File file, List<ConfigSendMailDtl> resultSearchs) {
        try {
            FileInputStream fileIS = new FileInputStream(file);
            Workbook workbookIn = new XSSFWorkbook(fileIS);
            Sheet sheetIn = workbookIn.getSheetAt(0);
            Cell c = sheetIn.getRow(3).getCell(1);
            int rowIn = sheetIn.getLastRowNum() + 1;
            for (int i = 0; i < resultSearchs.size(); i++) {
                ConfigSendMailDtl result = resultSearchs.get(i);
                Row row = sheetIn.createRow(rowIn + i);
                row.createCell(0).setCellStyle(c.getCellStyle());
                row.getCell(0).setCellValue(i + 1);
                row.createCell(1).setCellStyle(c.getCellStyle());
                if (result.getProvince() != null)
                    row.getCell(1).setCellValue(result.getProvince());
                row.createCell(2).setCellStyle(c.getCellStyle());
                if (result.getDistrict() != null)
                    row.getCell(2).setCellValue(result.getDistrict());
                row.createCell(3).setCellStyle(c.getCellStyle());
                if (result.getMailList() != null)
                    row.getCell(3).setCellValue(result.getMailList());
            }
            c.setCellStyle(null);
            String result = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getName());
            FileOutputStream fileOS = new FileOutputStream(result);
            workbookIn.write(fileOS);
            workbookIn.close();
            fileIS.close();
            fileOS.close();
            return new File(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
