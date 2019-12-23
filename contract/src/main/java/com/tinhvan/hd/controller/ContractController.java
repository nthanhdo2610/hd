package com.tinhvan.hd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.*;
import com.tinhvan.hd.entity.enumtype.ContractStatus;
import com.tinhvan.hd.service.*;
import com.tinhvan.hd.utils.ConstantStatus;
import com.tinhvan.hd.utils.ContractUtils;
import com.tinhvan.hd.utils.DateUtils;
import com.tinhvan.hd.utils.WriteLog;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.tinhvan.hd.utils.ConstantStatus.WAIT_FOR_SIGNING_CONTRACT;
import static com.tinhvan.hd.utils.ContractUtils.splitStatus;

/**
 * @author tuongnk on 7/9/2019
 * @project Contract
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/contract")
public class ContractController extends HDController {

    private static final String[] CHAR_UPPER = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Value("${service.config_staff.endpoint}")
    private String urlConfigStaffRequest;
    @Value("${service.config_adjustment_contract.endpoint}")
    private String configAdjustmentContract;
    @Value("${service.config_contract_type_background.endpoint}")
    private String configContractTypeBackgroundRequest;
    @Value("${service.customer.endpoint}")
    private String urlCustomerRequest;
    @Value("${service.staff.endpoint}")
    private String urlStaffRequest;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractCustomerService contractCustomerService;
    @Autowired
    private HDMiddleService hdMiddleService;
    @Autowired
    private ContractLogStatusService contractLogStatusService;
    @Autowired
    private ContractAdjustmentInfoService contractAdjustmentInfoService;
    @Autowired
    private ContractFileTemplateService contractFileTemplateService;
    @Autowired
    private ContractEsignedFileService eSignedFileService;

    @Autowired
    private ContractEsignedService contractEsignedService;

    @Autowired
    WriteLog log;

    @Autowired
    private ContractEditInfoService contractEditInfoService;

    /**
     * Customer fill info and click register new account
     *
     * @param req ContractRegister contain info need to create a new customer
     * @return VerifyResponse contain info of customer register successfully
     */
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> registerByContractAndIdentify(@RequestBody RequestDTO<ContractRegister> req) {

        UUID customerUuid = null;
        VerifyResponse verifyResponse = new VerifyResponse();
        ContractRegister contractRegister = req.init();
        String contractCode = contractRegister.getContractCode();
        String identifyId = contractRegister.getIdentifyId();

        //write log
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Khách hàng nhập thông tin và nhấn chọn nút Đăng ký");
        joiner.add("- Số hợp đồng: " + contractCode);
        joiner.add("- Số CMND/CCCD: " + identifyId);
        log.writeLogAction(req, "Nhập thông tin đăng ký tài khoản", joiner.toString(), contractRegister.toString(), "", "", contractCode, "register");


        Contract contract = contractService.getContractByContractCode(contractCode);

        Invoker invoker = new Invoker();
        if (contract != null) {
            List<ContractCustomer> contractCustomers = contractCustomerService.getListContractCustomerByContractUuid(contract.getContractUuid());

            if (contractCustomers == null || contractCustomers.isEmpty()) {
                throw new NotFoundException(1401, "Contract customer does not exits");
            }

            if (contractCustomers.size() > 1) {
                JSONArray jsArray2 = new JSONArray(contractCustomers);
                Log.error("ContractCustomer", jsArray2.toString());
            }
            customerUuid = contractCustomers.get(0).getCustomerUuid();


            IdPayload idPayload = new IdPayload();
            idPayload.setId(customerUuid);
            ObjectMapper mapper = new ObjectMapper();
            Integer status = 0;
            String userNameValid = "";
            try {
                ResponseDTO<Object> dto = invoker.call(urlCustomerRequest + "/valid_customer", idPayload, new ParameterizedTypeReference<ResponseDTO<Object>>() {
                });
                if (dto.getCode() == HttpStatus.OK.value()) {
                    ValidCustomer validCustomer = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<ValidCustomer>() {
                            });
                    status = validCustomer.getStatus();
                    userNameValid = validCustomer.getUserName();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (status == 0) {
                throw new BadRequestException(1128, "Customer does not exits !");
            }

            if (status == 1) {
                throw new BadRequestException(1112, "Customer already exist !");
            }
            verifyResponse.setCustomerUuid(customerUuid);
            if (status == -1) {
                HDContractResponse contractResponse = hdMiddleService.getContractByContractCodeAndIdentifyId(contractCode, identifyId);
                if (contractResponse == null) {
                    throw new NotFoundException(1400, "Contract Code does not exits !");
                }

                List<String> contractCodes = new ArrayList<>();

                List<ContractsByCustomerUuid> contractsByCustomerUuids = contractService.getListContractByCustomerUuid(customerUuid);

                for (ContractsByCustomerUuid item : contractsByCustomerUuids) {
                    if (item.getContractCustomerStatus() == 0) {
                        continue;
                    }
                    contractCodes.add(item.getContractCode());
                }

                String phone = getPhoneVerify(contractCodes);
                verifyResponse.setPhoneNumber(phone);
                verifyResponse.setUserName(userNameValid);

                //write log new customer (-1 == 1)
                StringJoiner joinerNew = new StringJoiner("\r\n");
                joinerNew.add("Hệ thống HD SAISON khởi tạo tài khoản");
                joinerNew.add("Họ và tên: ");
                joinerNew.add("Tên tài khoản: ");
                log.writeLogAction(req, "Khởi tạo tài khoản", joinerNew.toString(), contractRegister.toString(), "", "", contractCode, "register");

                return ok(verifyResponse);
            }
        }

        List<String> phones = new ArrayList<>();
        // Call procedure get Contract by contract code and identifyId
        HDContractResponse hdContractResponse = hdMiddleService.getContractByContractCodeAndIdentifyId(contractCode, identifyId);

        if (hdContractResponse == null) {
            throw new NotFoundException(1400, "Contract Code does not exits !");
        }

        // set document verify and contract printing if null
        setDocVerifyAndContractPrinting(hdContractResponse);

        List<HDContractResponse> contractResponseList = new ArrayList<>();

        String phoneOtp = hdContractResponse.getPhoneNumber();
        Date lastDayUpdatePhone = hdContractResponse.getLastUpdateApplicant();

        List<String> lstContractCodeExits = new ArrayList<>();

        contractResponseList.add(hdContractResponse);

        ValidateContract validateContract = new ValidateContract();
        validateContract.setDriversLicence(hdContractResponse.getDriversLicence());
        validateContract.setFamilyBookNo(hdContractResponse.getFamilyBookNo());
        validateContract.setPhone(hdContractResponse.getPhoneNumber());
        validateContract.setFirstName(hdContractResponse.getFirstName());
        validateContract.setLastName(hdContractResponse.getLastName());
        validateContract.setIdentifyId(hdContractResponse.getNationalID());
        validateContract.setBirthday(hdContractResponse.getDob());

        phones.add(hdContractResponse.getPhoneNumber());

        // list contractCode
        lstContractCodeExits.add(contractCode);

        // Call procedure get list contract by identifyId
        List<String> identifyIds = new ArrayList<>();
        identifyIds.add(identifyId);

        if (!identifyId.equals(hdContractResponse.getNationalID())) {
            identifyIds.add(hdContractResponse.getNationalID());
        }

        HDContractResponse checkPhoneNotify = hdContractResponse;
        for (String nationalId : identifyIds) {
            List<HDContractResponse> contractResponses = hdMiddleService.getListContractByIdentifyId(nationalId);
            if (contractResponses != null && !contractResponses.isEmpty()) {

                for (HDContractResponse response : contractResponses) {

                    if (!lstContractCodeExits.isEmpty() && lstContractCodeExits.contains(response.getContractNumber())) {
                        continue;
                    }

                    // check contract by phone,name,birthday,familyBookNo and driversLicence
                    if (!checkContractByNameOrPhoneOrBirthdayOrFamilyBookNo(validateContract, response, false)) {
                        continue;
                    }

                    lstContractCodeExits.add(response.getContractNumber());

                    // set document verify and contract printing if null
                    setDocVerifyAndContractPrinting(response);

                    contractResponseList.add(response);

                    checkPhoneNotify = compareLastUpdatePhone(checkPhoneNotify, response);

                    if (!phones.contains(response.getPhoneNumber())) {
                        phones.add(response.getPhoneNumber());
                    }
                }
            }
        }

        // get phone from middle db
        for (String phone : phones) {
            List<HDContractResponse> listContractByPhone = hdMiddleService.getListContractByPhoneNumber(phone);
            if (listContractByPhone != null && listContractByPhone.size() > 0) {
                for (HDContractResponse responseContract : listContractByPhone) {

                    // if validate true then insert contract
//                    if (!insertContract(responseContract,customerUuid,lstContractCodeExits,validateContract)) {
//                        continue;
//                    }
                    if (!lstContractCodeExits.isEmpty() && lstContractCodeExits.contains(responseContract.getContractNumber())) {
                        continue;
                    }

                    // check contract by phone,name,birthday,familyBookNo and driversLicence
                    if (!checkContractByNameOrPhoneOrBirthdayOrFamilyBookNo(validateContract, responseContract, true)) {
                        continue;
                    }

                    contractResponseList.add(responseContract);

                    // set document verify and contract printing if null
                    setDocVerifyAndContractPrinting(responseContract);

                    checkPhoneNotify = compareLastUpdatePhone(checkPhoneNotify, responseContract);
                    lstContractCodeExits.add(responseContract.getContractNumber());
                }
            }
        }


        Config config = HDConfig.getInstance();
        String[] configDate = config.getList("CHECK_VERIFY_DATE_REGISTER_CONTRACT");

        String dateCheckDocumentVerification = configDate[0] + " 00:00:00";
        String dateCheckPrinting = configDate[1] + " 00:00:00";

        Date dateCheckDocumentVerificationFm = DateUtils.covertStringToDate(dateCheckDocumentVerification);
        Date dateCheckPrintingFm = DateUtils.covertStringToDate(dateCheckPrinting);
        boolean isCheckRegisterContract = false;
        List<HDContractResponse> contractInserts = new ArrayList<>();
        for (HDContractResponse item : contractResponseList) {
            Date documentVerification = DateUtils.covertStringToDate(item.getDocumentVerificationDate());
            Date printingDate = DateUtils.covertStringToDate(item.getContractPrintingDate());

            if (documentVerification == null && printingDate == null) {
                continue;
            }
            contractInserts.add(item);
        }

        for (HDContractResponse contractResponse : contractInserts) {
            Date documentVerification = DateUtils.covertStringToDate(contractResponse.getDocumentVerificationDate());
            Date printingDate = DateUtils.covertStringToDate(contractResponse.getContractPrintingDate());

            if (documentVerification != null) {
                if (documentVerification.after(dateCheckDocumentVerificationFm) ||
                        documentVerification.equals(dateCheckDocumentVerificationFm)){
                    isCheckRegisterContract = true;
                    break;
                }
            }

            if (printingDate != null) {
                if (printingDate.after(dateCheckPrintingFm) ||
                        printingDate.equals(dateCheckPrintingFm)){
                    isCheckRegisterContract = true;
                    break;
                }
            }
        }

        if (!isCheckRegisterContract) {
            throw new NotFoundException(1436, "Chưa đăng ký sử dụng dịch vụ điện tử !");
        }

        // insert customer
        String userName = "";
        String name = hdContractResponse.getFirstName().toUpperCase();

        String[] b = name.split(" ");

        name = HDUtil.unAccent(b[b.length - 1]);

        name = HDUtil.unAccent(name);
        String subString = name.substring(1);
        name = name.substring(0, 1) + subString.toLowerCase();
        userName = hdContractResponse.getNationalID() + name;

        String nationalId = "";
        if (hdContractResponse.getNationalID().length() == 9) {
            nationalId = HDUtil.maskNumber(hdContractResponse.getNationalID(), "******###");
        } else {
            nationalId = HDUtil.maskNumber(hdContractResponse.getNationalID(), "********####");
        }

        String userNameFm = nationalId + name;

        CustomerSignUp customerSignUp = new CustomerSignUp();
        customerSignUp.setUsername(userName);
        customerSignUp.setPhoneNumber(hdContractResponse.getPhoneNumber());
        customerSignUp.setUserNameFm(userNameFm);
        ResponseDTO<Object> dto = invoker.call(urlCustomerRequest + "/sign_up", customerSignUp, new ParameterizedTypeReference<ResponseDTO<Object>>() {
        });

        int i = 0;
        while (dto.getCode() == 1112) {
            userName = CHAR_UPPER[i] + userName;
            customerSignUp.setUsername(userName);
            dto = invoker.call(urlCustomerRequest + "/sign_up", customerSignUp, new ParameterizedTypeReference<ResponseDTO<Object>>() {
            });
            i++;
        }

        if (dto.getCode() != 1112 && dto.getCode() != 200) {
            throw new InternalServerErrorException(1404, "Insert customer error !");
        }
//
        String strCustomerUuid = (String) dto.getPayload();
        customerUuid = UUID.fromString(strCustomerUuid);


        // insert contract
        for (HDContractResponse contractResponseInsert : contractInserts) {
            if (!insertContract(contractResponseInsert, customerUuid, null, null)) {
                throw new InternalServerErrorException(1405, "Insert contract error !");
            }
        }

        phoneOtp = checkPhoneNotify.getPhoneNumber();
        verifyResponse.setCustomerUuid(customerUuid);
        verifyResponse.setPhoneNumber(phoneOtp);
        verifyResponse.setUserName(userName);

        //write log customer
        log.writeLogAction(req, "Xác thực thông tin đăng ký", "Đúng thông tin đăng ký", contractRegister.toString(), "", "", contractCode, "register");

        return ok(verifyResponse);
    }

    /**
     * Verify info customer request register
     *
     * @param hdContractResponse HDContractResponse contain info result of middle response
     */
    public void setDocVerifyAndContractPrinting(HDContractResponse hdContractResponse) {
        String documentVerification = hdContractResponse.getDocumentVerificationDate();
        String printingDate = hdContractResponse.getContractPrintingDate();
        if (documentVerification == null && printingDate == null) {
            PhoneAndStatus phoneAndStatus = hdMiddleService.getPhoneAndStatusByContractCode(hdContractResponse.getContractNumber());
            if (phoneAndStatus != null) {
                hdContractResponse.setDocumentVerificationDate(phoneAndStatus.getDocumentVerificationDate());
                hdContractResponse.setContractPrintingDate(phoneAndStatus.getContractPrintingDate());
            }
        }
    }

    /**
     * Set phone receipt otp of customer
     *
     * @param phoneLastUpdate
     * @param lastUpdatePhone
     * @param lastUpdateCurrent
     * @param phoneCurrent
     * @return phone number is valid
     */
    public String setPhoneOTP(String phoneLastUpdate, Date lastUpdatePhone, Date lastUpdateCurrent, String phoneCurrent) {
        String phoneOtp = "";
        try {
            if (lastUpdateCurrent == null && lastUpdatePhone != null) {
                phoneOtp = phoneLastUpdate;
            }

            if (lastUpdatePhone != null && lastUpdateCurrent != null && lastUpdatePhone.before(lastUpdateCurrent)) {
                phoneOtp = phoneCurrent;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneOtp;
    }

    /**
     * Check phone number last updated of customer
     *
     * @param current current contract of customer
     * @param contractCheck contract need to check
     * @return info of contract if valid last update phone number
     */
    public HDContractResponse compareLastUpdatePhone(HDContractResponse current, HDContractResponse contractCheck) {

        try {
            if (current.getLastUpdateApplicant() == null && contractCheck.getLastUpdateApplicant() != null) {
                current = contractCheck;
            }

            if (current.getLastUpdateApplicant() != null && contractCheck.getLastUpdateApplicant() != null
                    && current.getLastUpdateApplicant().before(contractCheck.getLastUpdateApplicant())) {
                current = contractCheck;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return current;
    }

    /**
     * Find current phone number is valid of customer
     *
     * @param req IdPayload contain id of customer
     * @return phone number
     */
    @PostMapping("/getPhoneByCustomerUuid")
    public ResponseEntity<?> getPhoneByCustomerUuid(@RequestBody RequestDTO<IdPayload> req) {
        String phone = "";
        IdPayload idPayload = req.init();

        String strId = (String) idPayload.getId();

        UUID customerUuid = UUID.fromString(strId);

        //List<ContractCustomer> contractCustomers = contractCustomerService.getListContractCustomerByCustomerUuidAndStatus(customerUuid, 1);

        List<String> contractCodes = new ArrayList<>();

        List<ContractsByCustomerUuid> contractsByCustomerUuids = contractService.getListContractByCustomerUuid(customerUuid);

        if (contractsByCustomerUuids == null || contractsByCustomerUuids.isEmpty()) {
            throw new NotFoundException(1401, "Contract customer does not exits");
        }


        for (ContractsByCustomerUuid item : contractsByCustomerUuids) {
            if (item.getContractCustomerStatus() == 0) {
                continue;
            }
            contractCodes.add(item.getContractCode());
        }


        //phone = getPhoneCurrentByCustomer(contractCustomers);
        phone = getPhoneVerify(contractCodes);

        return ok(phone);
    }

    /**
     * Verify phone number is valid of list contract
     *
     * @param contractCodes list of contract code
     * @return phone number
     */
    public String getPhoneVerify(List<String> contractCodes) {
        String phone = "";
        List<PhoneAndStatus> phoneAndStatuses = hdMiddleService.getPhoneAndStatusByContractCodes(contractCodes);
        PhoneAndStatus phoneAndLastUpdate = null;
        if (phoneAndStatuses != null && phoneAndStatuses.size() > 0) {
            for (PhoneAndStatus phoneAndStatus : phoneAndStatuses) {
                if (phoneAndLastUpdate == null) {
                    phoneAndLastUpdate = phoneAndStatus;
                    continue;
                }
                phoneAndLastUpdate = getPhoneCurrentByLastUpdate(phoneAndLastUpdate, phoneAndStatus);
            }
        }

        if (phoneAndLastUpdate != null) {
            phone = phoneAndLastUpdate.getPhoneNumber();
        }
        return phone;
    }

    public String getPhoneCurrentByCustomer(List<ContractCustomer> contractCustomers) {
        PhoneAndStatus phoneAndLastUpdate = null;
        String phone = "";
        try {

            for (ContractCustomer contractCustomer : contractCustomers) {
                Contract con = contractService.getById(contractCustomer.getContractUuid());
                if (con != null) {
                    PhoneAndStatus phoneAndStatus = hdMiddleService.getPhoneAndStatusByContractCode(con.getLendingCoreContractId());
                    if (phoneAndLastUpdate == null) {
                        phoneAndLastUpdate = phoneAndStatus;
                        continue;
                    }

                    phoneAndLastUpdate = getPhoneCurrentByLastUpdate(phoneAndLastUpdate, phoneAndStatus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (phoneAndLastUpdate != null) {
            phone = phoneAndLastUpdate.getPhoneNumber();
        }

        return phone;
    }

    /**
     * Find phone number of customer last updated
     *
     * @param current PhoneAndStatus contain info of current phone number
     * @param phoneAndLastDate contain info of phone number need to compare
     * @return
     */
    public PhoneAndStatus getPhoneCurrentByLastUpdate(PhoneAndStatus current, PhoneAndStatus phoneAndLastDate) {
        try {
            if (current.getLastUpdatePhone() == null && phoneAndLastDate.getLastUpdatePhone() != null) {
                current = phoneAndLastDate;
            }

            if (current.getLastUpdatePhone() != null && phoneAndLastDate.getLastUpdatePhone() != null && current.getLastUpdatePhone().before(phoneAndLastDate.getLastUpdatePhone())) {
                current = phoneAndLastDate;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return current;
    }

    /**
     * Find list contract
     *
     * @param req
     * @return list of contract
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllContractByFilter(@RequestBody RequestDTO<EmptyPayload> req) {
        List<ContractResponse> contractResponses = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
            List<Contract> contracts = contractService.getListContractInfo(pageable);
            List<String> contractCodes = new ArrayList<>();
            if (contracts != null && !contracts.isEmpty()) {
                for (Contract contract : contracts) {
                    contractCodes.add(contract.getLendingCoreContractId());
                }
            }

            if (contractCodes.size() > 0) {
                List<ContractInfo> contractInfos = getContractInfos(contractCodes);
                if (contractInfos == null || contractCodes.size() < 1) {
                    throw new InternalServerErrorException();
                }
                contractResponses = getContractResponseByContracts(contracts, contractInfos, contractCodes);
            }
        } catch (Exception ex) {
            throw new InternalServerErrorException();
        }
        return ok(contractResponses);
    }

//    @PostMapping("/list_contract_dashboard")
//    public ResponseEntity<?> getAllContractByFilter(@RequestBody RequestDTO<EmptyPayload> req){
//        List<ContractResponse> contractResponses = new ArrayList<>();
//        try {
//            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
//            List<Contract> contracts = contractService.getListContractInfo(pageable);
//            if (contracts != null && !contracts.isEmpty()) {
//                for (Contract contract : contracts) {
//                    contractResponses.add(getContractResponseByContract(contract));
//                }
//            }
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(contractResponses);
//    }

    /**
     * Find list of contract by status
     *
     * @param req
     * @return list of contract
     */
    @PostMapping("/count_dashboard")
    public ResponseEntity<?> countAllContractByStatus(@RequestBody RequestDTO<EmptyPayload> req) {

        DashBoardContract boardContract;
        List<String> statuses = new ArrayList<>();
        statuses.addAll(splitStatus(WAIT_FOR_SIGNING_CONTRACT));


        boardContract = contractService.getCountContractForDashBoard(statuses);

        int totalContractESigned = contractService.countContractWaitingEsign();

        boardContract.setTotalWaitingSign(totalContractESigned);

        SearchSignUpLoan search = new SearchSignUpLoan();
        search.setIsSent(-1);
        search.setPageNum(1);
        search.setPageSize(1);

        List<ResultSearchSignUpLoan> resultSearchSignUpLoans = hdMiddleService.searchSignUpLoan(search);

        if (resultSearchSignUpLoans != null && resultSearchSignUpLoans.size() > 0) {
            boardContract.setTotalSignUpLoan(resultSearchSignUpLoans.get(0).getTotal());
        } else {
            boardContract.setTotalSignUpLoan(0);
        }

        return ok(boardContract);
    }

    /**
     * Count number of contract
     *
     * @param req
     * @return total contracts
     */
    @PostMapping("/num")
    public ResponseEntity<?> getTotalContract(@RequestBody RequestDTO<EmptyPayload> req) {

        int total = 0;

        try {
            total = contractService.countContract();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ok(total);
    }

    /**
     * Find all info of contracts by customer id
     *
     * @param req IdPayload contain id of customer
     * @return list ListContractByCustomer contain info contract
     */
    @PostMapping("/getAllContractByCustomerUuid")
    public ResponseEntity<?> getAllContractByCustomerUuid(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.getPayload();
        String strUuid = (String) payload.getId();
        UUID customerUuid = UUID.fromString(strUuid);

        List<ListContractByCustomer> listContractByCustomers = new ArrayList<>();

        List<ContractCustomer> contractCustomers = contractCustomerService.getListContractCustomerByCustomerUuid(customerUuid);

        if (contractCustomers != null && contractCustomers.size() > 0) {
            for (ContractCustomer contractCustomer : contractCustomers) {
                Contract contract = contractService.getById(contractCustomer.getContractUuid());
                if (contract == null) {
                    continue;
                }
                ListContractByCustomer contractByCustomer = new ListContractByCustomer();
                contractByCustomer.setContractCode(contract.getLendingCoreContractId());
                contractByCustomer.setContractUuid(contract.getContractUuid());
                listContractByCustomers.add(contractByCustomer);
            }
        }

        return ok(listContractByCustomers);
    }

    /**
     * Find all info of contracts by customer id
     *
     * @param req IdPayload contain id of customer
     * @return list ContractByStatusMobile contain info contract
     */
    @PostMapping("/listContractByCustomer")
    public ResponseEntity<?> getAllContractByCustomer(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload payload = req.getPayload();
        String strUuid = (String) payload.getId();
        UUID customerUuid = UUID.fromString(strUuid);
        List<ContractByStatusMobile> contractByStatusMobiles = new ArrayList<>();

//        String contractWaitingStatus = WAIT_FOR_SIGNING_CONTRACT;
//        String contractCurrentStatus = ConstantStatus.CURRENT_CONTRACT;
//        String contractDisbursedStatus = ConstantStatus.CONTRACT_DISBURSED;
//        String contractSignedContract = ConstantStatus.SIGNED_CONTRACT;

        List<ContractResponseMobile> contractWaiting = new ArrayList<>();

        List<ContractResponseMobile> contractCurrent = new ArrayList<>();

        List<ContractResponseMobile> contractDuePayment = new ArrayList<>();

        try {

            List<ContractByCustomer> contractByCustomers = contractService.getContractCodesByCustomer(customerUuid);

            List<String> contractCodes = new ArrayList<>();
            if (contractByCustomers != null && contractByCustomers.size() > 0) {
                for (ContractByCustomer byCustomerUuid : contractByCustomers) {
                    contractCodes.add(byCustomerUuid.getContractCode());
                }
            }

            List<Contract> contracts = contractService.getContractByContractCodes(contractCodes);

            if (contracts != null && contracts.size() > 0) {
                ContractByStatusMobile contractByStatusWaiting = null;
                ContractByStatusMobile contractByStatusLive = null;
                ContractByStatusMobile contractByStatusMobileDue = null;
                List<ContractResponseMobile> contractResponseMobiles = getContractResponseMobileByContracts(contracts, contractByCustomers);
                int index = 0;
                if (contractResponseMobiles != null && contractResponseMobiles.size() > 0) {
                    for (ContractResponseMobile contractResponseMobile : contractResponseMobiles) {

                        if (contractResponseMobile.getStatusType().equals("esign")) {

                            contractResponseMobile.setStatus(ContractUtils.convertStatus(contractResponseMobile.getStatus()));

                            if (contractByStatusWaiting == null) {
                                contractByStatusWaiting = new ContractByStatusMobile();
                                contractByStatusWaiting.setCode(ContractStatus.WAITING_FOR_SIGNING.getValue());

                                contractWaiting.add(contractResponseMobile);
                                contractByStatusWaiting.setData(contractWaiting);
                                contractByStatusMobiles.add(contractByStatusWaiting);
                            } else {

                                contractWaiting.add(contractResponseMobile);
                                contractByStatusWaiting.setData(contractWaiting);
                            }
                            index++;
                            continue;
                        }

                        if (contractResponseMobile.getStatusType().equals("live")) {
                            contractResponseMobile.setStatus(ContractUtils.convertStatus(contractResponseMobile.getStatus()));

                            if (contractByStatusLive == null) {
                                contractByStatusLive = new ContractByStatusMobile();
                                contractByStatusLive.setCode(ContractStatus.LIVE.getValue());

                                contractCurrent.add(contractResponseMobile);
                                contractByStatusLive.setData(contractCurrent);
                                contractByStatusMobiles.add(contractByStatusLive);
                            } else {

                                contractCurrent.add(contractResponseMobile);
                                contractByStatusLive.setData(contractCurrent);
                            }
                            index++;
                            continue;
                        }

                        if (contractResponseMobile.getStatusType().equals("repayment")) {
                            contractResponseMobile.setStatus(ContractUtils.convertStatus(contractResponseMobile.getStatus()));

                            if (contractByStatusMobileDue == null) {
                                contractByStatusMobileDue = new ContractByStatusMobile();
                                contractByStatusMobileDue.setCode(ContractStatus.DUE.getValue());

                                contractDuePayment.add(contractResponseMobile);
                                contractByStatusMobileDue.setData(contractDuePayment);
                                contractByStatusMobiles.add(contractByStatusMobileDue);
                            } else {

                                contractDuePayment.add(contractResponseMobile);
                                contractByStatusMobileDue.setData(contractDuePayment);
                            }
                            index++;
                            continue;
                        }
                    }
                }
            }

            // update contract new by customer
            contractService.updateContractByCustomerUuid(customerUuid);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok(contractByStatusMobiles);
    }

    public int getMonthLastPayment(String contractCode) {
        int monthLastPayment = 0;
        try {
            PaymentInfoRequest paymentInfoRequest = new PaymentInfoRequest();
            paymentInfoRequest.setContractCode(contractCode);
            List<PaymentInformation> paymentInformations = hdMiddleService.getListPaymentInfoByContractCodeAndLatestPaymentDate(paymentInfoRequest);
            Date lastPaymentDate = null;
            if (paymentInformations != null && !paymentInformations.isEmpty()) {
                for (PaymentInformation pay : paymentInformations) {
                    if (lastPaymentDate == null) {
                        lastPaymentDate = pay.getMonthlyDueDate();
                        continue;
                    }

                    if (lastPaymentDate.before(pay.getMonthlyDueDate())) {
                        lastPaymentDate = pay.getMonthlyDueDate();
                    }
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(lastPaymentDate);

            monthLastPayment = cal.get(Calendar.MONTH);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }

        return monthLastPayment;
    }

    /**
     * Find list contract
     *
     * @param req ContractSearch contain info need filter list contarct
     * @return AdminContractResponse contain info of list contract paging style
     */
    @PostMapping("/search")
    public ResponseEntity<?> searchContractByCodeOrPhoneOrIdentifyId(@RequestBody RequestDTO<ContractSearch> req) {
        List<ContractResponse> contractResponses = new ArrayList<>();

        AdminContractResponse adminContractResponse = new AdminContractResponse();

        ContractSearch contractSearch = req.init();
        PageSearch pageSearch = contractSearch.getPages();
        if (pageSearch == null) {
            pageSearch = new PageSearch(1, 10);
        }
        String contractCode = contractSearch.getContractCode();
        String identifyId = contractSearch.getIdentifyId();
        String phoneNumber = contractSearch.getPhoneNumber();

        //int
        int total = 0;


        List<String> contractCodes = new ArrayList<>();
        if (HDUtil.isNullOrEmpty(contractCode) && HDUtil.isNullOrEmpty(identifyId) && HDUtil.isNullOrEmpty(phoneNumber)) {
            total = contractService.countContract();
            //Pageable pageable = PageRequest.of(pageSearch.getPage() - 1, pageSearch.getPageSize(), Sort.by("id").descending());

//            List<Contract> lstContract = contractService.getListContractInfo(pageable);
//            contractCodes.addAll(getContractCodeByContract(lstContract));
            List<String> lstCode = contractService.getContractByCustomerActive("",pageSearch);
            contractCodes.addAll(lstCode);
        }

        if (!HDUtil.isNullOrEmpty(contractCode)) {
            total =+ contractService.countContractByContractCode(contractCode);
//            Pageable pageable = PageRequest.of(pageSearch.getPage() - 1, pageSearch.getPageSize(), Sort.by("id").descending());
//
//            List<Contract> lstContractByCode = contractService.searchContractByContractCodeAndPageable(contractCode, pageable);
            List<String> lstCode = contractService.getContractByCustomerActive(contractCode,pageSearch);
            if (lstCode != null && lstCode.size() > 0) {
                contractCodes.addAll(lstCode);
            }
        }

        if (!HDUtil.isNullOrEmpty(identifyId)) {
            List<HDContractResponse> hdContractResponses = hdMiddleService.getListContractByIdentifyId(identifyId);
            if (hdContractResponses != null && !hdContractResponses.isEmpty()) {
                for (HDContractResponse response : hdContractResponses) {
                    contractCodes.add(response.getContractNumber());
                }
            }
        }

        if (!HDUtil.isNullOrEmpty(phoneNumber)) {
            List<HDContractResponse> hdContractResponses = hdMiddleService.getListContractByPhoneNumber(phoneNumber);
            if (hdContractResponses != null && !hdContractResponses.isEmpty()) {
                for (HDContractResponse response : hdContractResponses) {
                    contractCodes.add(response.getContractNumber());
                }
            }
        }

        if (contractCodes != null && !contractCodes.isEmpty()) {
            List<Contract> contractList = contractService.getContractByContractCodes(contractCodes);

            if (contractList != null && contractList.size() > 0) {
                if (!HDUtil.isNullOrEmpty(identifyId) || !HDUtil.isNullOrEmpty(phoneNumber)) {
                    total += contractList.size();
                }
                List<String> listCodes = new ArrayList<>();
                for (Contract con : contractList) {
                    listCodes.add(con.getLendingCoreContractId());
                }

                List<ContractInfo> contractInfos = hdMiddleService.getContractDetailFromMidServers(listCodes);

                if (contractInfos == null || contractInfos.size() < 1) {
                    throw new InternalServerErrorException();
                }

                contractResponses = getContractResponseByContracts(contractList, contractInfos, listCodes);
            }
        }

        adminContractResponse.setTotal(total);
        adminContractResponse.setContracts(contractResponses);

        return ok(adminContractResponse);
    }

    public List<String> getContractCodeByContract(List<Contract> contracts) {
        List<String> contractCodes = new ArrayList<>();
        if (contracts != null && contracts.size() > 0) {
            for (Contract contract : contracts) {
                contractCodes.add(contract.getLendingCoreContractId());
            }
        }
        return contractCodes;
    }

    /**
     * Find contract info
     *
     * @param contractCodes list of contract code
     * @return list contract info
     */
    public List<ContractInfo> getContractInfos(List<String> contractCodes) {
        List<ContractInfo> contractInfos = new ArrayList<>();
        if (contractCodes.size() > 0) {
            contractInfos = hdMiddleService.getContractDetailFromMidServers(contractCodes);
        }
        return contractInfos;
    }

    /**
     * Remove a contract exist
     *
     * @param req RemoveContract contain info of contract need removed
     * @return http status code
     */
    @PostMapping("/remove")
    @Transactional
    public ResponseEntity<?> removeContract(@RequestBody RequestDTO<RemoveContract> req) {
        RemoveContract removeContract = req.init();
        UUID contractUuid = removeContract.getContractUuid();
        UUID customerUuid = removeContract.getCustomerUuid();

        Contract contract = contractService.getById(contractUuid);
        if (contract == null) {
            throw new NotFoundException(1406, "Contract does not exits");
        }

        List<ContractEsigned> contractEsigneds = contractEsignedService.getByCustomerUuidAndContractUuid(customerUuid,contractUuid);

        if (contractEsigneds != null && contractEsigneds.size() > 0) {
            throw new NotFoundException(1437, "Hợp đồng ký điện tử không được quyền xóa");
        }

        List<ContractCustomer> lstCustomers = contractCustomerService.getListContractCustomerByContractUuid(contractUuid);
        if (lstCustomers == null || lstCustomers.isEmpty()) {
            throw new NotFoundException(1401, "Contract Customer does not exits");
        }

        for (ContractCustomer contractCustomer : lstCustomers) {
            if (customerUuid.equals(contractCustomer.getCustomerUuid())) {
                contractCustomer.setStatus(0);
                contractCustomerService.updateContractCustomer(contractCustomer);
                continue;
            }
        }

        return ok(null);
    }

    /**
     * Find info history payment of contract
     *
     * @param req PaymentInfoRequest contain info of contract need to find history payment
     * @return list of PaymentInformation contain info history payment of contract
     */
    @PostMapping("/historyPayment")
    public ResponseEntity<?> historyPayment(@RequestBody RequestDTO<PaymentInfoRequest> req) {
        PaymentInfoRequest paymentInfoRequest = req.init();
        List<PaymentInformation> informationList = new ArrayList<>();

        String contractCode = paymentInfoRequest.getContractCode();
        UUID customerUuid = paymentInfoRequest.getCustomerUuid();
        String loanType = paymentInfoRequest.getLoanType();
        if (HDUtil.isNullOrEmpty(contractCode)) {
            List<ContractCustomer> contractCustomers = contractCustomerService.getListContractCustomerByCustomerUuidAndStatus(customerUuid, 1);
            if (contractCustomers != null && contractCustomers.size() > 0) {
                for (ContractCustomer contractCustomer : contractCustomers) {
                    Contract contract = contractService.getById(contractCustomer.getContractUuid());
                    if (contract == null) {
                        continue;
                    }
                    if (HDUtil.isNullOrEmpty(loanType)) {
                        paymentInfoRequest.setContractCode(contract.getLendingCoreContractId());
                        List<PaymentInformation> list = hdMiddleService.getListPaymentInfoByContractCodeAndLatestPaymentDate(paymentInfoRequest);
                        if (list != null && list.size() > 0) {
                            informationList.addAll(list);
                            continue;
                        }
                    }

                    List<String> contractCodes = new ArrayList<>();
                    contractCodes.add(contract.getLendingCoreContractId());

                    List<ConfigContractTypeBackground> contractTypeBackgrounds = getConfigContractType(contractCodes);
                    if (contractTypeBackgrounds == null) {
                        continue;
                    }
                    if (!contractTypeBackgrounds.get(0).getContractType().equals(loanType)) {
                        continue;
                    }

                    paymentInfoRequest.setContractCode(contract.getLendingCoreContractId());
                    List<PaymentInformation> list = hdMiddleService.getListPaymentInfoByContractCodeAndLatestPaymentDate(paymentInfoRequest);
                    if (list != null && list.size() > 0) {
                        informationList.addAll(list);
                        continue;
                    }
                }
            }
        } else {
            Contract contract = contractService.getContractByContractCode(paymentInfoRequest.getContractCode());
            if (contract == null) {
                throw new NotFoundException(1406, "Contract does not exits");
            }

            informationList = hdMiddleService.getListPaymentInfoByContractCodeAndLatestPaymentDate(paymentInfoRequest);
        }

        informationList = convertAddLoanNamePayment(informationList);

        return ok(informationList);
    }

    /**
     * Set loan name of PaymentInformation
     *
     * @param paymentInformations list of PaymentInformation need to set loan name
     * @return list of PaymentInformation after update successfully
     */
    public List<PaymentInformation> convertAddLoanNamePayment(List<PaymentInformation> paymentInformations) {

        if (paymentInformations != null && paymentInformations.size() > 0) {

            List<String> contractCodes = new ArrayList<>();

            for (PaymentInformation paymentInformation : paymentInformations) {
                contractCodes.add(paymentInformation.getContractCode());
            }

            List<ConfigContractTypeBackground> contractTypeBackgrounds = getConfigContractType(contractCodes);
            if (contractTypeBackgrounds == null || contractTypeBackgrounds.isEmpty()) {
                throw new InternalServerErrorException();
            }
            int index = 0;
            for (PaymentInformation paymentInformation : paymentInformations) {
                if (contractTypeBackgrounds.get(index) != null) {
                    paymentInformation.setLoanType(contractTypeBackgrounds.get(index).getContractType());
                    index++;
                }
            }

            // sort payment
            Collections.sort(paymentInformations, new Comparator<PaymentInformation>() {
                public int compare(PaymentInformation p1, PaymentInformation p2) {
                    return Long.valueOf(p2.getMonthlyDueDate().getTime()).compareTo(p1.getMonthlyDueDate().getTime());
                }
            });
        }

        return paymentInformations;
    }

    /**
     *  Create a new contract
     *
     * @param req Contract contain info need to create a new contract
     * @return id of contract after generate successfully
     */
    @PostMapping("/post")
    public ResponseEntity<?> insertContract(@RequestBody RequestDTO<Contract> req) {
        UUID contractId = UUID.randomUUID();
        Contract contract = req.init();
        try {
            contract.setContractUuid(contractId);
            contract.setCreatedAt(new Date());
            contractService.createContract(contract);
        } catch (Exception ex) {
            throw new InternalServerErrorException();
        }
        return ok(contractId);
    }

    //    @PostMapping("/getListContractSigned")
//    public ResponseEntity<?> getListContractNew(@RequestBody RequestDTO<IdPayload> req) {
//
//        String contractNewStatus = ConstantStatus.SIGNED_CONTRACT;
//
//        List<ContractInfo> infoList = new ArrayList<>();
//        try {
//            infoList = getContractByStatus(req.init(), contractNewStatus);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new InternalServerErrorException();
//        }
//        return ok(infoList);
//    }
//

    /**
     * Find list WaitingAdjustment
     *
     * @param req IdPayload contain id of contract need to find adjustment
     * @return list of WaitingAdjustment of contract
     */
    @PostMapping("/getListWaitingAdjustment")
    public ResponseEntity<?> getListWaitingAdjustment(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload idPayload = req.init();

        String strId = (String) idPayload.getId();
        List<ContractResponseMobile> list = new ArrayList<>();

        List<String> contractCodes = new ArrayList<>();

        try {
            List<WaitingAdjustment> waitingAdjustments = contractService.getContractAdjustmentInfoByCustomerUuid(strId);

            if (waitingAdjustments != null && waitingAdjustments.size() > 0) {
                for (WaitingAdjustment item : waitingAdjustments) {
                    contractCodes.add(item.getContractCode());
                }

                List<Contract> contracts = contractService.getContractByContractCodes(contractCodes);

                if (contracts != null && !contracts.isEmpty()) {
                    list = getContractResponseMobileByContracts(contracts, null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok(list);
    }

    /**
     * Find list WaitingAdjustment
     *
     * @param req IdPayload contain id of customer need to find contract waiting for sign
     * @return list of ContractResponseMobile contain info od contract waiting for sign
     */
    @PostMapping("/getListWaitingForSigningContract")
    public ResponseEntity<?> waitingForSigning(@RequestBody RequestDTO<IdPayload> req) {

        List<ContractResponseMobile> infoList = new ArrayList<>();

        infoList = getContractByStatus(req.init());

        return ok(infoList);
    }

    /**
     * Find list current contract
     *
     * @return list of ContractResponseMobile contain info of contract
     */
    @PostMapping("/getListCurrentContract")
    public ResponseEntity<?> currentContract() {

        List<ContractResponseMobile> contractResponseMobiles = new ArrayList<>();
        try {
            String contractCurrentStatus = ConstantStatus.CURRENT_CONTRACT;

            List<String> lstCurrent = new ArrayList<String>(Arrays.asList(contractCurrentStatus.split(",")));

            List<Contract> contracts = contractService.getContractListCurrentContract(lstCurrent);

            if (contracts != null && !contracts.isEmpty()) {
                contractResponseMobiles = getContractResponseMobileByContracts(contracts, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok(contractResponseMobiles);
    }

    /**
     * Find list contract waiting for sign of customer
     *
     * @param idPayload contain id of customer
     * @return list of ContractResponseMobile contain info of contract
     */
    public List<ContractResponseMobile> getContractByStatus(IdPayload idPayload) {
        List<ContractResponseMobile> contractResponseMobiles = new ArrayList<>();

        try {
            String strId = (String) idPayload.getId();
            UUID customerUuid = UUID.fromString(strId);
            List<String> contractCodes = new ArrayList<>();

            List<ContractWaitingForSigning> list = contractService.getContractWaitingForSigning(customerUuid.toString());

            if (list != null && list.size() > 0) {
                for (ContractWaitingForSigning contractWaitingForSigning : list) {
                    contractCodes.add(contractWaitingForSigning.getContractCode());
                }

                List<Contract> contracts = contractService.getContractByContractCodes(contractCodes);

                if (contracts != null && !contracts.isEmpty()) {
                    contractResponseMobiles = getContractResponseMobileByContracts(contracts, null);
                }
            }
            if (contractResponseMobiles != null) {
                for (ContractResponseMobile item : contractResponseMobiles) {
                    item.setStatus(ContractUtils.convertStatus(item.getStatus()));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }

        return contractResponseMobiles;
    }

    /**
     * Create a new Loan
     *
     * @param req Loan info
     * @return contract info
     */
    @PostMapping("/addLoan")
    public ResponseEntity<?> addLoan(@RequestBody RequestDTO<ValidateAddLoan> req) {
        ValidateAddLoan validateAddLoan = req.init();
        String contractCode = validateAddLoan.getContractCode();
        String identifyId = validateAddLoan.getIdentifyId();
        UUID customerUuid = validateAddLoan.getCustomerUuid();

        Log.print("Request", validateAddLoan.toString());

        List<ContractCustomer> contracts = contractCustomerService.getListContractCustomerByCustomerUuid(customerUuid);

        if (contracts == null) {
            throw new NotFoundException(1400, "Contract code or Identify does not exits");
        }

        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contractCode);


        if (contractInfo == null) {
            throw new NotFoundException(1400, "Contract code or Identify does not exits");
        }

        // check contract printing
        PhoneAndStatus phoneAndStatus = hdMiddleService.getPhoneAndStatusByContractCode(contractCode);
        if (phoneAndStatus != null) {
            if (phoneAndStatus.getDocumentVerificationDate() == null && phoneAndStatus.getContractPrintingDate() == null) {
                throw new NotFoundException(1400, "Contract code or Identify does not exits");
            }
        }

        if (!contractInfo.getNationalID().equals(identifyId)) {
            throw new NotFoundException(1400, "Contract code or Identify does not exits");
        }

        ContractInfo contractInfoResponse = null;
        for (ContractCustomer item : contracts) {

            if (item.getContractCode().equals(contractCode) && item.getStatus() == 1) {
                throw new BadRequestException(1434, "Contract already exists");
            }

            ContractInfo contractInfoExits = hdMiddleService.getContractDetailFromMidServer(item.getContractCode());

            String fullNameNew = contractInfo.getLastName() + contractInfo.getFirstName();
            String fullNameOle = contractInfoExits.getLastName() + contractInfoExits.getFirstName();
            fullNameNew = HDUtil.unAccent(fullNameNew.toLowerCase());
            fullNameOle = HDUtil.unAccent(fullNameOle.toLowerCase());
            if (!fullNameNew.equals(fullNameOle)) {
                continue;
            }

            String phoneOld = contractInfoExits.getPhoneNumber();
            String phoneNew = contractInfo.getPhoneNumber();

            if (phoneOld.equals(phoneNew)) {
                contractInfoResponse = contractInfo;
                contractInfoResponse.setStatus(ContractUtils.convertStatus(contractInfo.getStatus()));
                break;
            }

            Date birthdayOld = contractInfoExits.getBirthday();
            Date birthdayNew = contractInfo.getBirthday();

            if (birthdayOld.compareTo(birthdayNew) == 0) {
                contractInfoResponse = contractInfo;
                contractInfoResponse.setStatus(ContractUtils.convertStatus(contractInfo.getStatus()));
                break;
            }

            String familyBookNoOld = contractInfoExits.getFamilyBookNo();
            String familyBookNoNew = contractInfo.getFamilyBookNo();

            String driversLicenceOld = contractInfoExits.getDriversLicence();
            String driversLicenceNew = contractInfo.getDriversLicence();

            if (familyBookNoOld.equals(familyBookNoNew) || driversLicenceOld.equals(driversLicenceNew)) {
                contractInfoResponse = contractInfo;
                contractInfoResponse.setStatus(ContractUtils.convertStatus(contractInfo.getStatus()));
                break;
            }
        }

        if (contractInfoResponse == null) {
            throw new NotFoundException(1400, "Contract code or Identify does not exits");
        }

        return ok(contractInfoResponse);
    }


    /**
     * Create a new Loan
     *
     * @param req Loan info
     * @return http status code
     */
    @PostMapping("/saveLoan")
    public ResponseEntity<?> saveLoan(@RequestBody RequestDTO<AddLoanRequest> req) {
        AddLoanRequest addLoanRequest = req.init();
        List<String> contracts = new ArrayList<>();

        String contractCode = addLoanRequest.getContractCode();

        HDContractResponse hdContractResponse = hdMiddleService.getContractByContractCodeAndIdentifyId(contractCode, addLoanRequest.getIdentifyId());

        if (hdContractResponse == null) {
            throw new InternalServerErrorException(1405, "Insert contract error !");
        }

        UUID customerUuid = addLoanRequest.getCustomerUuid();

        List<ContractsByCustomerUuid> contractsByCustomerUuids = contractService.getListContractByCustomerUuid(customerUuid);

        if (contractsByCustomerUuids == null) {
            throw new NotFoundException(1400, "Contract code or Identify does not exits");
        }

        ContractsByCustomerUuid byCustomerUuid = null;
        for (ContractsByCustomerUuid item : contractsByCustomerUuids) {
            if (item.getContractCode().equals(contractCode)) {
                byCustomerUuid = item;
            }
        }

        if (byCustomerUuid != null && byCustomerUuid.getContractCustomerStatus() == 1) {
            throw new BadRequestException(1434, "Contract already exists");
        }

        if (byCustomerUuid != null && byCustomerUuid.getContractCustomerStatus() == 0) {
            ContractCustomer contractCustomer = contractCustomerService.getListContractCustomerByContractUuidAndStatus(UUID.fromString(byCustomerUuid.getContractUuid()), 0);
            contractCustomer.setStatus(1);
            contractCustomerService.updateContractCustomer(contractCustomer);
        } else {
            if (!insertContract(hdContractResponse, customerUuid, contracts, null)) {
                throw new InternalServerErrorException(1405, "Insert contract error !");
            }
        }

        return ok(null);
    }

    /**
     * View detail of one contract
     *
     * @param req IdPayload contain id of contract
     * @return contract info
     */
    @PostMapping("/detail")
    public ResponseEntity<?> getContractById(@RequestBody RequestDTO<IdPayload> req) {

        Contract contract = null;
        ContractInfo contractInfo = null;

        IdPayload idPayload = req.init();
        contract = getContractById(req.init());

        //write log
        log.writeLogAction(req, "Xem nội dung Hợp đồng", "Khách hàng chọn nút Kích hoạt Hợp đồng", idPayload.toString(), "", "", null, "esign");


        if (contract == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }
        contractInfo = hdMiddleService.getContractDetailFromMidServer(contract.getLendingCoreContractId());

        if (contractInfo == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }

        contractInfo.setStatus(ContractUtils.convertStatus(contractInfo.getStatus()));
        contractInfo.setConfigRecords(new ArrayList<>());

        if (contractInfo.getNationalID().length() == 9) {
            contractInfo.setNationalID(HDUtil.maskNumber(contractInfo.getNationalID(), "*** *** ###"));
        } else {
            contractInfo.setNationalID(HDUtil.maskNumber(contractInfo.getNationalID(), "**** **** ####"));
        }

        String phoneNumber = contractInfo.getPhoneNumber();

        if (phoneNumber.length() == 10) {
            contractInfo.setPhoneNumber(HDUtil.maskNumber(phoneNumber, "*** *** ####"));
        } else {
            contractInfo.setPhoneNumber(HDUtil.maskNumber(phoneNumber, "*** **** ####"));
        }


        int len = contractInfo.getLastName().length();
        StringBuilder lastName = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            lastName.append('x');
        }

        contractInfo.setLastName(lastName.toString());
        List<String> attachments = eSignedFileService.getFile(contract.getContractUuid(), "");
        contractInfo.setAttachments(attachments);

        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống HDSaison khởi tạo bộ chứng từ Hợp đồng tín dụng");
        joiner.add("- Mã số Hợp đồng: " + contractInfo.getContractNumber());
        joiner.add("- Người khởi tạo: Công ty Tài chính TNHH HD SAISON");
        joiner.add("- Địa điểm khởi tạo: Trụ sở văn phòng Công ty Tài chính TNHH HD SAISON - Lầu 8, 9, 10, Tòa nhà Gilimex - 24C Phan Đăng Lưu, Phường 6, Quận Bình Thạnh, Tp. HCM");
        log.writeLogAction(req, "Khởi tạo chứng từ Hợp đồng", joiner.toString(), idPayload.toString(), "", "", contractInfo.getContractNumber(), "esign");

        return ok(contractInfo);
    }

    /**
     * View detail of one contract
     *
     * @param req IdPayload contain id of contract
     * @return contract info
     */
    @PostMapping("/detail_mobile")
    public ResponseEntity<?> getContractByIdMobile(@RequestBody RequestDTO<IdPayload> req) {
        Contract contract = null;
        ContractInfo contractInfo = null;
        IdPayload idPayload = req.init();
        contract = getContractById(idPayload);

        //write log
        log.writeLogAction(req, "Xem nội dung Hợp đồng", "Khách hàng chọn nút Kích hoạt Hợp đồng", idPayload.toString(), "", "", null, "esign");


        if (contract == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }


        DetailMobile detailMobile = new DetailMobile();
        contractInfo = hdMiddleService.getContractDetailFromMidServer(contract.getLendingCoreContractId());

        if (contractInfo == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }


        contractInfo.setContractUuid(contract.getContractUuid());
        contractInfo.setConfigRecords(new ArrayList<>());
        contractInfo.setIsInsurance(contract.getIsInsurance());

        PaymentInfoRequest paymentInfoRequest = new PaymentInfoRequest();
        paymentInfoRequest.setContractCode(contract.getLendingCoreContractId());

        List<PaymentInformation> paymentInformations = new ArrayList<>();

        paymentInformations = hdMiddleService.getListPaymentInfoByContractCodeAndLatestPaymentDate(paymentInfoRequest);

        if (paymentInformations != null && paymentInformations.size() > 0) {
            paymentInformations = convertAddLoanNamePayment(paymentInformations);
        }


        String contractDisbursedStatus = ConstantStatus.CONTRACT_DISBURSED;
        List<String> lstDisbursed = new ArrayList<String>(Arrays.asList(contractDisbursedStatus.split(",")));
        int paymentPaid = 0;
        if (!lstDisbursed.contains(contractInfo.getStatus())) {
            Integer totalPayment = 0;

            if (paymentInformations != null && paymentInformations.size() > 0) {
                for (PaymentInformation information : paymentInformations) {
                    totalPayment += information.getMonthlyInstallmentAmount().intValue();
                }
            }
            paymentPaid = totalPayment / contractInfo.getMonthlyInstallmentAmount().intValue();
        } else {
            paymentPaid = contractInfo.getTenor().intValue();
        }

        contractInfo.setTenorRemaind(contractInfo.getTenor().intValue() - paymentPaid);
        contractInfo.setTotalPaid(paymentPaid);
        contractInfo.setStatus(ContractUtils.convertStatus(contractInfo.getStatus()));

        List<String> contractCodes = new ArrayList<>();
        contractCodes.add(contractInfo.getContractNumber());
        List<ConfigContractTypeBackground> contractTypeBackground = getConfigContractType(contractCodes);

        if (contractTypeBackground != null && contractTypeBackground.size() > 0) {
            contractInfo.setLoanType(contractTypeBackground.get(0).getContractType());
            contractInfo.setLoanName(contractTypeBackground.get(0).getContractName());
        }

        //contractInfo.setLoanName(getLoanType(contractInfo.getContractNumber()));
        String loanType = contractInfo.getLoanType();
        if (!HDUtil.isNullOrEmpty(loanType)) {
            if (loanType.equals("CL") || loanType.equals("CLO")) {
                contractInfo.setProductName("Vay tiền mặt");
                contractInfo.setProductPrice(contractInfo.getLoanAmount());
            }
        }

        //List<String> attachments = eSignedFileService.getFile(contract.getContractUuid(), "");
        contractInfo.setAttachments(eSignedFileService.getFile(contract.getContractUuid(), Contract.FILE_TYPE.E_SIGN));
        contractInfo.setLstAdj(eSignedFileService.getFile(contract.getContractUuid(), Contract.FILE_TYPE.ADJUSTMENT));

        detailMobile.setContract(contractInfo);
        detailMobile.setHistoryPayments(paymentInformations);
        //detailMobile.setAttachments(attachments);

        // get contract documenr verify date
        ContractEsigned esigned = contractEsignedService.findByContractId(contract.getContractUuid());
        if (esigned != null) {
            contractInfo.setDocumentVerificationDate(esigned.getCreatedAt());
        }else {
            contractInfo.setDocumentVerificationDate(contract.getDocumentVerificationDate());
        }


        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống HDSaison khởi tạo bộ chứng từ Hợp đồng tín dụng");
        joiner.add("- Mã số Hợp đồng: " + contractInfo.getContractNumber());
        joiner.add("- Người khởi tạo: Công ty Tài chính TNHH HD SAISON");
        joiner.add("- Địa điểm khởi tạo: Trụ sở văn phòng Công ty Tài chính TNHH HD SAISON - Lầu 8, 9, 10, Tòa nhà Gilimex - 24C Phan Đăng Lưu, Phường 6, Quận Bình Thạnh, Tp. HCM");
        log.writeLogAction(req, "Khởi tạo chứng từ Hợp đồng", joiner.toString(), idPayload.toString(), "", "", contractInfo.getContractNumber(), "esign");

        return ok(detailMobile);
    }

    /**
     * Find contract
     *
     * @param idPayload contain contract code
     * @return object Contract
     */
    public Contract getContractById(IdPayload idPayload) {
        String contractCode = (String) idPayload.getId();
        return contractService.getContractByContractCode(contractCode);
    }

    /**
     * Find info CheckRecords of contract
     *
     * @param req contain contract code
     * @return ConfigCheckRecordsAndStatus contain info records
     */
    @PostMapping("/checkRecordsContract")
    public ResponseEntity<?> checkRecordsContract(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload payload = req.getPayload();
        String contractCode = (String) payload.getId();
        Contract contract = contractService.getContractByContractCode(contractCode);

        if (contract == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }
        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contractCode);

        if (contractInfo == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }


        ConfigCheckRecordsAndStatus configCheckRecordsAndStatus = new ConfigCheckRecordsAndStatus();
        List<ContractAdjustmentInfo> contractAdjustmentInfos = contractAdjustmentInfoService.getListContractAdjustmentInfoByContractCode(contractCode);

        if (contractAdjustmentInfos != null && !contractAdjustmentInfos.isEmpty()) {
            configCheckRecordsAndStatus.setChanges(false);
        } else {
            configCheckRecordsAndStatus.setChanges(true);
        }

//        String contractStatus = WAIT_FOR_SIGNING_CONTRACT;
//
//        List<String> listStatus = new ArrayList<String>(Arrays.asList(contractStatus.split(",")));

        if (!"DOCUMENT VERIFICATION".equals(contract.getStatus().toUpperCase())) {
            configCheckRecordsAndStatus.setChanges(false);
        }

        List<ConfigCheckRecords> configCheckRecordsList = getConfigCheckRecords(contractInfo);

        for (ConfigCheckRecords checkRecords : configCheckRecordsList) {
            for (ContractAdjustmentInfo contractAdjustmentInfo : contractAdjustmentInfos) {
                if (checkRecords.getKey().equals(contractAdjustmentInfo.getKey())) {
                    checkRecords.setValueNew(contractAdjustmentInfo.getValue());
                    break;
                }
            }
        }


        CheckRecords checkRecords = new CheckRecords();
        checkRecords.setContractCode(contractInfo.getContractNumber());
        checkRecords.setStatus(ContractUtils.convertStatus(contract.getStatus().toUpperCase()));
        checkRecords.setFirstDue(contractInfo.getFirstDue());
        checkRecords.setContractPrintingDate(contract.getContractPrintingDate());
        checkRecords.setConfig(configCheckRecordsList);
        configCheckRecordsAndStatus.setConfig(checkRecords);
        return ok(configCheckRecordsAndStatus);
    }

    /**
     * Update CheckRecords of contract
     *
     * @param req UpdateCheckRecords contain info need to update
     * @return http status code
     */
    @PostMapping("/updateCheckRecords")
    public ResponseEntity<?> updateCheckRecords(@RequestBody RequestDTO<UpdateCheckRecords> req) {

        UpdateCheckRecords updateCheckRecords = req.init();

        String contractCode = updateCheckRecords.getContractCode();
        UUID createdBy = updateCheckRecords.getCreatedBy();

        if (createdBy == null || HDUtil.isNullOrEmpty(createdBy.toString())) {
            throw new BadRequestException(1423, "CreatedBy is null or empty !");
        }
        List<ConfigCheckRecords> configCheckRecords = updateCheckRecords.getConfig();
        if (configCheckRecords != null && !configCheckRecords.isEmpty()) {
            Date currentDate = new Date();
            for (ConfigCheckRecords records : configCheckRecords) {

                if (HDUtil.isNullOrEmpty(records.getValueNew())) {
                    continue;
                }
                ContractAdjustmentInfo contractAdjustmentInfo = null;
                try {
                    contractAdjustmentInfo = contractAdjustmentInfoService.getContractAdjustmentByContractCodeAndKey(contractCode, records.getKey());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (contractAdjustmentInfo == null) {
                    contractAdjustmentInfo = new ContractAdjustmentInfo();
                    contractAdjustmentInfo.setIsConfirm(0);
                    contractAdjustmentInfo.setKey(records.getKey());
                    contractAdjustmentInfo.setValue(records.getValueNew());
                    contractAdjustmentInfo.setContractCode(contractCode);
                    contractAdjustmentInfo.setCreatedAt(currentDate);
                    contractAdjustmentInfo.setCreatedBy(createdBy);
                    contractAdjustmentInfoService.create(contractAdjustmentInfo);
                } else {
                    contractAdjustmentInfo.setValue(records.getValueNew());
                    contractAdjustmentInfo.setCreatedAt(currentDate);
                    contractAdjustmentInfo.setCreatedBy(createdBy);
                    contractAdjustmentInfoService.update(contractAdjustmentInfo);
                }
            }
        }
        return ok(null);
    }

    /**
     * Find list contract adjustment is approval
     *
     * @param req ContractApproval contain info filter
     * @return list result contract adjustment
     */
    @PostMapping("/listContractApproval")
    public ResponseEntity<?> listContractApproval(@RequestBody RequestDTO<ContractApproval> req) {

        ContractApproval contractApproval = req.init();

        String contractCode = contractApproval.getContractCode();

        Integer isConfirm = contractApproval.getIsConfirm();

        PageSearch pageSearch = contractApproval.getPages();

        if (pageSearch == null) {
            pageSearch = new PageSearch(1, 10);
        }

        List<ContractAdjustmentResponse> list = new ArrayList<>();
        List<AdjustmentInfoMapper> contractAdjustmentInfos = contractAdjustmentInfoService.getListContractAdjustmentInfo(contractCode, isConfirm, pageSearch);
        List<String> contractCodes = new ArrayList<>();
        if (contractAdjustmentInfos != null && !contractAdjustmentInfos.isEmpty()) {
            for (AdjustmentInfoMapper mapper : contractAdjustmentInfos) {
                ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(mapper.getContractCode());
                ContractAdjustmentResponse response = new ContractAdjustmentResponse();
                response.setContractCode(mapper.getContractCode());
                response.setDateUpdateContract(mapper.getCreatedAt());
                contractCodes.add(mapper.getContractCode());
                if (contractInfo != null) {
                    response.setFullName(contractInfo.getFirstName() + " " + contractInfo.getMidName() + " " + contractInfo.getLastName());
                    response.setLoanAmount(contractInfo.getLoanAmount());
                    response.setLoanType(getLoanType(mapper.getContractCode()));
                    //response.setStatus(ContractUtils.convertStatus(contractInfo.getStatus()));
                    list.add(response);
                }
            }
        }
        List<Contract> contracts = contractService.getContractByContractCodes(contractCodes);

        if (list != null && list.size() > 0) {
            int index = 0;
            for (ContractAdjustmentResponse item : list) {
                item.setStatus(ContractUtils.convertStatus(contracts.get(index).getStatus()));
                index++;
            }
        }

        return ok(list);
    }

    /**
     * View detail adjustment info of contract
     *
     * @param req contract code
     * @return object ConfirmCheckRecords contain info of adjustment
     */
    @PostMapping("/getDetailContractAdjustmentInfo")
    public ResponseEntity<?> getDetailContractAdjustmentInfo(@RequestBody RequestDTO<IdPayload> req) {

        ConfirmCheckRecords records = new ConfirmCheckRecords();
        IdPayload idPayload = req.init();
        String contractCode = (String) idPayload.getId();

        Contract contract = contractService.getContractByContractCode(contractCode);

        if (contract == null) {
            throw new NotFoundException(1400, "Contract Code does not exits !");
        }

        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contractCode);

        if (contractInfo == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }

        //List<ConfigCheckRecords> configCheckRecords = contractInfo.getConfigRecords();

        List<ContractAdjustmentInfo> contractAdjustmentInfos = contractAdjustmentInfoService.getListContractAdjustmentInfoByContractCode(contractCode);

        if (contractAdjustmentInfos == null || contractAdjustmentInfos.isEmpty()) {
            throw new BadRequestException(1428, "ContractAdjustmentInfo is does not exits");
        }

        List<ConfigCheckRecords> configCheckRecordsList = getConfigCheckRecords(contractInfo);

        for (ConfigCheckRecords checkRecords : configCheckRecordsList) {
            for (ContractAdjustmentInfo contractAdjustmentInfo : contractAdjustmentInfos) {
                if (checkRecords.getKey().equals(contractAdjustmentInfo.getKey())) {
                    checkRecords.setValueNew(contractAdjustmentInfo.getValue());
                    checkRecords.setValueConfirm(contractAdjustmentInfo.getValueConfirm());
                    break;
                }
            }
        }

        records.setConfig(configCheckRecordsList);

        ContractAdjustmentInfo infoMappers = contractAdjustmentInfos.get(0);
        records.setChanges(true);

        if (!"DOCUMENT VERIFICATION".equals(contract.getStatus().toUpperCase())) {
            records.setChanges(false);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        if (infoMappers != null) {
            if (infoMappers.getIsConfirm() != null && infoMappers.getIsConfirm() == 1) {
                records.setChanges(false);
            }
            Invoker invoker = new Invoker();
            String staffName = "";
            UuidRequest uuidRequest = new UuidRequest();
            uuidRequest.setUuid(infoMappers.getCreatedBy());
            ResponseDTO<Object> dto = invoker.call(urlStaffRequest + "/find", uuidRequest, new ParameterizedTypeReference<ResponseDTO<Object>>() {
            });
            if (dto != null && dto.getCode() == 200) {
                try {
                    Staff staff = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<Staff>() {
                            });
                    staffName = staff.getFullName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            records.setContractCode(contractCode);
            records.setCreateBy(staffName);
            records.setCreatedAt(infoMappers.getCreatedAt());
            records.setStatus(ContractUtils.convertStatus(contract.getStatus()));
        }
        return ok(records);
    }

    /**
     * View detail adjustment info of contract
     *
     * @param req contract code
     * @return object ConfirmCheckRecords contain info of adjustment
     */
    @PostMapping("/getDetailContractAdjustmentInfoMobile")
    public ResponseEntity<?> getDetailContractAdjustmentInfoMobile(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload idPayload = req.init();
        String contractCode = (String) idPayload.getId();
        DetailAdjustmentInfoMobile detailAdjustmentInfoMobile = new DetailAdjustmentInfoMobile();
        Contract contract = contractService.getContractByContractCode(contractCode);

        if (contract == null) {
            throw new NotFoundException(1400, "Contract Code does not exits !");
        }

        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contractCode);

        if (contractInfo == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }

        List<ContractAdjustmentDetail> contractAdjustmentDetails = contractAdjustmentInfoService.getListContractAdjustmentInfoByContractCodeMobile(contractCode);

        if (contractAdjustmentDetails == null || contractAdjustmentDetails.isEmpty()) {
            throw new BadRequestException(1428, "ContractAdjustmentInfo is does not exits");
        }

        List<ConfigCheckRecords> configCheckRecordsList = getConfigCheckRecords(contractInfo);

        List<ConfigCheckRecordsMobile> checkRecordsListResponse = new ArrayList<>();

        for (ContractAdjustmentDetail contractAdjustmentDetail : contractAdjustmentDetails) {
            for (ConfigCheckRecords checkRecords : configCheckRecordsList) {
                if (checkRecords.getKey().equals(contractAdjustmentDetail.getKey())) {

                    ConfigCheckRecordsMobile checkRecordsMobile = new ConfigCheckRecordsMobile();
                    checkRecordsMobile.setKey(contractAdjustmentDetail.getKey());
                    checkRecordsMobile.setName(checkRecords.getName());
                    checkRecordsMobile.setValueOle(checkRecords.getValue());
                    checkRecordsMobile.setValueChange(contractAdjustmentDetail.getValueAdjustment());

                    checkRecordsListResponse.add(checkRecordsMobile);
                    break;
                }
            }
        }

        detailAdjustmentInfoMobile.setContractNumber(contractCode);
        detailAdjustmentInfoMobile.setContractUuid(contract.getContractUuid().toString());
        detailAdjustmentInfoMobile.setConfigs(checkRecordsListResponse);
        detailAdjustmentInfoMobile.setPhoneNumber(contractInfo.getPhoneNumber());
        return ok(detailAdjustmentInfoMobile);
    }

    /**
     * Find ConfigCheckRecords info of one contract
     *
     * @param contractInfo info contract need to check
     * @return list of ConfigCheckRecords of contract
     */
    public List<ConfigCheckRecords> getConfigCheckRecords(ContractInfo contractInfo) {
        List<ConfigCheckRecords> configCheckRecordsList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Invoker invoker = new Invoker();
        ResponseDTO<Object> dto = invoker.call(configAdjustmentContract + "/list_is_check_document", null, new ParameterizedTypeReference<ResponseDTO<Object>>() {
        });
        List<AdjustmentInfoConfig> adjustmentInfoConfigs = new ArrayList<>();

        if (dto != null && dto.getCode() == 200) {
            try {
                adjustmentInfoConfigs = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<List<AdjustmentInfoConfig>>() {
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<ConfigCheckRecords> configCheckRecords = contractInfo.getConfigRecords();
        for (ConfigCheckRecords checkRecords : configCheckRecords) {
            for (AdjustmentInfoConfig config : adjustmentInfoConfigs) {
                if (config.getCode().equals(checkRecords.getKey())) {
                    checkRecords.setName(config.getName());
                    configCheckRecordsList.add(checkRecords);
                    break;
                }
            }
        }
        return configCheckRecordsList;
    }

    /**
     * Update CheckRecords of contract
     *
     * @param req UpdateCheckRecords contain info need to update
     * @return http status code
     */
    @PostMapping("/updateCheckRecordsConfirm")
    public ResponseEntity<?> updateCheckRecordsConfirm(@RequestBody RequestDTO<UpdateCheckRecords> req) {
        ;

        UpdateCheckRecords updateCheckRecords = req.init();

        String contractCode = updateCheckRecords.getContractCode();
        UUID createdByConfirm = updateCheckRecords.getCreatedConfirmBy();

        if (createdByConfirm == null || HDUtil.isNullOrEmpty(createdByConfirm.toString())) {
            throw new BadRequestException(1423, "CreatedBy is null or empty !");
        }

        List<ConfigCheckRecords> configCheckRecords = updateCheckRecords.getConfig();
        if (configCheckRecords != null && !configCheckRecords.isEmpty()) {
            Date currentDate = new Date();
            for (ConfigCheckRecords records : configCheckRecords) {
                if (HDUtil.isNullOrEmpty(records.getValueNew()) && HDUtil.isNullOrEmpty(records.getValueConfirm())){
                    continue;
                }
                ContractAdjustmentInfo contractAdjustmentInfo = null;
                try {
                    contractAdjustmentInfo = contractAdjustmentInfoService.getContractAdjustmentByContractCodeAndKey(contractCode, records.getKey());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (contractAdjustmentInfo == null) {
                    contractAdjustmentInfo = new ContractAdjustmentInfo();
                    contractAdjustmentInfo.setIsConfirm(1);
                    contractAdjustmentInfo.setKey(records.getKey());
                    contractAdjustmentInfo.setValueConfirm(records.getValueConfirm());
                    contractAdjustmentInfo.setContractCode(contractCode);
                    contractAdjustmentInfo.setCreatedConfirmAt(currentDate);
                    contractAdjustmentInfo.setCreatedConfirmBy(createdByConfirm);
                    contractAdjustmentInfoService.create(contractAdjustmentInfo);
                } else {
                    contractAdjustmentInfo.setIsConfirm(1);
                    contractAdjustmentInfo.setValueConfirm(records.getValueConfirm());
                    contractAdjustmentInfo.setCreatedConfirmAt(currentDate);
                    contractAdjustmentInfo.setCreatedConfirmBy(createdByConfirm);
                    contractAdjustmentInfoService.update(contractAdjustmentInfo);
                }
            }
        }
        return ok(null);
    }

    /**
     * Find contract code by customer info
     *
     * @param req CustomerFilter contain info need find
     * @return list string of contract code
     */
    @PostMapping("/getContractCodesByIdentifyIdOrContractCode")
    public ResponseEntity<?> getContractCodesByIdentifyIdOrContractCode(@RequestBody RequestDTO<CustomerFilter> req) {

        List<String> contractCodes = new ArrayList<>();

        CustomerFilter customerFilter = req.init();
        if (customerFilter == null) {
            throw new BadRequestException();
        }
        String contractCode = customerFilter.getContractCode();
        String identifyId = customerFilter.getIdentityNumber();
        if (!HDUtil.isNullOrEmpty(contractCode)) {
            List<Contract> contracts = contractService.searchContractByContractCode(contractCode, null);
            for (Contract contract : contracts) {
                contractCodes.add(contract.getLendingCoreContractId());
            }
        }

        if (!HDUtil.isNullOrEmpty(identifyId)) {
            List<HDContractResponse> hdContractResponses = hdMiddleService.getListContractByIdentifyId(identifyId);
            if (hdContractResponses != null && !hdContractResponses.isEmpty()) {
                for (HDContractResponse response : hdContractResponses) {
                    contractCodes.add(response.getContractNumber());
                }
            }
        }
        return ok(contractCodes);
    }

    /**
     * Check customer info is valid to forgot password or not
     *
     * @param req CustomerFilter contain info need check
     * @return VerifyResponse contain info result after check successfully
     */
    @PostMapping("/checkValidateForgotPasswordByIdentifyId")
    public ResponseEntity<?> checkValidateForgotPasswordByIdentifyId(@RequestBody RequestDTO<CustomerFilter> req) {

        VerifyResponse verifyResponse = new VerifyResponse();
        List<Contract> lstContract = new ArrayList<>();

        CustomerFilter customerFilter = req.init();
        if (customerFilter == null) {
            throw new BadRequestException();
        }
        String identifyId = customerFilter.getIdentityNumber();

        HDContractResponse contractResponse = null;

        List<HDContractResponse> hdContractResponses = hdMiddleService.getListContractByIdentifyId(identifyId);

        if (hdContractResponses == null || hdContractResponses.isEmpty()) {
            throw new NotFoundException(1421, "IdentifyId does not exits");
        }
        ContractCustomer contractCustomer = new ContractCustomer();
        if (hdContractResponses != null && !hdContractResponses.isEmpty()) {
            for (HDContractResponse response : hdContractResponses) {
                if (contractResponse == null) {
                    contractResponse = response;
                }

                contractResponse = compareLastUpdatePhone(contractResponse, response);
                Contract contract = contractService.getContractByContractCode(response.getContractNumber());
                if (contract != null) {

                    List<ContractCustomer> contractCustomers = contractCustomerService.getListContractCustomerByContractUuid(contract.getContractUuid());
                    if (contractCustomers != null && !contractCustomers.isEmpty()) {
                        if (contractCustomers.size() > 1) {
                            throw new NotFoundException(1441, "He thong chua the xac dinh duoc");
                        }
                        contractCustomer = contractCustomers.get(0);
                        verifyResponse.setCustomerUuid(contractCustomer.getCustomerUuid());
                        contractService.updateContractByCustomerUuid(contractCustomer.getCustomerUuid());
                    }
                    lstContract.add(contract);
                }
            }
        }

        if (lstContract.isEmpty()) {
            throw new NotFoundException(1421, "IdentifyId does not exits");
        }

        if (contractResponse != null) {
            verifyResponse.setPhoneNumber(contractResponse.getPhoneNumber());
        }

        return ok(verifyResponse);
    }

    /**
     * Check customer info is valid to forgot password or not
     *
     * @param req CustomerFilter contain info need check
     * @return VerifyResponse contain info result after check successfully
     */
    @PostMapping("/checkValidateForgotPasswordByIdentifyIdAndContractCode")
    public ResponseEntity<?> checkValidateForgotPasswordByIdentifyIdAndContractCode(@RequestBody RequestDTO<CustomerFilter> req) {

        VerifyResponse verifyResponse = new VerifyResponse();
        CustomerFilter customerFilter = req.init();
        if (customerFilter == null) {
            throw new BadRequestException();
        }
        String identifyId = customerFilter.getIdentityNumber();
        String contractCode = customerFilter.getContractCode();

        Contract contract = contractService.getContractByContractCode(contractCode);

        if (contract == null) {
            throw new NotFoundException(1422, "Contract number not registered!");
        }

        if (contract.getIdentifyId().replaceAll(" ", "").length() != identifyId.length()) {
            throw new BadRequestException(1435, "Other identity card number at registration !");
        }

        HDContractResponse hdContractResponse = hdMiddleService.getContractByContractCodeAndIdentifyId(contractCode, identifyId);

        if (hdContractResponse == null) {
            throw new NotFoundException(1400, "Contract code or Identify does not exits");
        }

        ContractCustomer contractCustomer = new ContractCustomer();
        List<ContractCustomer> customers = contractCustomerService.getListContractCustomerByContractUuid(contract.getContractUuid());
        List<String> contractCodes = new ArrayList<>();
        String phoneNumber = "";
        if (customers != null && !customers.isEmpty()) {
            contractCustomer = customers.get(0);
            verifyResponse.setCustomerUuid(contractCustomer.getCustomerUuid());
            for (ContractCustomer item : customers) {
                contractCodes.add(item.getContractCode());
            }
            phoneNumber = getPhoneVerify(contractCodes);

        }

        verifyResponse.setPhoneNumber(phoneNumber);


        return ok(verifyResponse);
    }

    /**
     * Find list of customer id by contract code
     *
     * @param req IdPayload contain list string of contract code
     * @return list string of customer id
     */
    @PostMapping("/getCustomerIdsByContractCodes")
    public ResponseEntity<?> getListContractByContractCodes(@RequestBody RequestDTO<IdPayload> req) {

        List<String> customerIds = new ArrayList<>();

        IdPayload jArray = req.getPayload();
        List<String> ids = (List<String>) jArray.getId();
        List<Contract> contracts = contractService.getContractByContractCodes(ids);
        for (Contract contract : contracts) {
            List<ContractCustomer> contractCustomers = contractCustomerService.getListContractCustomerByContractUuid(contract.getContractUuid());

            if (contractCustomers == null || contractCustomers.isEmpty()) {
                //throw new NotFoundException(1401, "Contract customer does not exits");
                customerIds.add("");
            }else {
                customerIds.add(contractCustomers.get(0).getCustomerUuid().toString());
            }
        }

        return ok(customerIds);
    }

    /**
     * Find list of contract code by customer id
     *
     * @param req IdPayload contain list string of customer id
     * @return list string of contract code
     */
    @PostMapping("/getAllContactCodeByCustomerIds")
    public ResponseEntity<?> getAllContactCodeByCustomerIds(@RequestBody RequestDTO<IdPayload> req) {

        List<ResponeCustomerSearch> responseCustomerSearches = new ArrayList<>();
        try {
            IdPayload payload = req.getPayload();
            String strIds = (String) payload.getId();

            Log.print("ID CUSTOMER REQUEST :" + strIds);

            List<String> list = new ArrayList<String>(Arrays.asList(strIds.split(",")));

            if (list != null && list.size() > 0) {
                for (String item : list) {
                    List<ContractCodeAndStatus> contractCodeAndStatuses = new ArrayList<>();
                    ResponeCustomerSearch customerSearch = new ResponeCustomerSearch();
                    UUID customerId = UUID.fromString(item);
                    List<ContractCustomer> contractCustomers = contractCustomerService.getListContractCustomerByCustomerUuidAndStatus(customerId, 1);
                    List<String> contractCodes = new ArrayList<>();
                    if (contractCustomers != null && !contractCustomers.isEmpty()) {
                        for (ContractCustomer contractCustomer : contractCustomers) {
                            if (contractCustomer.getStatus() == 0) {
                                continue;
                            }
                            if (!contractCodes.contains(contractCustomer.getContractCode())) {
                                contractCodes.add(contractCustomer.getContractCode());
                            }
                        }
                    }

                    if (contractCodes != null && contractCodes.size() > 0) {
                        List<Contract> contracts = contractService.getContractByContractCodes(contractCodes);
                        for (Contract contract : contracts) {
                            if (contract != null) {
                                ContractCodeAndStatus codeAndStatus = new ContractCodeAndStatus();
                                codeAndStatus.setContractCode(contract.getLendingCoreContractId());
                                codeAndStatus.setStatus(ContractUtils.convertStatus(contract.getStatus()));
                                codeAndStatus.setIdentifyId(contract.getIdentifyId());
                                codeAndStatus.setPhoneNumber(contract.getPhone());
                                contractCodeAndStatuses.add(codeAndStatus);
                            }
                        }
                    }

                    customerSearch.setData(contractCodeAndStatuses);
                    responseCustomerSearches.add(customerSearch);
                }
            }
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        return ok(responseCustomerSearches);
    }

    /**
     * Write logs status of contract
     *
     * @param req ContractLogStatusRequest contain info need to write logs
     * @return http status code
     */
    @PostMapping("/contract_log_status")
    public ResponseEntity<?> contractLogStatus(@RequestBody RequestDTO<ContractLogStatusRequest> req) {

        ContractLogStatusRequest request = req.init();
        ContractLogStatus contractLogStatus = new ContractLogStatus();
        contractLogStatus.setContractUuid(UUID.fromString(request.getContractUuid()));
        contractLogStatus.setStatus(request.getStatus());
        contractLogStatus.setCreatedAt(req.now());
        contractLogStatus.setCreatedBy(req.jwt().getUuid());
        contractLogStatusService.create(contractLogStatus);
        return ok();
    }

    /**
     * UpdateMonthlyDueDate of contract
     *
     * @param req UpdateMonthlyDueDate contain info of contract need to update
     * @return http status code
     */
    @PostMapping("/updateMonthlyDueDate")
    public ResponseEntity<?> updateMonthlyDueDate(@RequestBody RequestDTO<UpdateMonthlyDueDate> req) {

        UpdateMonthlyDueDate monthlyDueDate = req.init();

        String contractCode = monthlyDueDate.getContractCode();

        Contract contract = contractService.getContractByContractCode(contractCode);

        if (contract == null) {
            throw new NotFoundException(1400, "Contract Code does not exits !");
        }

        //boolean isUpdate = hdMiddleService.updateMonthlyDueDateContract(monthlyDueDate);

        ContractEditInfo contractEditInfo = contractEditInfoService.getContractEditInfoByContractCode(monthlyDueDate.getContractCode());
        if (contractEditInfo == null) {
            contractEditInfo = new ContractEditInfo();
            contractEditInfo.setContractCode(monthlyDueDate.getContractCode());
            contractEditInfo.setMonthlyDueDate(monthlyDueDate.getMonthlyDueDate());
            contractEditInfo.setCreatedAt(new Date());
            contractEditInfo.setFirstDate(monthlyDueDate.getFirstDate());
            contractEditInfo.setEndDate(monthlyDueDate.getEndDate());
        }else {
            contractEditInfo.setMonthlyDueDate(monthlyDueDate.getMonthlyDueDate());
            contractEditInfo.setFirstDate(monthlyDueDate.getFirstDate());
            contractEditInfo.setEndDate(monthlyDueDate.getEndDate());
            contractEditInfo.setUpdatedAt(new Date());
        }

        contractEditInfoService.saveOrUpdate(contractEditInfo);

        return ok(null);
    }

    /**
     * UpdateChassisNoAndEngineerNo of contract
     *
     * @param req UpdateChassisNoAndEnginerNo contain info of contract need to update
     * @return http status code
     */
    @PostMapping("/updateChassisNoAndEngineerNo")
    public ResponseEntity<?> updateChassisNoAndEngineerNo(@RequestBody RequestDTO<UpdateChassisNoAndEnginerNo> req) {

        UpdateChassisNoAndEnginerNo updateChassisNoAndEnginerNo = req.init();

        String contractCode = updateChassisNoAndEnginerNo.getContractCode();

        Contract contract = contractService.getContractByContractCode(contractCode);

        if (contract == null) {
            throw new NotFoundException(1400, "Contract Code does not exits !");
        }

        boolean isUpdate = hdMiddleService.updateChassisNoAndEngineerNo(updateChassisNoAndEnginerNo);

        ContractEditInfo contractEditInfo = contractEditInfoService.getContractEditInfoByContractCode(updateChassisNoAndEnginerNo.getContractCode());
        if (contractEditInfo == null) {
            contractEditInfo = new ContractEditInfo();
            contractEditInfo.setContractCode(updateChassisNoAndEnginerNo.getContractCode());
            contractEditInfo.setChassisno(updateChassisNoAndEnginerNo.getChassisNo());
            contractEditInfo.setEnginerno(updateChassisNoAndEnginerNo.getEngineerNo());
            contractEditInfo.setCreatedAt(new Date());
        }else {
            contractEditInfo.setChassisno(updateChassisNoAndEnginerNo.getChassisNo());
            contractEditInfo.setEnginerno(updateChassisNoAndEnginerNo.getEngineerNo());
            contractEditInfo.setUpdatedAt(new Date());
        }

        contractEditInfoService.saveOrUpdate(contractEditInfo);

        return ok(null);
    }

    /**
     * Update status a contract
     *
     * @param req contain contract code need to update
     * @return http status code
     */
    @PostMapping("/updateStatusContract")
    public ResponseEntity<?> updateStatusByContractCode(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload idPayload = req.init();

        String contractCode = (String) idPayload.getId();

        Contract contract = contractService.getContractByContractCode(contractCode);

        if (contract == null) {
            throw new NotFoundException(1400, "Contract Code does not exits !");
        }
        boolean isUpdate = true;

        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contractCode);

        if (contractInfo == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }

        // check status from middle db and update

        //isUpdate = hdMiddleService.updateStatusByContractCode(updateStatus);


        if (isUpdate) {
            contract.setStatus("DOCUMENT VERIFICATION");
            contractService.updateContract(contract);
        }

        ContractEditInfo contractEditInfo = contractEditInfoService.getContractEditInfoByContractCode(contractCode);

        if (contractEditInfo == null) {
            contractEditInfo = new ContractEditInfo();
            contractEditInfo.setContractCode(contractCode);
            contractEditInfo.setCreatedAt(new Date());
            contractEditInfo.setIsUpdateChassinoEnginerno(-1);
            contractEditInfo.setIsUpdateConprintToDocveri(-1);
        }

        if (isUpdate) {
            contractEditInfo.setIsUpdateAdjustment(1);
            contractEditInfo.setUpdateAdjustmentAt(new Date());
        }else {
            contractEditInfo.setIsUpdateAdjustment(0);
            contractEditInfo.setUpdateAdjustmentAt(new Date());
        }

        contractEditInfoService.saveOrUpdate(contractEditInfo);

        //write log customer
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống HDSaison cập nhật trạng thái hợp đồng");
        joiner.add("- Mã số Hợp đồng: " + contractCode);
        joiner.add("- Trạng thái: " + "DOCUMENT VERIFICATION");
        joiner.add("- Ngày ký: ");
        joiner.add("- Hình thức ký Hợp đồng: ");
        joiner.add("- Loại chữ ký điện tử: ");
        joiner.add("- Số điện thoại nhận mã xác thực ký Hợp đồng: ");
        joiner.add("- Phương tiện điện tử sử dụng ký Hợp đồng: ");
        joiner.add("- Loại chứng từ: ");
        joiner.add("- Định dạng: ");
        joiner.add("- Độ dài: ");
        log.writeLogAction(req, "Cập nhật trạng thái Hợp đồng đã được ký thành công", joiner.toString(), contractCode, "", "", contractCode, "esign");

        return ok(null);
    }

    /**
     * Update status of contract when verify info success
     *
     * @param req contain contract code
     * @return http status code
     */
    @PostMapping("/updateStatusContractAfterDocVerify")
    public ResponseEntity<?> updateStatusContractAfterDocVerify(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload idPayload = req.init();

        String contractCode = (String) idPayload.getId();

        ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(contractCode);

        if (contractInfo == null) {
            throw new BadRequestException(1406, "Contract is does not exits");
        }


        boolean isUpdate = hdMiddleService.updateStatusAdjustmentInfo(contractCode);

        ContractEditInfo contractEditInfo = contractEditInfoService.getContractEditInfoByContractCode(contractCode);

        if (contractEditInfo == null) {
            contractEditInfo = new ContractEditInfo();
            contractEditInfo.setContractCode(contractCode);
            contractEditInfo.setIsUpdateMonthlyDueDate(-1);
            contractEditInfo.setIsUpdateChassinoEnginerno(-1);
            contractEditInfo.setIsUpdateAdjustment(-1);
            contractEditInfo.setCreatedAt(new Date());
        }

        if (isUpdate) {
            contractEditInfo.setIsUpdateAdjustment(1);
            contractEditInfo.setUpdateAdjustmentAt(new Date());
        }else {
            contractEditInfo.setIsUpdateAdjustment(0);
            contractEditInfo.setUpdateAdjustmentAt(new Date());
        }

        contractEditInfoService.saveOrUpdate(contractEditInfo);

        return ok(null);
    }

    /**
     * Create Disbursement Info of contract
     *
     * @param req DisbursementInfo contain info need to create
     * @return http status code
     */
    @PostMapping("/updateBankInformation")
    public ResponseEntity<?> updateBankInformation(@RequestBody RequestDTO<DisbursementInfo> req) {

        DisbursementInfo disbursementInfo = req.init();

        hdMiddleService.insertDisbursementInfo(disbursementInfo);

        return ok(null);
    }

    /**
     * Confirm sign a congtract
     *
     * @param req ConfirmSignContract contain contract info to confirm
     * @return http status code
     */
    @PostMapping("/confirmEsignContract")
    public ResponseEntity<?> confirmEsignContract(@RequestBody RequestDTO<ConfirmSignContract> req) {

        ConfirmSignContract confirmSignContract = req.init();
        boolean isUpdateMonthly = true;
        boolean isUpdateChassisNo = true;
        if (confirmSignContract != null) {

            UpdateMonthlyDueDate monthlyDueDate = confirmSignContract.getUpdateMonthlyDueDate();

            String contractCode = "";
            if (monthlyDueDate != null) {
                // isUpdateMonthly = hdMiddleService.updateMonthlyDueDateContract(monthlyDueDate);
                contractCode = monthlyDueDate.getContractCode();

            }

            UpdateChassisNoAndEnginerNo updateChassisNoAndEnginerNo = confirmSignContract.getUpdateChassisNoAndEnginerNo();
            if (updateChassisNoAndEnginerNo != null) {

                 isUpdateChassisNo = hdMiddleService.updateChassisNoAndEngineerNo(updateChassisNoAndEnginerNo);

                contractCode = updateChassisNoAndEnginerNo.getContractCode();

            }

            if (!HDUtil.isNullOrEmpty(contractCode)) {

                ContractEditInfo contractEditInfo = contractEditInfoService.getContractEditInfoByContractCode(contractCode);
                if (contractEditInfo == null) {
                    contractEditInfo = new ContractEditInfo();
                    contractEditInfo.setCreatedAt(new Date());
                    contractEditInfo.setContractCode(contractCode);
                    if (monthlyDueDate != null) {
                        contractEditInfo.setMonthlyDueDate(monthlyDueDate.getMonthlyDueDate());
                        contractEditInfo.setFirstDate(monthlyDueDate.getFirstDate());
                        contractEditInfo.setEndDate(monthlyDueDate.getEndDate());
                    }

                    if (updateChassisNoAndEnginerNo != null) {
                        contractEditInfo.setChassisno(updateChassisNoAndEnginerNo.getChassisNo());
                        contractEditInfo.setEnginerno(updateChassisNoAndEnginerNo.getEngineerNo());
                    }
                } else {
                    contractEditInfo.setUpdatedAt(new Date());
                    if (monthlyDueDate != null) {
                        contractEditInfo.setMonthlyDueDate(monthlyDueDate.getMonthlyDueDate());
                        contractEditInfo.setFirstDate(monthlyDueDate.getFirstDate());
                        contractEditInfo.setEndDate(monthlyDueDate.getEndDate());
                    }

                    if (updateChassisNoAndEnginerNo != null) {
                        contractEditInfo.setChassisno(updateChassisNoAndEnginerNo.getChassisNo());
                        contractEditInfo.setEnginerno(updateChassisNoAndEnginerNo.getEngineerNo());
                    }
                }

                if (isUpdateMonthly) {
                    contractEditInfo.setIsUpdateMonthlyDueDate(1);
                    contractEditInfo.setUpdateMonthlyDueDateAt(new Date());
                }else {
                    contractEditInfo.setUpdateMonthlyDueDateAt(new Date());
                    contractEditInfo.setIsUpdateMonthlyDueDate(0);
                }

                if (isUpdateChassisNo) {
                    contractEditInfo.setIsUpdateChassinoEnginerno(1);
                    contractEditInfo.setUpdateChassinoEnginernoAt(new Date());
                }else {
                    contractEditInfo.setIsUpdateChassinoEnginerno(0);
                    contractEditInfo.setUpdateChassinoEnginernoAt(new Date());
                }
                contractEditInfo.setIsUpdateAdjustment(-1);
                contractEditInfo.setIsUpdateConprintToDocveri(-1);
                contractEditInfoService.saveOrUpdate(contractEditInfo);
            }

            DisbursementInfo disbursementInfo = confirmSignContract.getDisbursementInfo();

            if (disbursementInfo != null) {
                hdMiddleService.insertDisbursementInfo(disbursementInfo);

                //write log customer
                StringJoiner joiner = new StringJoiner("\r\n");
                joiner.add("Khách hàng cập nhật thông tin tài khoản ngân hàng để nhận giải ngân");
                joiner.add("- Chủ tài khoản: " + disbursementInfo.getAccountName());
                joiner.add("- Số tài khoản: " + disbursementInfo.getAccountNumber());
                joiner.add("- Tên ngân hàng: " + disbursementInfo.getBankName());
                if (monthlyDueDate != null) {
                    joiner.add("- Ngày thanh toán đầu tiên: " + monthlyDueDate.getMonthlyDueDate());
                }
                log.writeLogAction(req, "Cập nhật thông tin giải ngân", joiner.toString(), confirmSignContract.toString(), "", disbursementInfo.toString(), disbursementInfo.getContractCode(), "esign");
            }
        }

        return ok(null);
    }

    /**
     * Update Disbursement Info of contract
     *
     * @param req UpdateDisbursementInfo contain info need to update
     * @return http status code
     */
    @PostMapping("/updateDisbursementInfo")
    public ResponseEntity<?> updateDisbursementInfo(@RequestBody RequestDTO<UpdateDisbursementInfo> req) {

        UpdateDisbursementInfo updateDisbursementInfo = req.init();

        hdMiddleService.updateDisbursementInfo(updateDisbursementInfo);

        return ok(null);
    }

    /**
     * Api for test call procedure
     * @param req
     * @return
     */
    @PostMapping("/testCallProcedure")
    public ResponseEntity<?> testCallProcedure(@RequestBody RequestDTO<EmptyPayload> req) {

        List<String> list = contractService.getAllContractCodesLive();

        return ok(list);
    }

    /**
     * Connect LDAP
     *
     * @param req RequestConnectLDap contain info to authorization
     * @return display name of ldap
     */
    @PostMapping("/ldap_connect")
    public ResponseEntity<?> connectLDapCheckUser(@RequestBody RequestDTO<RequestConnectLDap> req) {

        RequestConnectLDap requestConnectLDap = req.init();

        String displayName = hdMiddleService.checkStaffWithConnectLdap(requestConnectLDap);

        return ok(displayName);
    }

    /**
     * Find all contract existed by customer
     *
     * @param req IdentifyIds contain list of identify number of customer
     * @return list HDContractResponse contain info of contract
     */
    @PostMapping("/getAllContractByIdentifyId")
    public ResponseEntity<?> getAllContractByIdentifyId(@RequestBody RequestDTO<IdentifyIds> req) {
        IdentifyIds request = req.init();

        List<HDContractResponse> hdContractResponses = hdMiddleService.getListContractByIdentifyIds(request);

        return ok(hdContractResponses);
    }

    /**
     * Api test find logs of contract from middle service
     *
     * @param req
     * @return list HDContractResponse contain info of contract
     */
    @PostMapping("/testLog")
    public ResponseEntity<?> testLog(@RequestBody RequestDTO<EmptyPayload> req) {

        List<HDContractResponse> hdContractResponses = hdMiddleService.getListContractByIdentifyIdRealTime("072084001165");

        return ok(hdContractResponses);
    }

    /**
     * Update contract by customer id
     *
     * @param req contain customer uuid
     * @return http status code
     */
    @PostMapping("/updateContractByCustomer")
    public ResponseEntity<?> updateContractByCustomer(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload idPayload = req.init();

        String customerId = (String) idPayload.getId();

        UUID customerUuid = UUID.fromString(customerId);

        contractService.updateContractByCustomerUuid(customerUuid);

        return ok(null);
    }

    /**
     * Find info need to show popup to customer
     *
     * @param req contain customer uuid
     * @return list PopupNotification contain info need show to customer
     */
    @PostMapping("/popupNotification")
    public ResponseEntity<?> popupNotification(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload idPayload = req.init();

        String customerId = (String) idPayload.getId();

        UUID customerUuid = UUID.fromString(customerId);

        List<PopupNotification> list = contractService.getListContractDuePayment(customerUuid);

        List<String> contractCodes = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (PopupNotification popupNotification : list) {
                contractCodes.add(popupNotification.getContractCode());
            }

            List<ConfigContractTypeBackground> backgrounds = getConfigContractType(contractCodes);
            int index = 0;
            if (backgrounds != null && backgrounds.size() > 0) {
                for (PopupNotification item : list) {
                    item.setStatus(ContractUtils.convertStatus(item.getStatus()));
                    item.setLoanType(backgrounds.get(index).getContractName());
                    item.setUrlImage(backgrounds.get(index).getBackgroupImageLink());
                    index++;
                }
            }
        }

        return ok(list);
    }

    /**
     * updatePaymentNotification for customer
     *
     * @param req PaymentNotification contain info need to update
     * @return http status code
     */
    @PostMapping("/updatePaymentNotification")
    public ResponseEntity<?> updatePaymentNotification(@RequestBody RequestDTO<PaymentNotification> req) {

        PaymentNotification paymentNotification = req.init();

        Integer isPaymentNotification = paymentNotification.getIsNotification();

        if (isPaymentNotification == null || isPaymentNotification < 0){
            isPaymentNotification = 1;
        }

        ContractCustomer contractCustomer = contractCustomerService.getByContractCodeAndCustomerUuid(paymentNotification.getContractCode(),paymentNotification.getCustomerUuid());

        if (contractCustomer == null) {
            throw new BadRequestException(1400,"Contract does not exits");
        }

        contractCustomer.setIsRepaymentNotification(isPaymentNotification);
        contractCustomerService.updateContractCustomer(contractCustomer);

        return ok(null);
    }

    /**
     *  Get info payment of customer
     *
     * @param req PaymentNotification contain info need to find
     * @return ContractCustomer contain info payment of customer
     */
    @PostMapping("/checkIsPaymentNotification")
    public ResponseEntity<?> checkIsPaymentNotification(@RequestBody RequestDTO<PaymentNotification> req) {

        PaymentNotification paymentNotification = req.init();

        ContractCustomer contractCustomer = contractCustomerService.getByContractCodeAndCustomerUuid(paymentNotification.getContractCode(),paymentNotification.getCustomerUuid());

        if (contractCustomer == null) {
            throw new BadRequestException(1400,"Contract does not exits");
        }

        return ok(contractCustomer);
    }

    /**
     * Get info disbursement bank of contract
     *
     * @param req info contract and customer request
     * @return ResultDisbursementInfo contain info of disbursement
     */
    @PostMapping("/getInformationBank")
    public ResponseEntity<?> getInformationBank(@RequestBody RequestDTO<InformationBank> req) {

        InformationBank informationBank = req.init();

        String contractCode = informationBank.getContractCode();
        SearchDisbursementInfo search = new SearchDisbursementInfo();
        search.setCustomerUuid(informationBank.getCustomerUuid());
        search.setIsSent(-1);
        List<ResultDisbursementInfo> resultDisbursementInfos = null;
        ResultDisbursementInfo resultDisbursementInfo = null;
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }

        return ok(resultDisbursementInfo);
    }

    /**
     * Get info of customer
     *
     * @param req contain id of customer
     * @return  info of customer
     */
    @PostMapping("/getInformationCustomer")
    public ResponseEntity<?> getInformationCustomer(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload idPayload = req.init();

        CustomerInfo customerInfo = null;

        String strId = (String) idPayload.getId();

        UUID customerUuid = UUID.fromString(strId);

        List<String> contractCodes = new ArrayList<>();
        List<ContractCustomer> contractCustomers = contractCustomerService.getListContractCustomerByCustomerUuidAndStatus(customerUuid,1);

        if (contractCustomers != null && contractCustomers.size() > 0) {
            for (ContractCustomer c : contractCustomers) {
                if (!HDUtil.isNullOrEmpty(c.getContractCode())){
                    contractCodes.add(c.getContractCode());
                }
            }
        }

        if (contractCodes != null && contractCodes.size() > 0) {
            PhoneAndStatus phoneAndStatus = getContractLastUpdated(contractCodes);
            if (phoneAndStatus != null) {
                ContractInfo contractInfo = hdMiddleService.getContractDetailFromMidServer(phoneAndStatus.getContractNumber());

                customerInfo = new CustomerInfo();
                customerInfo.setPhoneNumber(phoneAndStatus.getPhoneNumber());
                customerInfo.setIdentifyId(contractInfo.getNationalID());
                customerInfo.setFullName(contractInfo.getLastName() + " " + contractInfo.getMidName() + " " + contractInfo.getFirstName());
            }
        }

        return ok(customerInfo);
    }

    /**
     * Find phone number of customer last updated
     *
     * @param contractCodes list contract code of customer
     * @return info of phone number last updated
     */
    public PhoneAndStatus getContractLastUpdated(List<String> contractCodes) {
        List<PhoneAndStatus> phoneAndStatuses = hdMiddleService.getPhoneAndStatusByContractCodes(contractCodes);
        PhoneAndStatus phoneAndLastUpdate = null;
        if (phoneAndStatuses != null && phoneAndStatuses.size() > 0) {
            for (PhoneAndStatus phoneAndStatus : phoneAndStatuses) {
                if (phoneAndLastUpdate == null) {
                    phoneAndLastUpdate = phoneAndStatus;
                    continue;
                }
                phoneAndLastUpdate = getPhoneCurrentByLastUpdate(phoneAndLastUpdate, phoneAndStatus);
            }
        }

        return phoneAndLastUpdate;
    }

    /**
     * Create a new contract
     *
     * @param hdContractResponse info of contract
     * @param customerUuid
     * @param contractCodes
     * @param validateContract info is validated
     * @return generate contract is successfully or not
     */
    public boolean insertContract(HDContractResponse hdContractResponse, UUID customerUuid, List<String> contractCodes, ValidateContract validateContract) {
        try {

            Contract con = contractService.getContractByContractCode(hdContractResponse.getContractNumber());
            UUID contractUuid = null;
            if (con == null) {

                // insert contract
                Contract contract = new Contract();
                contract.setCreatedAt(new Date());
                contract.setLendingCoreContractId(hdContractResponse.getContractNumber());
                contract.setContractUuid(UUID.randomUUID());

                String phoneNumber = hdContractResponse.getPhoneNumber();

                if (phoneNumber.length() == 10) {
                    contract.setPhone(HDUtil.maskNumber(phoneNumber, "*** *** ####"));
                } else {
                    contract.setPhone(HDUtil.maskNumber(phoneNumber, "*** ****####"));
                }

                String nationalId = "";
                if (hdContractResponse.getNationalID().length() == 9) {
                    nationalId = HDUtil.maskNumber(hdContractResponse.getNationalID(), "*** *** ###");
                } else {
                    nationalId = HDUtil.maskNumber(hdContractResponse.getNationalID(), "**** **** ####");
                }
                contract.setIdentifyId(nationalId);
                contract.setStatus(hdContractResponse.getStatus());
                contract.setIsInsurance(hdContractResponse.getIsInsurance());
                // Date
                contract.setContractCompleteDate(DateUtils.covertStringToDate(hdContractResponse.getContractPrintingCompleted()));
                contract.setFirstDue(hdContractResponse.getFirstDue());
                contract.setEndDue(hdContractResponse.getEndDue());
                contract.setDocumentVerificationDate(DateUtils.covertStringToDate(hdContractResponse.getDocumentVerificationDate()));
                contract.setContractPrintingDate(DateUtils.covertStringToDate(hdContractResponse.getContractPrintingDate()));

                contract.setLastUpdateApplicant(hdContractResponse.getLastUpdateApplicant());

                // String
                contract.setContractCompleteDateTemp(hdContractResponse.getContractPrintingCompleted());
                if (hdContractResponse.getFirstDue() != null) {
                    contract.setFirstDueTemp(hdContractResponse.getFirstDue().toString());
                }
                if (hdContractResponse.getEndDue() != null) {
                    contract.setEndDueTemp(hdContractResponse.getEndDue().toString());
                }

                contract.setDocumentVerificationDateTemp(hdContractResponse.getDocumentVerificationDate());
                contract.setContractPrintingDateTemp(hdContractResponse.getContractPrintingDate());

                // BigDecimal
                contract.setTenor(hdContractResponse.getTenor());
                contract.setLoanAmount(hdContractResponse.getLoanAmount());
                contract.setMonthlyDueDate(hdContractResponse.getMonthlyDueDate());
                contract.setMonthlyInstallmentAmount(hdContractResponse.getMonthlyInstallmentAmount());
                contractService.createContract(contract);

                contractUuid = contract.getContractUuid();
            } else {
                contractUuid = con.getContractUuid();
            }

            // insert contract customer
            ContractCustomer contractCustomer = new ContractCustomer();
            contractCustomer.setContractUuid(contractUuid);
            contractCustomer.setCreatedAt(new Date());
            contractCustomer.setCustomerUuid(customerUuid);
            contractCustomer.setStatus(1);
            contractCustomer.setIsRepaymentNotification(1);
            contractCustomer.setContractCode(hdContractResponse.getContractNumber());
            contractCustomerService.insertContractCustomer(contractCustomer);

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Check contract is valid by info request
     *
     * @param validateContract info validate
     * @param hdContractResponse contract info
     * @param isPhone is validate by phone or not
     * @return contract is valid or not
     */
    public boolean checkContractByNameOrPhoneOrBirthdayOrFamilyBookNo(ValidateContract validateContract, HDContractResponse hdContractResponse, boolean isPhone) {

        try {
            if (validateContract == null) {
                return true;
            }
            String fullNameOld = (validateContract.getFirstName() + validateContract.getLastName()).replaceAll("\\s", "");
            //fullNameOld = HDUtil.unAccent(fullNameOld);

            String fullNameNew = (hdContractResponse.getFirstName() + hdContractResponse.getLastName()).replaceAll("\\s", "");
            //fullNameNew = HDUtil.unAccent(fullNameNew);

            if (fullNameOld.equals(fullNameNew)) {
                return true;
            }

            if (!isPhone) {
                String phoneOld = validateContract.getPhone();
                String phoneNew = hdContractResponse.getPhoneNumber();

                if (phoneOld != null && phoneNew != null && phoneOld.equals(phoneNew)) {
                    return true;
                }
            }

            Date birthdayOld = validateContract.getBirthday();
            Date birthdayNew = hdContractResponse.getDob();

            if (birthdayOld != null && birthdayNew != null && birthdayOld.compareTo(birthdayNew) == 0) {
                return true;
            }

            String familyBookNoOld = validateContract.getFamilyBookNo();
            String familyBookNoNew = hdContractResponse.getFamilyBookNo();

            String driversLicenceOld = validateContract.getDriversLicence();
            String driversLicenceNew = hdContractResponse.getDriversLicence();


            if ((!HDUtil.isNullOrEmpty(familyBookNoOld) && !HDUtil.isNullOrEmpty(familyBookNoNew) && familyBookNoOld.equals(familyBookNoNew))
                    || (!HDUtil.isNullOrEmpty(driversLicenceOld) && !HDUtil.isNullOrEmpty(driversLicenceNew) && driversLicenceOld.equals(driversLicenceNew))) {
                return true;
            }

        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    /**
     * Generate list info of contract
     *
     * @param contracts list contract object
     * @param contractInfos list contract info of middle
     * @param contractCodes list contract code
     * @return list ContractResponse contain info all of them
     */
    public List<ContractResponse> getContractResponseByContracts(List<Contract> contracts, List<ContractInfo> contractInfos, List<String> contractCodes) {

        String contractName = "";

        List<ContractResponse> contractResponses = new ArrayList<>();


        List<ConfigContractTypeBackground> backgrounds = getConfigContractType(contractCodes);

        if (backgrounds == null || backgrounds.isEmpty()) {
            throw new InternalServerErrorException();
        }

        int index = 0;
        for (Contract contract : contracts) {
            ContractResponse contractResponse = new ContractResponse();
            contractResponse.setContractCode(contract.getLendingCoreContractId());
            contractResponse.setContractUuid(contract.getContractUuid());
            contractResponse.setIdentifyId(contract.getIdentifyId());
            contractResponse.setPhoneNumber(contract.getPhone());
            contractResponse.setStatus(ContractUtils.convertStatus(contract.getStatus()));
            contractResponse.setLoanAmount(contract.getLoanAmount());

            contractResponse.setFullName(convertNameCustomer(contractInfos.get(index)));
            contractResponse.setLoanType(backgrounds.get(index).getContractName());

            contractResponses.add(contractResponse);

            index++;
        }

        return contractResponses;
    }

    /**
     * Generate full name of customer
     *
     * @param contractInfo info of contract
     * @return full name of customer
     */
    public String convertNameCustomer(ContractInfo contractInfo) {
        String fullNameFm = "";
        if (contractInfo != null) {
            int len = contractInfo.getFirstName().length();
            StringBuilder lastName = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                lastName.append('*');
            }

            fullNameFm = lastName.toString() + " " + contractInfo.getMidName() + " " + contractInfo.getFirstName();
        }
        return fullNameFm;
    }

    public List<ContractResponseMobile> getContractResponseMobileByContracts(List<Contract> contracts, List<ContractByCustomer> contractByCustomers) {

        List<String> contractCodes = new ArrayList<>();
        for (Contract item : contracts) {
            contractCodes.add(item.getLendingCoreContractId());
        }

        List<ContractResponseMobile> contractResponseMobiles = new ArrayList<>();

        String urlImage = "";

        List<ConfigContractTypeBackground> backgrounds = getConfigContractType(contractCodes);

        if (backgrounds == null || backgrounds.isEmpty()) {
            throw new InternalServerErrorException();
        }

        int index = 0;
        for (Contract contract : contracts) {

            List<ContractAdjustmentInfo> contractAdjustmentInfos = contractAdjustmentInfoService.getListContractAdjustmentInfoByContractCode(contract.getLendingCoreContractId());

            List<String> valueChanges = new ArrayList<>();
            List<String> keyChanges = new ArrayList<>();
            if (contractAdjustmentInfos != null && contractAdjustmentInfos.size() > 0) {
                for (ContractAdjustmentInfo item : contractAdjustmentInfos) {
                    if (item.getValue() != null || item.getValueConfirm() != null) {
                        keyChanges.add(item.getKey());
                    }
                }
            }

            // call authorize check config
            try {
                IdPayload idPayload = new IdPayload();
                String listString = String.join(",", keyChanges);
                idPayload.setId(listString);

                Invoker invoker = new Invoker();

                ResponseDTO<Object> dto = invoker.call(configAdjustmentContract + "/get_list_name", idPayload, new ParameterizedTypeReference<ResponseDTO<Object>>() {
                });

                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                    valueChanges = (List<String>) dto.getPayload();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ContractResponseMobile contractResponseMobile = new ContractResponseMobile();
            contractResponseMobile.setContractCode(contract.getLendingCoreContractId());
            contractResponseMobile.setContractUuid(contract.getContractUuid());
            contractResponseMobile.setStatus(contract.getStatus());
            contractResponseMobile.setLoanAmount(contract.getLoanAmount());

            List<String> lstWaiting = new ArrayList<String>(Arrays.asList(WAIT_FOR_SIGNING_CONTRACT.split(",")));

            // ngay het han ky
            contractResponseMobile.setContractPrintingDate(contract.getContractPrintingDate());
            Date endDateEsign = contract.getContractPrintingDate();
            if (lstWaiting.contains(contract.getStatus())) {
                ContractEsigned contractEsigned = contractEsignedService.findByContractId(contract.getContractUuid());
                if (contractEsigned != null) {
                    contractResponseMobile.setContractPrintingDate(contractEsigned.getCreatedAt());
                }else{
                    if (endDateEsign != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(endDateEsign);
                        c.add(Calendar.DATE, 30);
                        endDateEsign = c.getTime();
                    }
                }
            }

            contractResponseMobile.setValueChanges(valueChanges);

            contractResponseMobile.setEndDate(endDateEsign);
            if (backgrounds.get(index) != null) {
                contractResponseMobile.setLoanType(backgrounds.get(index).getContractName());
                contractResponseMobile.setUrlImage(backgrounds.get(index).getBackgroupImageLink());
                contractResponseMobile.setLoanCode(backgrounds.get(index).getContractType());
            } else {
                contractResponseMobile.setUrlImage(urlImage);
            }

            if (contract.getMonthlyDueDate() != null) {
                contractResponseMobile.setMonthlyDueDate(contract.getMonthlyDueDate().intValue());
            }

            if (contract.getTenor() != null) {
                contractResponseMobile.setTenor(contract.getTenor().intValue());
            }

            if (contractByCustomers != null && contractByCustomers.size() > 0) {
                for (ContractByCustomer con : contractByCustomers) {
                    if (!con.getContractCode().equals(contract.getLendingCoreContractId())) {
                        continue;
                    }
                    contractResponseMobile.setStatusType(con.getType());
                }
            }

            contractResponseMobiles.add(contractResponseMobile);

            index++;
        }
        return contractResponseMobiles;
    }

    /**
     * Invoke config service to Get loan type of contract
     *
     * @param contractCode
     * @return loan type
     */
    public String getLoanType(String contractCode) {
        String loanType = "";
        try {
            IdPayload idPayload = new IdPayload();
            idPayload.setId(contractCode);

            Invoker invoker = new Invoker();

            ResponseDTO<Object> dto = invoker.call(urlConfigStaffRequest + "/get_kind_offer", idPayload, new ParameterizedTypeReference<ResponseDTO<Object>>() {
            });

            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                loanType = (String) dto.getPayload();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return loanType;
    }

    /**
     * Invoke config service to get contract type background
     *
     * @param contractCodes
     * @return list ConfigContractTypeBackground
     */
    public List<ConfigContractTypeBackground> getConfigContractType(List<String> contractCodes) {

        List<ConfigContractTypeBackground> contractTypeBackgrounds = new ArrayList<>();

        Invoker invoker = new Invoker();

        String strIds = StringUtils.join(contractCodes, ",");

        ObjectMapper mapper = new ObjectMapper();
        IdPayload idPayload = new IdPayload();
        idPayload.setId(strIds);
        try {
            ResponseDTO<Object> dto = invoker.call(configContractTypeBackgroundRequest + "/find", idPayload, new ParameterizedTypeReference<ResponseDTO<Object>>() {
            });
            if (dto != null && dto.getCode() == 200) {

                contractTypeBackgrounds = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<List<ConfigContractTypeBackground>>() {
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contractTypeBackgrounds;
    }

//    public static void main(String[] args) {
//
//        String a = "Tường Kr'Nor";
//
//        String[] b = a.split(" ");
//
//        System.out.println(HDUtil.unAccent(b[b.length - 1]));
//    }

}
