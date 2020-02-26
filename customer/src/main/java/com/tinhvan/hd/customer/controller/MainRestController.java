package com.tinhvan.hd.customer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.base.enities.StaffLogAction;
import com.tinhvan.hd.base.file.FileS3DTORequest;
import com.tinhvan.hd.base.file.FileS3DTOResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.customer.file.service.FileStorageService;
import com.tinhvan.hd.customer.model.*;
import com.tinhvan.hd.customer.payload.*;
import com.tinhvan.hd.customer.service.*;
import com.tinhvan.hd.customer.utils.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/v1/customer")
public class MainRestController extends HDController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerTokenService customertokenService;

    @Autowired
    private CustomerImageService customerimageService;

    @Autowired
    private CustomerDeviceService customerDeviceService;

    @Autowired
    private FileStorageService fileLocalStorageService;

    @Autowired
    private LoginConfigService loginConfigService;

    @Autowired
    private CustomerLogActionService customerLogActionService;

    @Autowired
    private StaffLogActionService staffLogActionService;

    @Autowired
    private RegisterByPhoneService registerByPhoneService;

    @Value("${app.module.contract.service.url}")
    private String urlContractRequest;
    @Value("${app.module.sms.service.url}")
    private String urlSMSRequest;

    @Value("${app.module.filehandler.service.url}")
    private String urlFileHandlerRequest;

    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    private StringJoiner joiner;
    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();
    private IdPayload idPayload = new IdPayload();
    private Logger logger = Logger.getLogger(MainRestController.class.getName());

    /**
     * Invoke contract service get all contract code by customer uuid and fill result in to customer object
     *
     * @param customers list customer needed to get contract code
     */
    private void setListContactForCustomerList(List<Customer> customers) {
        if (ObjectUtils.isEmpty(customers)) {
            return;
        }
        List<LoginConfig> loginConfigs = loginConfigService.findAll();
        List<UUID> uuids = new ArrayList<>();
        for (Customer customer : customers) {
            uuids.add(customer.getUuid());

            //check customer is locking
            if (customer.getLockedLoginAt() != null && customer.getCountLoginFail() > 0) {
                long lockedTime = 0;
                for (LoginConfig config : loginConfigs) {
                    if (config.getCountTime() == customer.getCountLoginFail()) {
                        lockedTime = config.getLockedTime();
                        break;
                    }
                }
                long lockedTo = lockedTime + HDUtil.getUnixTime(customer.getLockedLoginAt());
                customer.setLockedLoginTo(new Date(lockedTo * 1000));
                //if customer time out locked
                if (lockedTo <= HDUtil.getUnixTimeNow()) {
                    customer.setLockedLoginAt(null);
                    customer.setLockedLoginTo(null);
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (UUID uuid : uuids) {
            stringBuilder.append(uuid.toString()).append(",");
        }
        String ids = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        List<ListContractResponse> contractLists = invokeContract_getAllContractCodeByCustomerIds(ids);
        if (org.springframework.util.ObjectUtils.isEmpty(contractLists)) {
            throw new InternalServerErrorException();
        }
        int index = 0;
        for (Customer customer : customers) {
            customer.setContacts(contractLists.get(index++).getData());
        }
    }

    /**
     * Register a customer require for use lending app
     *
     * @param req object SignUpRequest contain fields require for register new customer
     * @return uuid of customer has registered
     */
    @PostMapping(value = "/sign_up")
    @Transactional
    public ResponseEntity<?> sign_up(@RequestBody RequestDTO<SignUpRequest> req) {

        //validation
        SignUpRequest signUpRequest = req.init();

        //validate customer exist
        Customer customer = customerService.findByUsername(signUpRequest.getUsername());
        if (customer != null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] sign_up " + signUpRequest.toString());
            return badRequest(1112);
        }

        Customer customerForm = new Customer();
        customerForm.setUsername(signUpRequest.getUsername());
        customerForm.setPhoneNumber(signUpRequest.getPhoneNumber());
        customerForm.setPhoneNumberOrigin(signUpRequest.getPhoneNumber());
        customerForm.setUserNameShow(signUpRequest.getUserNameFm());
        customerForm.init(req.now(), req.langCode());
        //insert customer
        customerService.insert(customerForm);

        return ok(customerForm.getUuid());
    }

    /**
     * Register a customer require for use lending app
     *
     * @param req object SignUpRequest contain fields require for register new customer
     * @return uuid of customer has registered
     */
    @PostMapping(value = "/getByUsername")
    public ResponseEntity<?> getByUsername(@RequestBody RequestDTO<IdPayload> req) {

        //validation
        IdPayload idPayload = req.init();

        String userName = (String) idPayload.getId();

        //validate customer exist
        Customer customer = customerService.findByUsername(userName);
        if (customer == null) {
            throw new NotFoundException(2222,"Customer not found");
        }

        if (customer.getStatus() != 1) {
            throw new NotFoundException(2222,"Customer not found");
        }
        return ok(customer.getUuid());
    }

    /**
     * Update a customer exist in lending app
     *
     * @param req object UpdateRequest contain information needed update
     * @return http status code and object customer has been updated
     */
    @PostMapping(value = "/update")
    @Transactional
    public ResponseEntity<?> update(@RequestBody RequestDTO<UpdateRequest> req) {

        //validate auth request
        UpdateRequest updateRequest = req.init();
        JWTPayload jwtPayload = req.jwt();

        //validate customer exist
        if (!HDUtil.isNullOrEmpty(updateRequest.getEmail())) {
            Customer customerTemp = customerService.findByEmail(updateRequest.getEmail());
            if (customerTemp != null && !customerTemp.getUuid().toString().equals(updateRequest.getUuid()))
                return badRequest(1116, "email already exist");
        }
        Customer customer = customerService.findByUuid(UUID.fromString(updateRequest.getUuid()), -1);
        if (customer == null || customer.getStatus() == HDConstant.STATUS.DISABLE) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] update " + updateRequest.toString());
            return notFound(1107, "customer not found");
        }
        validateToken_Customer(jwtPayload, customer);
        if (customer.getObjectVersion() != updateRequest.getObjectVersion()) {
            return conflict();
        }
        String ov = customer.toString();
        //update customer
        customer.update(updateRequest, req.now(), jwtPayload.getUuid(), req.langCode());
        customerService.update(customer);
        writeLogAction(req, "customer", "update", updateRequest.toString(), ov, customer.toString(), "update_customer", "");

        Customer customerClone = null;
        try {
            customerClone = (Customer) customer.clone();
            if (!HDUtil.isNullOrEmpty(customerClone.getPhoneNumber())) {
                customerClone.setPhoneNumber(HDUtil.maskNumber(customerClone.getPhoneNumber(), "*** *** ####"));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return ok(customerClone);
    }

    /**
     * Customer use this api for login
     *
     * @param req object SignInRequest contain information login of customer
     * @return object AuthResponse contain data needed for client
     */
    @PostMapping(value = "/sign_in")
    @Transactional
    public ResponseEntity<?> sign_in(@RequestBody RequestDTO<SignInRequest> req) {
        StopWatch sw = new StopWatch("sing in");
        sw.start("f1");
        //get request
        SignInRequest customerForm = req.init();
        String reqUsername = customerForm.getUsername();
        String reqPassword;
        if (customerForm.isEncryptPassword()) {
            try {
                reqPassword = AES256Provider.decrypt(customerForm.getPassword(), customerForm.getUsername());
            } catch (Exception e) {
                Log.error("customer", this.getClass().getName() + " [BAD REQUEST] sign_in " + customerForm.toString());
                e.printStackTrace();
                return badRequest(1110, "invalid username or password");
            }
        } else
            reqPassword = DigestUtils.sha512Hex(customerForm.getPassword());
        sw.stop();
        sw.start("f2");
        //validate customer exist
        Customer customer = customerService.findByUsername(reqUsername);
        if (customer == null || customer.getStatus() == -1) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] sign_in " + customerForm.toString());
            return badRequest(1110, "invalid username or password");
        }
        sw.stop();
        /*
        if(customer.getRequireChangePassword()==HDConstant.STATUS.ENABLE){
            throw new RequirePasswordException();
        }
        */
        sw.start("f3");
        //valid if customer has over time check last login fail then set count login fail = 0
        if (customer.getCountLoginFail() > 0 && customer.getLastLoginFailAt() != null) {
            int checkTime = loginConfigService.find(0);
            long checkTimeTo = checkTime + HDUtil.getUnixTime(customer.getLastLoginFailAt());
            long timeNow = HDUtil.getUnixTimeNow();
            if (checkTimeTo <= timeNow) {
                customer.setCountLoginFail(0);
            }
        }
        //valid customer has still locked login
        if (customer.getCountLoginFail() > 0 && customer.getLockedLoginAt() != null) {
            int lockedTime = loginConfigService.find(customer.getCountLoginFail());
            long lockedTo = lockedTime + HDUtil.getUnixTime(customer.getLockedLoginAt());
            long timeNow = HDUtil.getUnixTimeNow();
            if (lockedTo > timeNow) {
                return badRequest(1120, new LoginFailResponse(Integer.valueOf(String.valueOf(lockedTo - timeNow)), customer.getCountLoginFail()));
            }
        }
        //customer login password not match
        if (customer.getPassword() == null)
            return badRequest(1110, "invalid username or password");
        if (!customer.getPassword().equals(reqPassword)) {
            customer.setCountLoginFail(customer.getCountLoginFail() + 1);
            int lockedTime = loginConfigService.find(customer.getCountLoginFail());
            customer.setLastLoginFailAt(req.now());
            if (lockedTime > 0) {
                customer.setLockedLoginAt(req.now());
                customerService.update(customer);
                return badRequest(1120, new LoginFailResponse(lockedTime, customer.getCountLoginFail()));
            }
            customerService.update(customer);
            return badRequest(1110, "invalid username or password");
        }
        //customer has been disabled
        if (customer.getStatus() == HDConstant.STATUS.DISABLE) {
            return badRequest(1115, "customer has blocked");
        }
        if (customer.getCountLoginFail() > 0) {
            customer.setCountLoginFail(0);
            customer.setLockedLoginAt(null);
            customerService.update(customer);
        }
        sw.stop();
        sw.start("f4");
        //insert customer_token
        CustomerToken customerToken = initCustomerToken(customer, req.now(), req.environment());
        customertokenService.insert(customerToken);
        sw.stop();
        sw.start("f5");
        //encrypt password
        String encrypted = "";//AES256Provider.encrypt(customer.getPassword(), customer.getUsername());
        sw.stop();
        joiner = new StringJoiner("\r\n");
        joiner.add("Khách hàng đăng nhập tài khoản để ký Hợp đồng trực tuyến.");
        joiner.add("- Họ và tên: " + customer.getFullName());
        joiner.add("- Tên tài khoản: " + customer.getUsername());
        joiner.add("- Mật khẩu đăng nhập: " + customer.getPassword());
        if (customerForm.isEncryptPassword())
            joiner.add("- Hình thức đăng nhập: sinh trắc học");
        if (!customerForm.isEncryptPassword())
            joiner.add("- Hình thức đăng nhập: nhập mật khẩu");
        sw.start("f6");
        writeLogAction(req, "Đăng nhập tài khoản", joiner.toString(), customerForm.toString(), "", customer.toString(), "esign", customer.getUuid().toString());
        sw.stop();
        sw.start("f7");
        // update contract from middle db
        try {
            IdPayload idPayload = new IdPayload();
            idPayload.setId(customer.getUuid());
            ResponseDTO<Object> dto = invoker.call(urlContractRequest + "/updateContractByCustomer", idPayload,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                System.out.println("Update contract when sign in success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sw.stop();
        System.out.println(sw.prettyPrint());
        Customer customerClone = null;
        try {
            customerClone = (Customer) customer.clone();
            if (!HDUtil.isNullOrEmpty(customerClone.getPhoneNumberOrigin())) {
                customerClone.setPhoneNumber(HDUtil.maskNumber(customerClone.getPhoneNumberOrigin(), "*** *** ####"));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return ok(new AuthResponse(customerToken.getToken(), customerClone, encrypted));
    }

    @PostMapping(value = "/enable_finger_login")
    @Transactional
    public ResponseEntity<?> enableFingerLogin(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), -1);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] enable_finger_login " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }
        validateToken_Customer(req.jwt(), customer);

        //encrypt password
        String encrypted = AES256Provider.encrypt(customer.getPassword(), customer.getUsername());

        return ok(encrypted);
    }

    /**
     * Check information SignInRequest is it valid or not
     *
     * @param req object SignInRequest contain information login of customer
     * @return http status code
     */
    @PostMapping(value = "/validate_sign_in")
    @Transactional
    public ResponseEntity<?> validate_sign_in(@RequestBody RequestDTO<SignInRequest> req) {

        //get request
        SignInRequest customerForm = req.init();
        String reqUsername = customerForm.getUsername();
        String reqPassword;
        if (customerForm.isEncryptPassword()) {
            try {
                reqPassword = AES256Provider.decrypt(customerForm.getPassword(), customerForm.getUsername());
            } catch (Exception e) {
                Log.error("customer", this.getClass().getName() + " [BAD REQUEST] sign_in " + customerForm.toString());
                e.printStackTrace();
                return badRequest(1110, "invalid username or password");
            }
        } else
            reqPassword = DigestUtils.sha512Hex(customerForm.getPassword());

        //validate customer exist
        Customer customer = customerService.findByUsername(reqUsername);
        if (customer == null || customer.getStatus() == -1) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] sign_in " + customerForm.toString());
            return badRequest(1110, "invalid username or password");
        }
        //customer login password not match
        if (!customer.getPassword().equals(reqPassword)) {
            customer.setCountLoginFail(customer.getCountLoginFail() + 1);
            int lockedTime = loginConfigService.find(customer.getCountLoginFail());
            customer.setLastLoginFailAt(req.now());
            if (lockedTime > 0) {
                customer.setLockedLoginAt(req.now());
                customerService.update(customer);
                return badRequest(1120, new LoginFailResponse(lockedTime, customer.getCountLoginFail()));
            }
            customerService.update(customer);
            return badRequest(1110, "invalid username or password");
        }

        joiner = new StringJoiner("\r\n");
        joiner.add("Khách hàng đăng nhập tài khoản để ký Hợp đồng trực tuyến.");
        joiner.add("- Họ và tên: " + customer.getFullName());
        joiner.add("- Tên tài khoản: " + customer.getUsername());
        joiner.add("- Mật khẩu đăng nhập: " + customer.getPassword());
        if (customerForm.isEncryptPassword())
            joiner.add("- Hình thức đăng nhập: sinh trắc học");
        if (!customerForm.isEncryptPassword())
            joiner.add("- Hình thức đăng nhập: nhập mật khẩu");
        writeLogAction(req, "Đăng nhập tài khoản", joiner.toString(), customerForm.toString(), "", customer.toString(), "esign", customer.getUuid().toString());

        //customer has been disabled
        if (customer.getStatus() == HDConstant.STATUS.DISABLE) {
            return badRequest(1115, "customer has blocked");
        }
        if (customer.getCountLoginFail() > 0) {
            customer.setCountLoginFail(0);
            customer.setLockedLoginAt(null);
            customerService.update(customer);
        }
        return ok();
    }

    /**
     * Customer using this api for logout lending app
     *
     * @param req object IdPayload contain private key of customer
     * @return http status code
     */
    @PostMapping(value = "/sign_out")
    @Transactional
    public ResponseEntity<?> sign_out(@RequestBody RequestDTO<IdPayload> req) {

        //get request
        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), -1);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] sign_out " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }

        //validateToken_Customer(req.jwt(), customer);

        //disabled customer_token
        customertokenService.disable(customer.getUuid(), req.environment(), req.now());

        //disabled customer_fcm_token
        if (req.jwt().getEnvironment().equals(HDConstant.ENVIRONMENT.APP)) {
            customerDeviceService.disableByUuidOrToken(customer.getUuid(), "");
        }
        writeLogAction(req, "Đăng xuất tài khoản", "Tên tài khoản: " + customer.getUsername(), uuidRequest.getId().toString(), "", "", "logout", customer.getUuid().toString());
        return ok();
    }

    /**
     * Customer turn off notification on current device
     *
     * @param req object IdPayload contain private key of customer
     * @return http status code
     */
    @PostMapping(value = "/disableByUuidOrToken")
    @Transactional
    public ResponseEntity<?> disableByUuidOrToken(@RequestBody RequestDTO<IdPayload> req) {
        validateToken_Staff(req.jwt());
        //validate customer exist
        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), -1);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] disableByUuidOrToken " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }
        validateToken_Customer(req.jwt(), customer);
        customerDeviceService.disableByUuidOrToken(customer.getUuid(), "");
        return ok();
    }

    /**
     * Customer create new password when registered successfully
     *
     * @param req object CreatePasswordRequest contain information new password require
     * @return object AuthResponse contain data needed for client
     */
    //sign up action
    @PostMapping(value = "/create_password")
    @Transactional
    public ResponseEntity<?> create_password(@RequestBody RequestDTO<CreatePasswordRequest> req) {

        //validate auth request
        CreatePasswordRequest passwordRequest = req.init();
        String uuid = passwordRequest.getUuid();

        joiner = new StringJoiner("\r\n");
        joiner.add("Khách hàng thiết lập mật khẩu đăng nhập");
        joiner.add("-Mật khẩu: " + passwordRequest.getNewPassword());
        joiner.add("-Xác nhận mật khẩu: " + passwordRequest.getNewPasswordRewrite());
        writeLogAction(req, "Cài đặt mật khẩu đăng nhập", joiner.toString(), passwordRequest.toString(), "", "", "register", uuid);

        String newPassword = passwordRequest.getNewPassword();
        String newPasswordRewrite = passwordRequest.getNewPasswordRewrite();
        //StringJoiner joinerValidatePassword = new StringJoiner("\r\n");
        //validate password
        if (HDUtil.isNullOrEmpty(newPassword)) {
            //joinerValidatePassword.add("-Sai thông tin mật khẩu");
            //writeLogAction(req, "Cài đặt mật khẩu đăng nhập", joiner.toString(), passwordRequest.toString(), "", "", "register");
            throw new BadRequestException(1123);
        }
        if (newPasswordRewrite == null || !newPassword.equals(newPasswordRewrite)) {
            //joinerValidatePassword.add("-Sai thông tin mật khẩu");
            //writeLogAction(req, "Cài đặt mật khẩu đăng nhập", joiner.toString(), passwordRequest.toString(), "", "", "register");
            throw new BadRequestException(1124);
        }

        Customer customer = customerService.findByUuid(UUID.fromString(uuid), -1);
        joiner = new StringJoiner("\r\n");
        joiner.add("Đúng thông tin mật khẩu");
        writeLogAction(req, "Kiểm tra thông tin mật khẩu", joiner.toString(), passwordRequest.toString(), "", customer.toString(), "register", uuid);
        if (customer == null || customer.getLastModifyPassword() != null || customer.getStatus() == -1) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] create_password " + passwordRequest.toString());
            return notFound(1107, "customer not found");
        }
        joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống HD SAISON cập nhật thông tin tài khoản");
        joiner.add("Họ và tên: " + customer.getFullName());
        joiner.add("Tên tài khoản: " + customer.getUsername());
        joiner.add("Mật khẩu đăng nhập cũ: " + customer.getPassword());
        writeLogAction(req, "Cập nhật thông tin mật khẩu", joiner.toString(), passwordRequest.toString(), "", customer.toString(), "register", uuid);
        //update customer
        updatePassword(customer, passwordRequest.getNewPassword(), req);

        //insert customer_token
        CustomerToken customerToken = initCustomerToken(customer, req.now(), req.environment());
        customertokenService.insert(customerToken);
        //encrypt password
        String encrypted = AES256Provider.encrypt(customer.getPassword(), customer.getUsername());

        Customer customerClone = null;
        try {
            customerClone = (Customer) customer.clone();
            if (!HDUtil.isNullOrEmpty(customerClone.getPhoneNumber())) {
                customerClone.setPhoneNumber(HDUtil.maskNumber(customerClone.getPhoneNumber(), "*** *** ####"));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ok(new AuthResponse(customerToken.getToken(), customerClone, encrypted));
    }

    /**
     * Admin disable one customer in lending app system, don't care current status of customer
     *
     * @param req object IdPayload contain private key of customer
     * @return http status code
     */
    @PostMapping(value = "/disable")
    @Transactional
    public ResponseEntity<?> disable(@RequestBody RequestDTO<IdPayload> req) {
        validateToken_Staff(req.jwt());
        //validate customer exist
        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), -1);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] disable " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }
        String ov = customer.toString();

        //update customer
        customer.changeStatus(req.now(), req.jwt().getUuid(), HDConstant.STATUS.DISABLE);
        customerService.update(customer);
        customertokenService.disableAllByCustomer(customer.getUuid(), req.now());
        writeLogAction(req, "lock customer", "update", uuidRequest.getId().toString(), ov, customer.toString(), "", "");
        return ok();
    }

    /*@PostMapping(value = "/delete_forever")
    @Transactional
    public ResponseEntity<?> delete(@RequestBody RequestDTO<IdPayload> req) {
        validateToken_Staff(req.jwt());
        //validate customer exist
        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), -1);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] delete_forever " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }
        validateToken_Customer(req.jwt(), customer);

        //update customer
        customer.changeStatus(req.now(), req.jwt().getUuid(), HDConstant.STATUS.DELETE_FOREVER);
        customerService.update(customer);

        return ok();
    }*/

    /**
     * Admin enable one customer in lending app system, don't care current status of customer
     *
     * @param req object IdPayload contain private key of customer
     * @return http status code
     */
    @PostMapping(value = "/enable")
    @Transactional
    public ResponseEntity<?> enable(@RequestBody RequestDTO<IdPayload> req) {
        //validate customer exist
        IdPayload uuidRequest = req.init();
        String uuid = uuidRequest.getId().toString();
        validateIdPayload(uuidRequest);

        Customer customer = customerService.findByUuid(UUID.fromString(uuid), -1);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] enable " + uuid);
            return notFound(1107, "customer not found");
        }
        if (customer.getStatus() == -1) {
            //write log new customer (-1 == 1)
            joiner = new StringJoiner("\r\n");
            joiner.add("Hệ thống HD SAISON khởi tạo tài khoản");
            joiner.add("Họ và tên: " + customer.getFullName());
            joiner.add("Tên tài khoản: " + customer.getUsername());
            writeLogAction(req, "Khởi tạo tài khoản", joiner.toString(), uuid, "", "", "register", uuid);
            //update customer
            customer.setStatus(HDConstant.STATUS.ENABLE);
            customerService.update(customer);
            return ok(customer.getUsername());
        }
        customer.setStatus(HDConstant.STATUS.ENABLE);
        customer.setCountLoginFail(0);
        customer.setLockedLoginAt(null);
        customerService.update(customer);
        return ok();
    }

    /**
     * Admin switched status of one customer in lending app system from disable to enable and opposite
     *
     * @param req object IdPayload contain private key of customer
     * @return http status code
     */
    @PostMapping(value = "/change_status")
    @Transactional
    public ResponseEntity<?> change_status(@RequestBody RequestDTO<IdPayload> req) {
        validateToken_Staff(req.jwt());
        //validate customer exist
        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), -1);
        if (customer == null || customer.getStatus() == -1) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] change_status " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }

        //update customer
        if (customer.getStatus() == HDConstant.STATUS.DISABLE) {
            customer.changeStatus(req.now(), req.jwt().getUuid(), HDConstant.STATUS.ENABLE);
            customertokenService.disableAllByCustomer(customer.getUuid(), req.now());
        }
        if (customer.getStatus() == HDConstant.STATUS.ENABLE) {
            customer.changeStatus(req.now(), req.jwt().getUuid(), HDConstant.STATUS.DISABLE);
        }
        customerService.update(customer);

        return ok();
    }

    /**
     * Admin unlock one customer in lending app system when it has blocked
     *
     * @param req object PhoneNumberRequest contain information customer given admin
     * @return http status code
     */
    @PostMapping(value = "/unlock")
    @Transactional
    public ResponseEntity<?> unlock(@RequestBody RequestDTO<PhoneNumberRequest> req) {

        validateToken_Staff(req.jwt());

        PhoneNumberRequest phoneNumberRequest = req.init();

        Log.print("Request API", phoneNumberRequest);
        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(phoneNumberRequest.getCustomerUuid()), -1);
        if (customer == null || customer.getStatus() == -1) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] unlock " + phoneNumberRequest.toString());
            return notFound(1107, "customer not found");
        }
        String ov = customer.toString();

        customer.setStatus(HDConstant.STATUS.ENABLE);
        customer.setCountLoginFail(0);
        customer.setLockedLoginAt(null);
        if (!HDUtil.isNullOrEmpty(phoneNumberRequest.getPhoneNumber())) {
            resetPasswordBySms(customer, phoneNumberRequest, req);
            customertokenService.disableAllByCustomer(customer.getUuid(), req.now());
        }
        customerService.update(customer);
        writeLogAction(req, "unlock customer", "update", phoneNumberRequest.toString(), ov, customer.toString(), "", "");
        return ok();
    }

    /**
     * This api not use now but in future may be use it
     */
    /*@PostMapping(value = "/forgot_password_by_sms")
    @Transactional
    public ResponseEntity<?> forgot_password_by_sms(@RequestBody RequestDTO<PhoneNumberRequest> req) {

        PhoneNumberRequest phoneNumber = req.init();

        //validate customer exist
        Customer customer = customerService.findByPhoneNumber(phoneNumber.getPhoneNumber());
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] forgot_password_by_sms " + phoneNumber.toString());
            return badRequest(1107);
        }
        if (customer.getStatus() == HDConstant.STATUS.DISABLE) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] forgot_password_by_sms " + phoneNumber.toString());
            return badRequest(1115);
        }
        //general new password
        Utils utils = new Utils();
        String newPassword = utils.getRandomPassword(6, 8);
        //update customer
        customer.setPassword(DigestUtils.sha512Hex(newPassword));
        customer.setLastModifyPassword(req.now());
        if (req.jwt() != null)
            customer.setModifiedBy(req.jwt().getUuid());
        else
            customer.setModifiedBy(customer.getUuid());
        customer.setModifiedAt(req.now());
        customer.setRequireChangePassword(HDConstant.STATUS.DISABLE);
        customer.setCountLoginFail(0);
        customer.setLockedLoginAt(null);
        customer.increaseObjectVersion();
        customerService.update(customer);
        customertokenService.disableAllByCustomer(customer.getUuid(), req.now());
        //send sms
        SMSResponse smsResponse = new SMSResponse(phoneNumber.getPhoneNumber(), "password:" + newPassword);
        //rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_SMS, smsRespone);

        return ok(newPassword);
    }*/

    /**
     * This api not use now but in future may be use it
     */
    /*@PostMapping(value = "/forgot_password_by_email")
    @Transactional
    public ResponseEntity<?> forgot_password_by_email(@RequestBody RequestDTO<EmailRequest> req) {

        Config config = HDConfig.getInstance();
        EmailRequest email = req.init();

        //validate customer exist
        Customer customer = customerService.findByEmail(email.getEmail());
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] forgot_password_by_email " + email.toString());
            return badRequest(1107);
        }

        //general password token
        long expiredAt = (HDUtil.getUnixTime(req.now())
                + Long.valueOf(config.get("CUSTOMER_FORGOT_PASSWORD_TOKEN_EXPIRED_TIME")))
                * 1000L;
        String token = DigestUtils
                .md5Hex(UUID.randomUUID().toString())
                .toUpperCase();

        CustomerForgotPasswordToken passwordToken = new CustomerForgotPasswordToken();
        passwordToken.setCustomerUuid(customer.getUuid());
        passwordToken.setEmail(email.getEmail());
        passwordToken.setToken(token);
        passwordToken.setStatus(HDConstant.STATUS.ENABLE);
        passwordToken.setCreateAt(req.now());
        passwordToken.setExpiredAt(new Date(expiredAt));
        customerForgotPasswordTokenService.insert(passwordToken);

        //send email
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_SMS, passwordToken);

        return ok(passwordToken);
    }*/

    /**
     * This api not use now but in future may be use it
     */
    /*@PostMapping(value = "/generate_new_password_by_email")
    @Transactional
    public ResponseEntity<?> generate_new_password_by_email(@RequestBody RequestDTO<NewPasswordFromEmailRequest> req) {
        NewPasswordFromEmailRequest emailRequest = req.init();

        //validate token exist
        CustomerForgotPasswordToken passwordToken = customerForgotPasswordTokenService.findActive(emailRequest.getToken());

        //validate token request
        if (passwordToken == null
                || !passwordToken.getEmail().equals(emailRequest.getEmail())
                || req.now().getTime() > passwordToken.getExpiredAt().getTime()) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] generate_new_password_by_email " + passwordToken.toString());
            return badRequest(1108, "invalid email token");
        }

        //validate customer exist
        Customer customer = customerService.findByUuid(passwordToken.getCustomerUuid(), HDConstant.STATUS.ENABLE);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] generate_new_password_by_email " + passwordToken.getCustomerUuid().toString());
            return badRequest(1107);
        }
        //update customer
        updatePassword(customer, emailRequest.getNewPassword(), req);

        //disable token
        passwordToken.setStatus(HDConstant.STATUS.DISABLE);
        customerForgotPasswordTokenService.update(passwordToken);
        //encrypt password
        //String encrypted = AES256Provider.encrypt(customer.getPassword(), customer.getUsername());

        return ok();
    }*/

    /**
     * View detail one customer
     *
     * @param req object IdPayload contain private key of customer
     * @return information customer
     */
    @PostMapping(value = "/detail")
    public ResponseEntity<?> detail(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        //validate customer exist
        Customer customer;
        if (HDUtil.isNullOrEmpty(req.jwt().getRole()) || req.jwt().getRole().equals(HDConstant.ROLE.CUSTOMER))
            customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), HDConstant.STATUS.ENABLE);
        else
            customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), -1);

        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] detail " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }
        validateToken_Customer(req.jwt(), customer);

        return ok(customer);
    }

    /**
     * Get information status of one customer required
     *
     * @param req object IdPayload contain private key of customer
     * @return object ValidateCustomerResponse contain information needed for client
     */
    @PostMapping(value = "/valid_customer")
    public ResponseEntity<?> valid_customer(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), -1);

        if (customer == null || customer.getStatus() == HDConstant.STATUS.DISABLE) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] valid_customer " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }
        return ok(new ValidateCustomerResponse(customer.getUsername(), customer.getStatus()));
    }

    /**
     * This api using for upload image of customer
     *
     * @param req object ImageRequest contain information image uploading of customer
     * @return object CustomerImage contain information image uploaded
     */
    @PostMapping(value = "/image")
    @Transactional
    public ResponseEntity<?> image(@RequestBody RequestDTO<ImageRequest> req) {

        ImageRequest imageRequest = req.init();

        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(imageRequest.getUuid()), HDConstant.STATUS.ENABLE);
        if (customer == null) {
            return notFound(1107, "customer not found");
        }
        validateToken_Customer(req.jwt(), customer);
        if (HDUtil.isNullOrEmpty(imageRequest.getFileName())) {
            CustomerImage customerImage = customerimageService.findByType(UUID.fromString(imageRequest.getUuid()), imageRequest.getType());
            if (customerImage != null) {
                customerImage.setModifiedAt(req.now());
                customerImage.setActive(HDConstant.STATUS.DISABLE);
                customerimageService.update(customerImage);
            }
            if (imageRequest.getType() == CustomerImage.TYPE.AVATAR) {
                customer.setAvatar("");
                customerService.update(customer);
            }
            return ok();
        }
        CustomerImage customerImage = customerimageService.find(UUID.fromString(imageRequest.getUuid()), imageRequest.getFileName(), imageRequest.getType());
        if (customerImage == null) {
            //general new image
            customerImage = new CustomerImage();
            customerImage.setCreatedAt(req.now());
            customerImage.setActive(HDConstant.STATUS.ENABLE);
            customerImage.setUuid(UUID.fromString(imageRequest.getUuid()));
            customerImage.setType(imageRequest.getType());
            String uri = invokeFileHandlerS3_upload(customerImage, imageRequest.getFileName());
            if (HDUtil.isNullOrEmpty(uri))
                return badRequest(1125);
            customerImage.setFileName(uri);
            customerImage = customerimageService.insert(customerImage);
            writeLogAction(req, "customer image", "upload image", imageRequest.toString(), "", customerImage.toString(), "", "");
        }
        if (customerImage.getType() == CustomerImage.TYPE.AVATAR) {
            String ov = customer.getAvatar();
            customer.setAvatar(customerImage.getFileName());
            customerService.update(customer);
            writeLogAction(req, "customer", "upload avatar", imageRequest.toString(), ov, customer.getAvatar(), "", "");
        }
        return ok(customerImage);
    }

    /**
     * Customer need update will use this api
     *
     * @param req object UpdatePasswordRequest contain information request update new password
     * @return object AuthResponse contain information needed for client when update password successfully
     */
    //update password
    @PostMapping(value = "/update_new_password")
    @Transactional
    public ResponseEntity<?> update_new_password(@RequestBody RequestDTO<UpdatePasswordRequest> req) {
        UpdatePasswordRequest updatePasswordRequest = req.init();

        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(updatePasswordRequest.getUuid()), HDConstant.STATUS.ENABLE);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] update_new_password " + updatePasswordRequest.toString());
            return badRequest(1107);
        }
        validateToken_Customer(req.jwt(), customer);
        String ov = customer.getPassword();
        //validate current password
        if (!DigestUtils.sha512Hex(updatePasswordRequest.getCurrentPassword()).equals(customer.getPassword()))
            return badRequest(1111);

        joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống HD SAISON cập nhật thông tin tài khoản");
        joiner.add("-Họ và tên: " + customer.getFullName());
        joiner.add("-Tên tài khoản: " + customer.getUsername());

        String currentPassword = updatePasswordRequest.getCurrentPassword();
        String newPassword = updatePasswordRequest.getNewPassword();
        String newPasswordRewrite = updatePasswordRequest.getNewPasswordRewrite();
        joiner.add("-Mật khẩu đăng nhập cũ: " + currentPassword);

        //validate password
        if (HDUtil.isNullOrEmpty(newPassword)) {
            //joiner.add("-Cập nhật thất bại");
           //writeLogAction(req, "Cập nhật thông tin mật khẩu", joiner.toString(), updatePasswordRequest.toString(), ov, "", "register", "");
            throw new BadRequestException(1123);
        }
        if (newPasswordRewrite == null || !newPassword.equals(newPasswordRewrite)) {
            throw new BadRequestException(1124);
        }
        if (newPassword.equals(currentPassword)) {
            throw new BadRequestException(1130);
        }



        //update customer
        updatePassword(customer, updatePasswordRequest.getNewPassword(), req);

        //insert customer_token
        CustomerToken customerToken = initCustomerToken(customer, req.now(), req.environment());
        customertokenService.insert(customerToken);
        //encrypt password
        String encrypted = AES256Provider.encrypt(customer.getPassword(), customer.getUsername());

        //joiner.add("-Cập nhật thành công");
        //writeLogAction(req, "Cài đặt mật khẩu đăng nhập", joiner.toString(), updatePasswordRequest.toString(), ov, customer.toString(), "register", "");
        writeLogAction(req, "Cập nhật thông tin mật khẩu", joiner.toString(), updatePasswordRequest.toString(), ov, customer.toString(), "register", "");

        Customer customerClone = null;
        try {
            customerClone = (Customer) customer.clone();
            if (!HDUtil.isNullOrEmpty(customerClone.getPhoneNumber())) {
                customerClone.setPhoneNumber(HDUtil.maskNumber(customerClone.getPhoneNumber(), "*** *** ####"));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return ok(new AuthResponse(customerToken.getToken(), customerClone, encrypted));
    }

    /**
     * Customer create new password after requested forgot password successfully
     *
     * @param req object NewPasswordRequest contain information request create new password
     * @return object AuthResponse contain information needed for client when update password successfully
     */
    //forgot password
    @PostMapping(value = "/generate_new_password")
    @Transactional
    public ResponseEntity<?> generate_new_password(@RequestBody RequestDTO<NewPasswordRequest> req) {
        NewPasswordRequest newPasswordRequest = req.init();
        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(newPasswordRequest.getCustomerUUID()), HDConstant.STATUS.ENABLE);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] generate_new_password " + newPasswordRequest.toString());
            return badRequest(1107);
        }
        String ov = customer.getPassword();
        //update customer
        updatePassword(customer, newPasswordRequest.getNewPassword(), req);
        //insert customer_token
        CustomerToken customerToken = initCustomerToken(customer, req.now(), req.environment());
        customertokenService.insert(customerToken);
        //encrypt password
        String encrypted = AES256Provider.encrypt(customer.getPassword(), customer.getUsername());
        writeLogAction(req, "customer", "generate password", newPasswordRequest.toString(), ov, customer.getPassword(), "generate_pass", "");

        Customer customerClone = null;
        try {
            customerClone = (Customer) customer.clone();
            if (!HDUtil.isNullOrEmpty(customerClone.getPhoneNumber())) {
                customerClone.setPhoneNumber(HDUtil.maskNumber(customerClone.getPhoneNumber(), "*** *** ####"));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return ok(new AuthResponse(customerToken.getToken(), customerClone, encrypted));
    }

    /**
     * Admin find customers to management
     *
     * @param req object CustomerSearchRequest contain information needed search
     * @return object CustomerSearchResponse contain result needed for client
     */
    @PostMapping(value = "/search")
    public ResponseEntity<?> search(@RequestBody RequestDTO<CustomerSearchRequest> req) {
        validateToken_Staff(req.jwt());
        CustomerSearchRequest search = req.init();
//        System.out.println(search.toString());
        List<Customer> customers;
        int count;
        List<UUID> customerIds = new ArrayList<>();

        if (!HDUtil.isNullOrEmpty(search.getKeyWord())) {
            String[] key = search.getKeyWord().split(",");
            //B1
            List<String> contactCodes = new ArrayList<>();
            for (int i = 0; i < key.length; i++) {
                //search by identity card number and contact number
                if (search.getType() == CustomerSearchRequest.TYPE.ALL) {
                    contactCodes.addAll(invokeContact_getAllContractCodeByKeySearch(new ContractSearchRequest(key[i].trim(), key[i].trim())));
                }
                //search by identity card number
                if (search.getType() == CustomerSearchRequest.TYPE.IDENTITY) {
                    contactCodes.addAll(invokeContact_getAllContractCodeByKeySearch(new ContractSearchRequest("", key[i].trim())));
                }
                //search by contact number
                if (search.getType() == CustomerSearchRequest.TYPE.CONTACT) {
                    contactCodes.addAll(invokeContact_getAllContractCodeByKeySearch(new ContractSearchRequest(key[i].trim(), "")));
                }
            }
            removedDuplicates(contactCodes);
            //B2
            customerIds.addAll(invokeContract_getCustomerIdByContractCode(contactCodes));
            //removedDuplicates(customerIds);
            if (customerIds.isEmpty()) {
                customerIds.add(UUID.randomUUID());
            }
            //use for UI testing
            /*customers = customerService.findCustomerIdByFullNameOrEmail(search.getKeyWord(), search.getPageNum(), search.getPageSize(), search.getOderBy(), search.getDirection());
            count = customerService.countCustomerIdByFullNameOrEmail(search.getKeyWord());*/
        }
        //use else for UI testing
        /*else {
            //B3
            customers = customerService.find(customerIds, search.getPageNum(), search.getPageSize(), search.getOderBy(), search.getDirection());
            count = customerService.count(customerIds);
        }*/
        //B3
        customers = customerService.find(customerIds, search.getPageNum(), search.getPageSize(), search.getOrderBy(), search.getDirection());
        count = customerService.count(customerIds);
        //B4
        setListContactForCustomerList(customers);

        return ok(new CustomerSearchResponse(customers, count));
    }

    /**
     * Admin find customers to management
     *
     * @param req object CustomerSearchRequest contain information needed search
     * @return object CustomerSearchResponse contain result needed for client
     */
    @PostMapping(value = "/search_dashboard")
    public ResponseEntity<?> searchDashboard(@RequestBody RequestDTO<CustomerSearchRequest> req) {
        validateToken_Staff(req.jwt());
        CustomerSearchRequest search = req.init();
//        System.out.println(search.toString());
        List<Customer> customers;
        int count;
        List<UUID> customerIds = new ArrayList<>();

        if (!HDUtil.isNullOrEmpty(search.getKeyWord())) {
            String[] key = search.getKeyWord().split(",");
            //B1
            List<String> contactCodes = new ArrayList<>();
            for (int i = 0; i < key.length; i++) {
                //search by identity card number and contact number
                if (search.getType() == CustomerSearchRequest.TYPE.ALL) {
                    contactCodes.addAll(invokeContact_getAllContractCodeByKeySearch(new ContractSearchRequest(key[i].trim(), key[i].trim())));
                }
                //search by identity card number
                if (search.getType() == CustomerSearchRequest.TYPE.IDENTITY) {
                    contactCodes.addAll(invokeContact_getAllContractCodeByKeySearch(new ContractSearchRequest("", key[i].trim())));
                }
                //search by contact number
                if (search.getType() == CustomerSearchRequest.TYPE.CONTACT) {
                    contactCodes.addAll(invokeContact_getAllContractCodeByKeySearch(new ContractSearchRequest(key[i].trim(), "")));
                }
            }
            removedDuplicates(contactCodes);
            //B2
            customerIds.addAll(invokeContract_getCustomerIdByContractCode(contactCodes));
            //removedDuplicates(customerIds);
            if (customerIds.isEmpty()) {
                customerIds.add(UUID.randomUUID());
            }
            //use for UI testing
            /*customers = customerService.findCustomerIdByFullNameOrEmail(search.getKeyWord(), search.getPageNum(), search.getPageSize(), search.getOderBy(), search.getDirection());
            count = customerService.countCustomerIdByFullNameOrEmail(search.getKeyWord());*/
        }
        //use else for UI testing
        /*else {
            //B3
            customers = customerService.find(customerIds, search.getPageNum(), search.getPageSize(), search.getOderBy(), search.getDirection());
            count = customerService.count(customerIds);
        }*/
        //B3
        customers = customerService.find(customerIds, search.getPageNum(), search.getPageSize(), search.getOrderBy(), search.getDirection());
        count = customerService.count(customerIds);
        //B4
        setListContactForCustomerList(customers);

        return ok(new CustomerSearchResponse(customers, count));
    }


    /**
     * This api not use now but in future may be use it
     */
    /*@PostMapping(value = "/customer_filter_category")
    public ResponseEntity<?> customer_filter_category(RequestDTO req) {
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        List<CustomerFilterCategory> lst = customerFilterCategoryService.find();
        return ok(lst);
    }

    @PostMapping(value = "/compare_type")
    public ResponseEntity<?> compare_type(RequestDTO req) {
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        List<CompareType> lst = compareTypeService.find();
        return ok(lst);
    }*/

    /**
     * Customer register device to receipt notification from lending app
     *
     * @param req object CustomerDeviceRequest contain information device register
     * @return http status code
     */
    @PostMapping(value = "/device")
    public ResponseEntity<?> device(@RequestBody RequestDTO<CustomerDeviceRequest> req) {

        CustomerDeviceRequest device = req.init();
        if (HDUtil.isNullOrEmpty(device.getPreferLanguage()) || device.getPreferLanguage().length() > 2) {
            device.setPreferLanguage(req.langCode());
        }
        customerDeviceService.insert(device);

        return ok();
    }

    //@PostMapping(value = "/unsubscribe")
    public ResponseEntity<?> unsubscribe() {
        customerDeviceService.unsubscribe();
        return ok();
    }

    /**
     * Customer find current device is enable receipt notification
     *
     * @param req object IdPayload contain private key of customer
     * @return fcm token of device
     */
    @PostMapping(value = "/fcm_token")
    public ResponseEntity<?> fcm_token(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), HDConstant.STATUS.ENABLE);

        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] fcm_token " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }

        //get fcm token by customer uuid
        String fcm = "";
        List<CustomerDevice> devices = customerDeviceService.findByUuid(customer.getUuid());
        if (devices.size() > 0) {
            fcm = devices.get(0).getFcmToken();
        }
        return ok(fcm);
    }

    /**
     * Validate identity number of customer when forgot password
     *
     * @param req object ContractSearchRequest contain information needed verify
     * @return VerifyResponse contain information has verified successfully
     */
    @PostMapping(value = "/verify_identity_number")
    public ResponseEntity<?> verifyIdentityNumber(@RequestBody RequestDTO<ContractSearchRequest> req) {

        ContractSearchRequest searchRequest = req.init();
        searchRequest.setContractCode("");
        if (HDUtil.isNullOrEmpty(searchRequest.getIdentityNumber()))
            return badRequest(1222, "empty identityNumber");
        VerifyResponse response = invokeContact_getPhoneNumber(searchRequest, "checkValidateForgotPasswordByIdentifyId");
        if (response == null)
            return notFound(1421);
        //validate customer exist
        Customer customer = customerService.findByUuid(response.getCustomerUuid(), HDConstant.STATUS.ENABLE);
        if (customer == null || customer.getStatus() == -1) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] verify_identity_number " + response.toString());
            return badRequest(1107);
        }
        if (customer.getStatus() == HDConstant.STATUS.DISABLE) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] verify_identity_number " + response.toString());
            return badRequest(1115);
        }
        writeLogAction(req, "customer", "verify identity number", searchRequest.toString(), "", "", "", "");
        return ok(response);
    }

    /**
     * Validate identity number and contract code of customer when verify_identity_number fail
     *
     * @param req object ContractSearchRequest contain information needed verify
     * @return VerifyResponse contain information has verified successfully
     */
    @PostMapping(value = "/verify_identity_number_and_contract_code")
    public ResponseEntity<?> verifyIdentityNumberAndContractCode(@RequestBody RequestDTO<ContractSearchRequest> req) {

        ContractSearchRequest searchRequest = req.init();
        if (HDUtil.isNullOrEmpty(searchRequest.getIdentityNumber()))
            return badRequest(1222, "empty identityNumber");
        if (HDUtil.isNullOrEmpty(searchRequest.getContractCode()))
            return badRequest(1221, "empty contractCode");
        VerifyResponse response = invokeContact_getPhoneNumber(searchRequest, "checkValidateForgotPasswordByIdentifyIdAndContractCode");
        if (response == null)
            return notFound(1400);
        //validate customer exist

        Customer customer = customerService.findByUuid(response.getCustomerUuid(), -1);

        if (customer == null || customer.getStatus() == -1) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] verify_identity_number " + response.toString());
            return badRequest(1107);
        }
        if (customer.getStatus() == HDConstant.STATUS.DISABLE) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] verify_identity_number " + response.toString());
            return badRequest(1115);
        }
        writeLogAction(req, "customer", "verify identity number and contract code", searchRequest.toString(), "", "", "", "");
        return ok(response);
    }

    /**
     * Statistics number of customer register lending app successfully
     *
     * @return number of customers
     */
    @PostMapping(value = "/statistics/register")
    public ResponseEntity<?> countRegister() {
        Integer count = customerService.countRegister(1);
        return ok(count);
    }

    /**
     * Statistics number of customer register lending app successfully by number of day
     *
     * @param req object StatisticsRegisterByDaysRequest contain number of days for report
     * @return StatisticsRegisterByDaysResponse contain information needed for client
     */
    @PostMapping(value = "/statistics/register_recent_days")
    public ResponseEntity<?> countRegisterRecently(@RequestBody RequestDTO<StatisticsRegisterByDaysRequest> req) {
        StatisticsRegisterByDaysRequest daysRequest = req.init();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        /*LocalDateTime today = LocalDate.now().atTime(23, 59, 59);
        LocalDateTime before = LocalDate.now().plusDays(-daysRequest.getNumOfDays()).atTime(0, 0, 0);*/
        LocalDate today = LocalDate.now();
        LocalDate before = LocalDate.now().plusDays(-daysRequest.getNumOfDays());
        List<StatisticsRegisterByDaysResponse> reports = new ArrayList<>();
        List<StatisticsRegisterByDaysResponse> lst = customerService.statisticsRegisterByDays(daysRequest.getNumOfDays());
        for (LocalDate date = before.plusDays(1); date.isBefore(today.plusDays(1)); date = date.plusDays(1)) {
            String dateReport = formatter.format(date);
            StatisticsRegisterByDaysResponse report = new StatisticsRegisterByDaysResponse(dateReport, 0);
            for (StatisticsRegisterByDaysResponse re : lst) {
                if (dateReport.equals(re.getDate())) {
                    report.setCount(re.getCount());
                    break;
                }
            }
            reports.add(0, report);
        }
        return ok(reports);
    }

    /**
     * Check token given is valid or not
     *
     * @param req object IdPayload contain string value of token given
     * @return http status code
     */
    @PostMapping(value = "/validate_token")
    public ResponseEntity<?> validateCustomerToken(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        String token = (String) payload.getId();
        CustomerToken customerToken = customertokenService.findByToken(token);
        if (customerToken == null || customerToken.getStatus() == HDConstant.STATUS.DISABLE) {
            return unauthorized(1126, token);
        }
        return ok();
    }
    @PostMapping(value = "/getRequireChangePassword")
    public ResponseEntity<?> getRequireChangePassword(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);

        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), HDConstant.STATUS.ENABLE);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] fcm_token " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }
        String s = String.valueOf(customer.getRequireChangePassword());
        return ok(s);
    }

    /**
     * Register by phone
     *
     * @param req object RegisterByPhonePayload contain string phone value of device id value
     * @return phone otp
     */
    @PostMapping(value = "/registerByPhone")
    public ResponseEntity<?> registerByPhone(@RequestBody RequestDTO<RegisterByPhonePayload> req) {

        RegisterByPhonePayload payload = req.init();

        String phone = payload.getPhone();

        RegisterByPhone customerByPhone = new RegisterByPhone();
        customerByPhone.setCreatedAt(new Date());
        customerByPhone.setDeviceId(payload.getDeviceId());
        customerByPhone.setPhone(phone);
        customerByPhone.setStatus(1);
        registerByPhoneService.saveOrUpdate(customerByPhone);

        Customer customer = customerService.findByPhoneNumberAndType(phone, 2);
        String token = "";
        if (customer == null) {
            customer = new Customer();
            customer.setRequireChangePassword(0);
            customer.setCreatedAt(req.now());
            customer.setModifiedAt(req.now());
            customer.setLastModifyPassword(req.now());
            UUID uuid = UUID.randomUUID();
            customer.setUuid(uuid);
            customer.setCreatedBy(uuid);
            customer.setObjectVersion(1);
            customer.setPreferLanguage("vi");
            customer.setUsername(phone);
            customer.setPhoneNumber(phone);
            customer.setPhoneNumberOrigin(phone);
            customer.setStatus(1);
            customer.setRegisterType(Customer.RegisterType.PHONE);
            //insert customer
            customer = customerService.insert(customer);

            //insert customer_device
//            CustomerDeviceRequest device = new CustomerDeviceRequest();
//            device.setFcmToken(payload.getFcmToken());
//            device.setPreferLanguage("vi");
//            device.setUuid(customer.getUuid().toString());
//            customerDeviceService.insert(device);
        } else {
            customer.setModifiedAt(req.now());
            customer.increaseObjectVersion();
            customer.setLastModifyPassword(req.now());
            customerService.update(customer);
        }
        //insert customer_token
        CustomerToken customerToken = initCustomerToken(customer, req.now(), req.environment());
        customertokenService.insert(customerToken);
        token = customerToken.getToken();
        Customer customerClone = null;
        try {
            customerClone = (Customer) customer.clone();
            if (!HDUtil.isNullOrEmpty(customerClone.getPhoneNumber())) {
                customerClone.setPhoneNumber(HDUtil.maskNumber(customerClone.getPhoneNumber(), "*** *** ####"));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ok(new AuthResponse(token, customerClone, ""));
    }

    /**
     * Find list register by phone
     *
     * @param req object RegisterByPhonePayload contain info need to filter data
     * @return list customer and total number of data
     */
    @PostMapping(value = "/listRegisterByPhone")
    public ResponseEntity<?> listRegisterByPhone(@RequestBody RequestDTO<SearchRegisterByPhoneRequest> req) {
        validateToken_Staff(req.jwt());
        SearchRegisterByPhoneRequest searchRequest = req.init();
        List<Customer> customers = customerService.find(searchRequest);
        int count = customerService.count(searchRequest);
        return ok(new CustomerSearchResponse(customers, count));
    }

    /**
     * Write log of customer when use lending app
     *
     * @param req object CustomerLogAction contain information of action
     * @return http status code
     */
    //create customer log action
    @PostMapping(value = "/log_action/create")
    public ResponseEntity<?> createCustomerLogAction(@RequestBody RequestDTO<CustomerLogAction> req) {
        CustomerLogAction customerLogAction = req.init();
        logger.info("input request: " + customerLogAction.toString());
        UUID customerId = customerLogAction.getCustomerId();
        customerLogAction.setPara(customerLogAction.toString());
        customerLogAction.setDevice(req.environment());
        customerLogAction.setCreatedBy(customerId);
        customerLogAction.setCustomerId(customerId);
        String action = customerLogAction.getAction();
        if (action.equals("register_set_fingerprint") || action.equals("register_set_faceid")) {
            joiner = new StringJoiner("\r\n");
            joiner.add("Khách hàng cài đặt đăng nhập bằng sinh trắc học");
            if (customerLogAction.getAction().equals("register_set_fingerprint")) {
                joiner.add("- Hình thức: quét vân tay");
            } else {
                joiner.add("- Hình thức: nhận diện ảnh khuôn mặt");
            }
            customerLogAction.setObjectName("Thiết lập đăng nhập sinh trắc học");
            customerLogAction.setAction(joiner.toString());
            customerLogAction.setType("register");
        }

        if (action.equals("confirm_read_and_agree")) {
            customerLogAction.setObjectName("Xác nhận đồng ý với nội dung Hợp đồng");
            customerLogAction.setAction("Khách hàng chọn ô Tôi đã đọc, hiểu và đồng ý với toàn bộ nội dung nêu trong bộ Hợp đồng tín dụng như trên hiển thị trên màn hình");
            customerLogAction.setType("esign");
        }

        if (action.equals("confirm_sign_contract")) {
            customerLogAction.setObjectName("Xác nhận ký Hợp đồng");
            customerLogAction.setAction("Khách hàng chọn nút Xác nhận hiển thị trên màn hình");
            customerLogAction.setType("esign");
        }

        customerLogActionService.createMQ(customerLogAction);
        return ok("ok");
    }

    /**
     * Find action of customer in log
     *
     * @param req object CustomerLogActionSearch contain information search
     * @return list CustomerLogActionSearch
     */
    //create customer log action
    @PostMapping(value = "/log_action/search")
    public ResponseEntity<?> searchCustomerLogAction(@RequestBody RequestDTO<CustomerLogActionSearch> req) {
        CustomerLogActionSearch customerLogActionSearch = req.init();
        List<CustomerLogActionResponse> list = customerLogActionService.search(customerLogActionSearch);
        return ok(list);
    }

    /**
     * Check status customer's password
     *
     * @param req object IdPayload is primary key of customer request
     * @return object ExpiredPasswordResponse contain information of this customer's password
     */
    @PostMapping(value = "/check_password")
    public ResponseEntity<?> expiredPassword(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload uuidRequest = req.init();
        validateIdPayload(uuidRequest);
        //validate customer exist
        Customer customer = customerService.findByUuid(UUID.fromString(uuidRequest.getId().toString()), HDConstant.STATUS.ENABLE);
        if (customer == null) {
            Log.error("customer", this.getClass().getName() + " [BAD REQUEST] expired_password " + uuidRequest.getId().toString());
            return notFound(1107, "customer not found");
        }
        validateToken_Customer(req.jwt(), customer);
        ExpiredPasswordResponse expiredPasswordResponse = new ExpiredPasswordResponse(0, true);
        if (customer.getLastModifyPassword() == null) {
            expiredPasswordResponse.setWarning(false);
            return ok(expiredPasswordResponse);
        }
        Config config = HDConfig.getInstance();
        LocalDate expiredDate = new Date(customer.getLastModifyPassword().getTime() + Long.valueOf(config.get("PASSWORD_EXPIRED_TIME")) * 1000L)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate now = LocalDate.now();
        if (expiredDate.isBefore(now))
            throw new RequirePasswordException();
        int timesConfig = 15;
        for (int i = 0; i <= timesConfig; i++) {
            expiredPasswordResponse.setDaysLeft(expiredPasswordResponse.getDaysLeft() + 1);
            if (now.plusDays(i).equals(expiredDate)) {
                return ok(expiredPasswordResponse);
            }
        }
        expiredPasswordResponse.setDaysLeft(0);
        expiredPasswordResponse.setWarning(false);
        return ok(expiredPasswordResponse);
    }

    /**
     * Upload new image from local service to s3 bucket AWS
     *
     * @param customerImage information current image needed to upload
     * @param fileNew       file image new need to upload
     * @return uri file s3
     */
    String invokeFileHandlerS3_upload(CustomerImage customerImage, String fileNew) {
        String uri = "";
        if (!HDUtil.isNullOrEmpty(fileNew)) {
            try {
                //create object to request upload s3 server
                FileS3DTORequest s3DTO = new FileS3DTORequest();
                List<FileS3DTORequest.FileReq> lst = new ArrayList<>();
                String b64 = fileLocalStorageService.loadFileAsBase64(fileNew);
                lst.add(new FileS3DTORequest.FileReq(customerImage.getUuid().toString(),
                        String.valueOf(customerImage.getType()),
                        "customer",
                        MimeTypes.lookupMimeType(FilenameUtils.getExtension(fileNew)),
                        b64, ""));
                s3DTO.setFiles(lst);

                //upload file new
                ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/upload", s3DTO,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                    try {
                        FileS3DTOResponse fileResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                                new TypeReference<FileS3DTOResponse>() {
                                });
                        if (fileResponse.getFiles().size() > 0) {
                            uri = fileResponse.getFiles().get(0).getUri();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fileLocalStorageService.deleteFile(fileNew);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException(1125);
            }
        }
        return uri;
    }

    /*boolean validateOtp(NewPasswordRequest newPasswordRequest) {
        SmsOtpDTORequest otpRequest = new SmsOtpDTORequest();
        otpRequest.setCustomerUUID(newPasswordRequest.getCustomerUUID());
        otpRequest.setPhoneNumber(newPasswordRequest.getPhoneNumber());
        otpRequest.setCodeOTP(newPasswordRequest.getCodeOTP());
        otpRequest.setOptType(newPasswordRequest.getOptType());
        try {
            ResponseDTO<Object> dto = invoker.call(urlSMSRequest + "/verify_otp", otpRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                return true;
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }*/

    /**
     * invoke contract service find all contract code by search information
     *
     * @param contractSearchRequest search info
     * @return list string of contract code
     */
    List<String> invokeContact_getAllContractCodeByKeySearch(ContractSearchRequest contractSearchRequest) {
        try {
            ResponseDTO<Object> dto = invoker.call(urlContractRequest + "/getContractCodesByIdentifyIdOrContractCode", contractSearchRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                List<String> ls = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<List<String>>() {
                        });
                if (ls == null)
                    return new ArrayList<>();
                removedDuplicates(ls);
                return ls;
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * invoke contract service find all customer id by list contract code
     *
     * @param ContractCodes list contract code
     * @return list customer uuid
     */
    List<UUID> invokeContract_getCustomerIdByContractCode(List<String> ContractCodes) {

        idPayload = new IdPayload<List<String>>();
        idPayload.setId(ContractCodes);

        try {
            ResponseDTO<Object> dto = invoker.call(urlContractRequest + "/getCustomerIdsByContractCodes", idPayload,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                List<String> ls = mapper.readValue(mapper.writeValueAsString(dto.getPayload()), new TypeReference<List<String>>() {
                });
                if (ls == null)
                    return new ArrayList<>();
                removedDuplicates(ls);
                List<UUID> customerIds = new ArrayList<>();
                ls.forEach(id -> {
                    if (!HDUtil.isNullOrEmpty(id))
                        customerIds.add(UUID.fromString(id));
                });
                return customerIds;
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * invoke contract service find all contract code id by list customer uuid
     *
     * @param customerIds list customer id
     * @return ListContractResponse contain info contract
     */
    List<ListContractResponse> invokeContract_getAllContractCodeByCustomerIds(String customerIds) {
//        System.out.println("invokeContract_getAllContractCodeByCustomerIds:" + customerIds);
        idPayload.setId(customerIds);
        try {
            ResponseDTO<Object> dto = invoker.call(urlContractRequest + "/getAllContactCodeByCustomerIds", idPayload,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                List<ListContractResponse> ls = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<List<ListContractResponse>>() {
                        });
                if (ls == null)
                    throw new InternalServerErrorException();
                return ls;
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Invoke contract service get phone number of contract
     *
     * @param contractSearchRequest info request needed to get phone number
     * @param uri                   contract service
     * @return object VerifyResponse contain result receipted
     */
    VerifyResponse invokeContact_getPhoneNumber(ContractSearchRequest contractSearchRequest, String uri) {
        ResponseDTO<Object> dto = invoker.call(urlContractRequest + "/" + uri, contractSearchRequest,
                new ParameterizedTypeReference<ResponseDTO<Object>>() {
                });
        if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
            try {
                return mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<VerifyResponse>() {
                        });
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (dto != null && dto.getCode() == 1435) {
            throw new BadRequestException(dto.getCode());
        }
        return null;
    }

    /**
     * Remove all duplicate values of list string
     *
     * @param list list of string require remove duplicate
     */
    void removedDuplicates(List<String> list) {
        Set<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
    }

    /**
     * Generate new jwt token for one customer to use lending app
     *
     * @param customer    current customer needed a jwt token
     * @param createdAt   time create jwt token
     * @param environment current portal using lending app
     * @return object CustomerToken contain value of new jwt token created to save database
     */
    CustomerToken initCustomerToken(Customer customer, Date createdAt, String environment) {
        long lastModifyPasswordTime = -1;
        if (customer.getLastModifyPassword() != null)
            lastModifyPasswordTime = HDUtil.getUnixTime(customer.getLastModifyPassword());
        String token = JWTProvider.encode(new JWTPayload(customer.getUuid(),
                HDConstant.ROLE.CUSTOMER,
                HDUtil.getUnixTime(createdAt),
                lastModifyPasswordTime,
                environment));
        CustomerToken customerToken = new CustomerToken();
        customerToken.setCustomerUuid(customer.getUuid());
        customerToken.setToken(token);
        customerToken.setCreateAt(createdAt);
        customerToken.setStatus(HDConstant.STATUS.ENABLE);
        customerToken.setEnvironment(environment);
        return customerToken;
    }

    /**
     * Update new password for customer
     *
     * @param customer    customer needed to update password
     * @param newPassword
     * @param req         contain info request from client
     */
    void updatePassword(Customer customer, String newPassword, RequestDTO req) {
        customer.setPassword(DigestUtils.sha512Hex(newPassword));
        customer.setLastModifyPassword(req.now());
        if (req.jwt() != null)
            customer.setModifiedBy(req.jwt().getUuid());
        else
            customer.setModifiedBy(customer.getUuid());
        customer.setModifiedAt(req.now());
        customer.setRequireChangePassword(HDConstant.STATUS.DISABLE);
        customer.increaseObjectVersion();
        customerService.update(customer);
        customertokenService.disableAllByCustomer(customer.getUuid(), req.now());
    }

    /**
     * Check token of request is mismatch with customer object require
     *
     * @param jwtPayload contain info request from client
     * @param customer   customer needed to valid
     */
    void validateToken_Customer(JWTPayload jwtPayload, Customer customer) {
        if (jwtPayload != null)
            if (jwtPayload.getRole().equals(HDConstant.ROLE.CUSTOMER))
                if (!jwtPayload.getUuid().toString().equals(customer.getUuid().toString()))
                    throw new UnauthorizedException();
    }

    /**
     * Check prevent customer activity
     *
     * @param jwtPayload contain info request from client
     */
    void validateToken_Staff(JWTPayload jwtPayload) {
        if (jwtPayload == null || jwtPayload.getRole().equals(HDConstant.ROLE.CUSTOMER))
            throw new UnauthorizedException();
    }

    /**
     * Check UUID request is valid or not
     *
     * @param payload contain uuid request
     */
    void validateIdPayload(IdPayload payload) {
        try {
            UUID.fromString(payload.getId().toString());
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
    }

    /**
     * When unlock or reset password of customer successfully, this function will send sms contain new password to phone number of customer
     *
     * @param customer           info customer request unlock or reset password
     * @param phoneNumberRequest contain info needed request receipt sms of customer
     * @param req                contain info request of client
     */
    void resetPasswordBySms(Customer customer, PhoneNumberRequest phoneNumberRequest, RequestDTO req) {
        //general new password
        Utils utils = new Utils();
        String newPassword = utils.getRandomPassword(6, 8);

        //update customer
        customer.setPassword(DigestUtils.sha512Hex(newPassword));
        customer.setLastModifyPassword(req.now());
        if (req.jwt() != null)
            customer.setModifiedBy(req.jwt().getUuid());
        else
            customer.setModifiedBy(customer.getUuid());
        customer.setModifiedAt(req.now());
        customer.setRequireChangePassword(HDConstant.STATUS.ENABLE);
        customer.increaseObjectVersion();

        //send sms
        SMSRequest smsRequest = new SMSRequest();
        smsRequest.setLangCode(req.langCode());
        smsRequest.setSmsType(phoneNumberRequest.getType());
        smsRequest.setPhoneNumber(phoneNumberRequest.getPhoneNumber());

        List<String> list = new ArrayList<>();
        list.add(0, customer.getUsername());
        list.add(1, newPassword);
        String[] params = list.toArray(new String[list.size()]);
        smsRequest.setParam(params);
        Log.print(smsRequest.toString());
        try {
            ResponseDTO<Object> dto = invoker.call(urlSMSRequest + "/send", smsRequest,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SEND_SMS, smsRespone);
    }

    /**
     * Write history action of customer or staff in lending app
     *
     * @param req       request from client
     * @param name      name of action
     * @param action    activity of customer of staff
     * @param para      params request
     * @param oldValues values old
     * @param newValues new values needed update
     * @param type      type action
     */
    void writeLogAction(RequestDTO req, String name, String action, String para, String oldValues, String newValues, String type, String uuid) {
//        if (req.jwt() != null) {
//            try {
//                if (req.jwt().getRole().equals(HDConstant.ROLE.CUSTOMER)) {
//                    CustomerLogAction customerLogAction = (CustomerLogAction) HDUtil.writeLogAction(req, name, action, para, oldValues, newValues, type);
//                    if (customerLogAction != null)
//                        customerLogActionService.createMQ(customerLogAction);
//                } else {
//                    StaffLogAction staffLogAction = (StaffLogAction) HDUtil.writeLogAction(req, name, action, para, oldValues, newValues, type);
//                    if (staffLogAction != null)
//                        staffLogActionService.createMQ(staffLogAction);
//                }
//            } catch (Exception e) {
//                e.getMessage();
//            }
//        }
        Logger logger = Logger.getLogger(MainRestController.class.getName());
        try {
            UUID uuidObject = null;
            if (req.jwt() != null) {
                uuidObject = req.jwt().getUuid();
            } else {
                if (uuid.length() > 0) {
                    uuidObject = UUID.fromString(uuid);
                }
            }

            if (req.jwt() != null && req.jwt().getRole().equals(HDConstant.ROLE.STAFF)) {
                StaffLogAction staffLogAction = new StaffLogAction();
                staffLogAction.setObjectName(name);
                staffLogAction.setAction(action);
                staffLogAction.setCreatedAt(req.now());
                staffLogAction.setCreatedBy(uuidObject);
                staffLogAction.setStaffId(uuidObject);
                staffLogAction.setPara(para);
                staffLogAction.setDevice(req.environment());
                staffLogAction.setValueOld(oldValues);
                staffLogAction.setValueNew(newValues);
                staffLogAction.setType(type);
                staffLogActionService.createMQ(staffLogAction);
            } else {
                CustomerLogAction customerLogAction = new CustomerLogAction();
                customerLogAction.setCreatedAt(req.now());
                customerLogAction.setDevice(req.environment());
                customerLogAction.setObjectName(name);
                customerLogAction.setAction(action);
                customerLogAction.setPara(para);
                customerLogAction.setValueOld(oldValues);
                customerLogAction.setValueNew(newValues);
                customerLogAction.setType(type);
                customerLogAction.setCustomerId(uuidObject);
                customerLogAction.setCreatedBy(uuidObject);
                customerLogAction.setContractCode("");
                logger.info("logs output: " + customerLogAction);
                customerLogActionService.createMQ(customerLogAction);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
