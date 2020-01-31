package com.tinhvan.hd.promotion.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.file.FileS3DTORequest;
import com.tinhvan.hd.base.file.FileS3DTOResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.promotion.entity.Promotion;
import com.tinhvan.hd.promotion.entity.PromotionCustomer;
import com.tinhvan.hd.promotion.entity.PromotionFilterCustomer;
import com.tinhvan.hd.promotion.file.service.FileStorageService;
import com.tinhvan.hd.promotion.payload.*;
import com.tinhvan.hd.promotion.service.PromotionCustomerService;
import com.tinhvan.hd.promotion.service.PromotionFilterCustomerService;
import com.tinhvan.hd.promotion.service.PromotionService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/api/v1/promotion")
public class PromotionRestController extends HDController {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionCustomerService promotionCustomerService;

    @Autowired
    private PromotionFilterCustomerService promotionFilterCustomerService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/type")
    public ResponseEntity<?> getType(HttpServletRequest req) {
        Map<Integer, String> lst = new HashMap<>();
        lst.put(0, "all");
        lst.put(1, "general");
        lst.put(2, "individual");
        return ok(lst);
    }

    /**
     * Search promotion pagination style
     *
     * @param req object PromotionSearchRequest contain condition search
     * @return object PromotionSearchResponse contain result search
     */
    @PostMapping("/list")
    public ResponseEntity<?> search(@RequestBody RequestDTO<PromotionSearchRequest> req) {
        PromotionSearchRequest searchRequest = req.init();
        /*if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            Log.warn("promotion", this.getClass().getName() + ": [unauthorized] list");
            return unauthorized();
        }*/
        List<Promotion> lst = promotionService.find(searchRequest);
        int total = promotionService.count(searchRequest);
        setTypeName(lst);
        return ok(new PromotionSearchResponse(lst, total));
    }

    /**
     * Find list of promotion is featured
     *
     * @return list promotion
     */
    @PostMapping("/featured")
    public ResponseEntity<?> getPromotionFeatured(HttpServletRequest req) {
        List<Promotion> lst = promotionService.getListFeatured("");
        setTypeName(lst);
        return ok(lst);
    }

    /**
     * Insert one promotion into database
     *
     * @param req contain info of one promotion needed insert
     * @return object Promotion after insert successfully
     */
    @PostMapping("/post")
    @Transactional
    public ResponseEntity<?> insertPromotion(@RequestBody RequestDTO<PromotionRequest> req) {
        //PostRequest postRequest = req.init();
        PromotionRequest promotionRequest = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        if (!HDUtil.isNullOrEmpty(promotionRequest.getPathFilter())
                && !FilenameUtils.getExtension(promotionRequest.getPathFilter()).equals(MimeTypes.lookupExtension(MimeTypes.MIME_APPLICATION_VND_MSEXCEL))
                && !FilenameUtils.getExtension(promotionRequest.getPathFilter()).equals(MimeTypes.lookupExtension(MimeTypes.MIME_APPLICATION_VND_MSEXCEL_2007))) {
            fileStorageService.deleteFile(promotionRequest.getPathFilter());
            return badRequest(1309);
        }
        //post promotion
        //PromotionRequest promotionRequest = postRequest.getpromotion();
        //System.out.println("/post" + promotionRequest.toString());
        Promotion promotion = new Promotion();
        promotion.init(promotionRequest);
        promotion.setId(UUID.randomUUID());
        promotion.setCreatedAt(req.now());
        promotion.setCreatedBy(req.jwt().getUuid());
        promotionService.postPromotion(promotion);
        //updatePromotionFilterCustomer(promotion, postRequest.getFilters(), req.now(), req.jwt().getUuid());
        boolean b = invokeFileHandlerS3_upload(promotion, promotion.getImagePath(), "", Promotion.FILE.IMAGE_PATH, true);
        boolean b1 = invokeFileHandlerS3_upload(promotion, promotion.getImagePathBrief(), "", Promotion.FILE.IMAGE_PATH_BRIEF, true);
        boolean b2 = invokeFileHandlerS3_upload(promotion, promotion.getPathFilter(), "", Promotion.FILE.PATH_FILTER, false);
        List<Promotion> lst = new ArrayList<>();
        lst.add(0, promotion);
        setTypeName(lst);
        if (!b || !b1 || !b2)
            return serverError(1125, lst.get(0));
        checkSendNotification(promotion);
        return ok(lst.get(0));
    }

    /**
     * Update one promotion exist database
     *
     * @param req contain info of one promotion needed update
     * @return object Promotion after update successfully
     */
    @PostMapping("/update")
    @Transactional
    public ResponseEntity<?> updatePromotion(@RequestBody RequestDTO<PromotionRequest> req) {
        //PostRequest postRequest = req.init();
        PromotionRequest promotionRequest = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            Log.system("promotion", this.getClass().getName() + ": [unauthorized] update");
            return unauthorized();
        }
        if (!HDUtil.isNullOrEmpty(promotionRequest.getPathFilter())
                && !FilenameUtils.getExtension(promotionRequest.getPathFilter()).equals(MimeTypes.lookupExtension(MimeTypes.MIME_APPLICATION_VND_MSEXCEL))
                && !FilenameUtils.getExtension(promotionRequest.getPathFilter()).equals(MimeTypes.lookupExtension(MimeTypes.MIME_APPLICATION_VND_MSEXCEL_2007))) {
            fileStorageService.deleteFile(promotionRequest.getPathFilter());
            return badRequest(1309);
        }
        //PromotionRequest promotionRequest = postRequest.getpromotion();
        //System.out.println("/update" + promotionRequest.toString());

        //update promotion
        Promotion promotion = promotionService.findById(UUID.fromString(promotionRequest.getId()));
        if (promotion == null) {
            return badRequest(1117, "promotion is not exits");
        }

        String fileOld = "";
        if (promotion.getImagePath() != null)
            fileOld = promotion.getImagePath();
        String fileOld1 = "";
        if (promotion.getImagePathBrief() != null)
            fileOld1 = promotion.getImagePathBrief();
        String fileOld2 = "";
        if (promotion.getPathFilter() != null)
            fileOld2 = promotion.getPathFilter();
        promotion.init(promotionRequest);
        promotion.setModifiedAt(req.now());
        promotion.setModifiedBy(req.jwt().getUuid());
        promotionService.updatePromotion(promotion);
        //updatePromotionFilterCustomer(promotion, postRequest.getFilters(), req.now(), req.jwt().getUuid());
        boolean b = true;
        if (promotionRequest.getImagePath() != null && !fileOld.equals(promotionRequest.getImagePath())) {
            b = invokeFileHandlerS3_upload(promotion, promotionRequest.getImagePath(), fileOld, Promotion.FILE.IMAGE_PATH, true);
        }
        boolean b1 = true;
        if (promotionRequest.getImagePathBrief() != null && !fileOld1.equals(promotionRequest.getImagePathBrief())) {
            b1 = invokeFileHandlerS3_upload(promotion, promotionRequest.getImagePathBrief(), fileOld1, Promotion.FILE.IMAGE_PATH_BRIEF, true);
        }
        boolean b2 = true;
        if (promotionRequest.getPathFilter() != null && !fileOld2.equals(promotionRequest.getPathFilter())) {
            b2 = invokeFileHandlerS3_upload(promotion, promotionRequest.getPathFilter(), fileOld2, Promotion.FILE.PATH_FILTER, false);
        }
        List<Promotion> lst = new ArrayList<>();
        lst.add(0, promotion);
        setTypeName(lst);
        if (!b || !b1 || !b2)
            return serverError(1125, lst.get(0));
        checkSendNotification(promotion);
        return ok(lst.get(0));
    }

    /**
     * Check status on one promotion
     *
     * @param req contain uuid of promotion
     * @return http status code
     */
    @PostMapping("/checkValid")
    public ResponseEntity<?> checkValid(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        validIdPayload(payload);
        Promotion promotion = promotionService.findById(UUID.fromString(payload.getId().toString()));
        if (promotion == null || promotion.getStatus() != HDConstant.STATUS.ENABLE) {
            return badRequest();
        }
        if (promotion.getEndDate() != null && HDUtil.getUnixTime(promotion.getEndDate()) <= HDUtil.getUnixTimeNow()) {
            return badRequest();
        }
        return ok();
    }

    /**
     * View detail of one promotion
     *
     * @param req contain promotion uuid
     * @return object promotion contain info required
     */
    @PostMapping("/detail")
    public ResponseEntity<?> getPromotionById(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        validIdPayload(payload);

        JWTPayload jwtPayload = req.jwt();

        Promotion promotion = promotionService.findById(UUID.fromString(payload.getId().toString()));
        if (promotion == null || promotion.getStatus() == HDConstant.STATUS.DELETE_FOREVER) {
            return badRequest(1117, "promotion is not exits");
        }
        if (req.jwt() == null || req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            if (promotion.getEndDate().before(req.now()))
                return badRequest(1117, "promotion is not exits");
        }
        PromotionCustomer promotionCustomer = null;

        if (promotion.getAccess() == Promotion.ACCESS.INDIVIDUAL &&
                jwtPayload != null && jwtPayload.getRole().equals(HDConstant.ROLE.CUSTOMER)) {
            promotionCustomer = promotionCustomerService.find(promotion.getId(), req.jwt().getUuid());
            if (promotionCustomer == null) {
                return badRequest(1117, "promotion is not exits");
            }
        }

        // validate token
//        if (jwtPayload != null && jwtPayload.getUuid() != null) {
//            System.out.println("promotion_request_token_uuid:" + jwtPayload.getUuid());
//            if (promotionCustomer != null && promotionCustomer.getCustomerId() != null) {
//                System.out.println("promotion_request_customer_uuid:" + promotionCustomer.getCustomerId());
//                if (!promotionCustomer.getCustomerId().equals(jwtPayload.getUuid())) {
//                    return badRequest(1117, "promotion is not exits");
//                }
//            }
//        }

        //promotion.setFilterCustomers(promotionFilterCustomerService.findList(promotion.getId()));
        List<Promotion> lst = new ArrayList<>();
        lst.add(0, promotion);
        setTypeName(lst);
        return ok(lst.get(0));
    }

    /**
     * View detail one promotion when access is general
     *
     * @param req contain promotion uuid
     * @return object promotion contain info required
     */
    @PostMapping("/detail_general")
    public ResponseEntity<?> detailGeneral(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        validIdPayload(payload);
        Promotion promotion = promotionService.findById(UUID.fromString(payload.getId().toString()));
        if (promotion == null || promotion.getStatus() == HDConstant.STATUS.DELETE_FOREVER || promotion.getAccess() == Promotion.ACCESS.INDIVIDUAL) {
            return badRequest(1117, "promotion is not exits");
        }
        if (req.jwt() == null || req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            if (promotion.getEndDate().before(req.now()))
                return badRequest(1117, "promotion is not exits");
        }
        //promotion.setFilterCustomers(promotionFilterCustomerService.findList(promotion.getId()));
        List<Promotion> lst = new ArrayList<>();
        lst.add(0, promotion);
        setTypeName(lst);
        return ok(lst.get(0));
    }

    /**
     * View detail one promotion when access is individual
     *
     * @param req contain promotion uuid
     * @return object promotion contain info required
     */
    @PostMapping("/individual")
    public ResponseEntity<?> getPromotionByCustomerId(@RequestBody RequestDTO<IndividualPromotionRequest> req) {
        IndividualPromotionRequest request = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER && !req.jwt().getUuid().toString().equals(request.getCustomerUuid())) {
            return unauthorized();
        }
        List<Promotion> lst = promotionService.findIndividual(UUID.fromString(request.getCustomerUuid()));
        setTypeName(lst);
        return ok(lst);
    }

    /**
     * Find list promotion when access is general
     *
     * @return list of promotion
     */
    @PostMapping("/general")
    public ResponseEntity<?> general() {
        List<Promotion> lst = promotionService.findGeneral();
        setTypeName(lst);
        return ok(lst);
    }

    /**
     * Find list promotion at home screen when customer not logged
     *
     * @param req object HomeRequest contain info needed filter
     * @return list promotion result
     */
    @PostMapping("/home")
    public ResponseEntity<?> home(@RequestBody RequestDTO<HomeRequest> req) {
        HomeRequest payload = req.init();
        List<Promotion> lst = promotionService.findHome(payload.getLimit());
        setTypeName(lst);
        return ok(lst);
    }

    /**
     * Find list promotion at home screen when customer was logged
     *
     * @param req object HomeLoggedRequest contain info needed filter
     * @return list promotion result
     */
    @PostMapping("/home_logged")
    public ResponseEntity<?> homeLogged(@RequestBody RequestDTO<HomeLoggedRequest> req) {
        HomeLoggedRequest payload = req.init();
        if (HDUtil.isNullOrEmpty(payload.getCustomerUuid()))
            throw new BadRequestException(1106, "invalid id");
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER && !req.jwt().getUuid().toString().equals(payload.getCustomerUuid())) {
            return unauthorized();
        }
        List<Promotion> lst = promotionService.findHomeLogged(UUID.fromString(payload.getCustomerUuid()), payload.getAccess(), payload.getLimit());
        setTypeName(lst);
        return ok(lst);
    }

    /**
     * Find list promotion at menu screen when customer was logged
     *
     * @param req object MenuRequest contain info needed filter
     * @return object PromotionSearchResponse contain result pagination style
     */
    @PostMapping("/menu")
    public ResponseEntity<?> menu(@RequestBody RequestDTO<MenuRequest> req) {
        MenuRequest payload = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER && !req.jwt().getUuid().toString().equals(payload.getCustomerUuid())) {
            return unauthorized();
        }

        List<Promotion> lst = promotionService.findMenu(payload);
        int total = promotionService.countMenu(payload);
        setTypeName(lst);
        return ok(new PromotionSearchResponse(lst, total));
    }

    /**
     * Update status of one promotion, if current status is enable then update status is disable or opposite
     *
     * @param req contain promotion uuid
     * @return object promotion contain info required
     */
    @PostMapping("/change_status")
    @Transactional
    public ResponseEntity<?> changeStatusPromotionById(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        validIdPayload(payload);
        Promotion promotion = promotionService.findById(UUID.fromString(payload.getId().toString()));
        if (promotion == null) {
            return badRequest(1117, "promotion is not exits");
        }
        if (promotion.getStatus() == HDConstant.STATUS.ENABLE)
            promotion.setStatus(HDConstant.STATUS.DISABLE);
        else
            promotion.setStatus(HDConstant.STATUS.ENABLE);
        promotion.setModifiedAt(req.now());
        promotion.setModifiedBy(req.jwt().getUuid());
        List<Promotion> lst = new ArrayList<>();
        lst.add(0, promotion);
        setTypeName(lst);
        promotionService.updatePromotion(promotion);
        return ok(lst.get(0));
    }

    /**
     * Update status of one promotion to deleted
     *
     * @param req contain promotion uuid
     * @return http status code
     */
    @PostMapping("/delete")
    @Transactional
    public ResponseEntity<?> deletePromotionById(@RequestBody RequestDTO<IdPayload> req) {
        Log.system("promotion", this.getClass().getName() + ": [BEGIN] delete");
        IdPayload payload = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            Log.system("promotion", this.getClass().getName() + ": [unauthorized] delete");
            return unauthorized();
        }
        validIdPayload(payload);
        Promotion promotion = promotionService.findById(UUID.fromString(payload.getId().toString()));
        if (promotion == null) {
            return badRequest(1117, "promotion is not exits");
        }
        promotion.setStatus(HDConstant.STATUS.DELETE_FOREVER);
        promotion.setModifiedAt(req.now());
        promotion.setModifiedBy(req.jwt().getUuid());
        promotionService.updatePromotion(promotion);

        long now = HDUtil.getUnixTimeNow();
        if (promotion.getStartDate() != null && HDUtil.getUnixTime(promotion.getStartDate()) <= now) {
            UuidNotificationRequest request = new UuidNotificationRequest();
            request.setPromotionId(promotion.getId());
            invokeNotification_disableNotificationByPromotionId(request);
        }
        Log.system("promotion", this.getClass().getName() + ": [END] delete");
        return ok(promotion);
    }

    /**
     * Check type IdPayload request is UUID
     *
     * @param payload
     */
    void validIdPayload(IdPayload payload) {
        try {
            UUID.fromString(payload.getId().toString());
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
    }

    /*void updatePromotionFilterCustomer(Promotion promotion, List<FilterCustomerRequest> filters, Date timeUpdate, UUID updateBy) {
        //get list promotion filter customer old
        List<Integer> promotionFilterCustomerIds = new ArrayList<>();
        List<PromotionFilterCustomer> promotionFilterCustomers = promotionFilterCustomerService.findList(promotion.getId());
        if (promotion.getStatusNotification() == Promotion.STATUS_NOTIFICATION.WILL_SEND) {
            if (filters != null && filters.size() > 0) {
                //insert new filter
                filters.forEach(filter -> {
                    if (filter.getId() == 0) {
                        PromotionFilterCustomer promotionFilterCustomer = new PromotionFilterCustomer();
                        promotionFilterCustomer.setPromotionId(promotion.getId());
                        promotionFilterCustomer.setKey(filter.getKey());
                        promotionFilterCustomer.setCompare(filter.getCompare());
                        promotionFilterCustomer.setValue(filter.getValue());
                        promotionFilterCustomer.setCreatedAt(timeUpdate);
                        promotionFilterCustomer.setCreatedBy(updateBy);
                        promotionFilterCustomerService.insert(promotionFilterCustomer);
                    }
                    if (filter.getId() > 0) {
                        promotionFilterCustomers.forEach(promotionFilterCustomer -> {
                            if (promotionFilterCustomer.getId() == filter.getId()) {
                                promotionFilterCustomer.setKey(filter.getKey());
                                promotionFilterCustomer.setCompare(filter.getCompare());
                                promotionFilterCustomer.setValue(filter.getValue());
                                promotionFilterCustomer.setModifiedAt(timeUpdate);
                                promotionFilterCustomer.setModifiedBy(updateBy);
                                promotionFilterCustomerService.update(promotionFilterCustomer);
                                promotionFilterCustomerIds.add(filter.getId());
                            }
                        });
                    }
                });
            }
        }
        if (promotion.getStatusNotification() != Promotion.STATUS_NOTIFICATION.WAS_SEND) {
            promotionFilterCustomers.forEach(promotionFilterCustomer -> {
                if (!promotionFilterCustomerIds.contains(promotionFilterCustomer.getId())) {
                    promotionFilterCustomerService.delete(promotionFilterCustomer.getId());
                }
            });
        }
        long now = HDUtil.getUnixTimeNow();
        if (promotion.getStartDate() != null
                && promotion.getEndDate() != null &&
                promotion.getStatus() == HDConstant.STATUS.ENABLE
                && promotion.getStatusNotification() == Promotion.STATUS_NOTIFICATION.WILL_SEND
                && HDUtil.getUnixTime(promotion.getStartDate()) <= now
                && now <= HDUtil.getUnixTime(promotion.getEndDate())) {
            List<String> logs = new ArrayList<>();
            logs.add(findCustomerAndSendNotification(promotion));
            //write Log
            fileStorageService.writeLog("promotion_" + new Date().getTime() + ".txt", logs);

        }
    }*/

    /**
     * Check promotion is valid begin for send notification or save to table promotion_customer
     *
     * @param promotion
     */
    void checkSendNotification(Promotion promotion) {
        long now = HDUtil.getUnixTimeNow();
        if (promotion.getStatus() == HDConstant.STATUS.ENABLE
                && promotion.getStartDate() != null
                && promotion.getEndDate() != null
                && HDUtil.getUnixTime(promotion.getStartDate()) <= now
                && now <= HDUtil.getUnixTime(promotion.getEndDate())) {
            List<String> logs = new ArrayList<>();
            //logs.add(findCustomerAndSendNotification(promotion));

            //write Log
            //fileStorageService.writeLog("promotion_" + new Date().getTime() + ".txt", logs);

        }
    }

    @Value("${app.module.notification.service.url}")
    private String urlNotificationRequest;

    @Value("${app.module.contract.service.url}")
    private String urlContractRequest;

    @Value("${app.module.filehandler.service.url}")
    private String urlFileHandlerRequest;

    @Value("${app.module.configContractTypeBackground.service.url}")
    private String configContractTypeBackgroundRequest;

    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    @Value("${app.module.customer.service.url}")
    private String urlCustomerRequest;

    private IdPayload idPayload = new IdPayload();
    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();

    @Scheduled(cron = "${scheduled.cron.notification}", zone = "Asia/Bangkok")
    //@Scheduled(cron = "${scheduled.cron.handing_file_filter}", zone = "Asia/Bangkok")
    @Transactional
    void handingFileFilter() throws InternalServerErrorException {
        Executor executor = Executors.newScheduledThreadPool(2);
        CompletionService completionService = new ExecutorCompletionService<>(executor);
        findCustomerAndSendNotification();
        List<Promotion> list = promotionService.findSendNotification();
        System.out.println("handingFileFilter " + list.size());
        if (list != null && list.size() > 0) {
            completionService.submit(() -> {
                for (Promotion promotion : list) {
                    if (promotion.getAccess() == Promotion.ACCESS.INDIVIDUAL) {
                        if (promotion.getStatusNotification() == Promotion.STATUS_NOTIFICATION.WILL_SEND) {
                            promotion.setStatusNotification(Promotion.STATUS_NOTIFICATION.WAS_SEND);
                            promotionService.updatePromotion(promotion);
                        }
                    } else {
                        //send notification general
                        System.out.println("send notification general");
                        NotificationDTO notificationDTO = new NotificationDTO();
                        notificationDTO.setPromotionId(promotion.getId().toString());
                        notificationDTO.setTitle(promotion.getTitle());
                        notificationDTO.setContent(promotion.getNotificationContent());
                        notificationDTO.setAccess(promotion.getAccess());
                        notificationDTO.setEndDate(promotion.getEndDate());
                        notificationDTO.setType(HDConstant.NotificationType.PROMOTION);
                        if (invokeNotification_sendNotificationQueueByPromotionId(notificationDTO, null)) {
                            promotion.setStatusNotification(Promotion.STATUS_NOTIFICATION.WAS_SEND);
                            promotionService.updatePromotion(promotion);
                        }
                    }
                }
                return null;
            });
            for (Promotion promotion : list) {
                if (promotion.getAccess() == Promotion.ACCESS.INDIVIDUAL) {
                    System.out.println("handing file individual");
                    List<PromotionCustomer> promotionCustomers = new ArrayList<>();
                    File fileFilter = invokeFileHandlerS3_download(new UriRequest(promotion.getPathFilter()));
                    if (fileFilter == null) {
                        System.out.println("file " + promotion.getPathFilter() + " is not found");
                        continue;
                    }
                    FileInputStream fileIS;
                    Workbook workbookIn;
                    List<String> contractCodes = new ArrayList<>();
                    try {
                        fileIS = new FileInputStream(fileFilter);
                        workbookIn = new XSSFWorkbook(fileIS);
                        Sheet sheetIn = workbookIn.getSheetAt(0);
                        Iterator<Row> rowIteratorIn = sheetIn.iterator();
                        while (rowIteratorIn.hasNext()) {
                            Row rowIn = rowIteratorIn.next();
                            if (rowIn.getRowNum() >= 5) {
                                //Iterator<Cell> cellIteratorIn = rowIn.iterator();
                                //while (cellIteratorIn.hasNext()) {
                                //Cell cellIn = cellIteratorIn.next();
                                String contractCode = "";
                                Cell cellIn = rowIn.getCell(0);
                                if (cellIn != null)
                                    contractCode = cellIn.getStringCellValue().trim();
                                if (!HDUtil.isNullOrEmpty(contractCode) && !contractCodes.contains(contractCode)) {
                                    //System.out.println(contractCode);
                                    contractCodes.add(contractCode);
                                    PromotionCustomer promotionCustomer = new PromotionCustomer();
                                    promotionCustomer.setPromotionId(promotion.getId());
                                    promotionCustomer.setTitle(promotion.getTitle());
                                    promotionCustomer.setNotificationContent(promotion.getNotificationContent());
                                    promotionCustomer.setAccess(promotion.getAccess());
                                    promotionCustomer.setImagePath(promotion.getImagePath());
                                    promotionCustomer.setEndDate(promotion.getEndDate());
                                    promotionCustomer.setStatusNotification(Promotion.STATUS_NOTIFICATION.NOT_SEND);
                                    if (promotion.getStatusNotification() != Promotion.STATUS_NOTIFICATION.NOT_SEND)
                                        promotionCustomer.setStatusNotification(Promotion.STATUS_NOTIFICATION.WILL_SEND);
                                    promotionCustomer.setContractCode(contractCode);
                                    promotionCustomer.setStatus(0);
                                    promotionCustomers.add(promotionCustomer);
                                    if (promotionCustomers.size() == 1000) {
                                        System.out.println("save " + promotionCustomers.size() + " promotionCustomers");
                                        promotionCustomerService.saveAll(promotionCustomers);
                                        promotionCustomers.clear();
                                        completionService.submit(() -> {
                                            findCustomerAndSendNotification();
                                            return null;
                                        });
                                    }
                                }
                                //}
                            }
                        }
                        workbookIn.close();
                        fileIS.close();
                        System.out.println("save " + promotionCustomers.size() + " promotionCustomers");
                        promotionCustomerService.saveAll(promotionCustomers);
                        promotionCustomers.clear();
                        completionService.submit(() -> {
                            findCustomerAndSendNotification();
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new InternalServerErrorException(e.getMessage());
                    } finally {
                        fileFilter.delete();
                    }
                }
            }
        }
    }

    //@Scheduled(cron = "${scheduled.cron.notification}", zone = "Asia/Bangkok")
    void findCustomerAndSendNotification() throws InternalServerErrorException {
        List<PromotionCustomer> promotionCustomers = promotionCustomerService.findCustomerAndSendNotification();
        List<PromotionCustomer> lstTemp = new ArrayList<>();
        System.out.println("findCustomerAndSendNotification " + promotionCustomers.size());
        if (promotionCustomers != null && promotionCustomers.size() > 0) {
            DataRequestByContractCodes payload = new DataRequestByContractCodes();
            List<CustomerIdsByContractCode> datas = new ArrayList<>();
            promotionCustomers.forEach(promotionCustomer -> {
                CustomerIdsByContractCode data = new CustomerIdsByContractCode();
                data.setIdx(promotionCustomer.getId());
                data.setValue(promotionCustomer.getContractCode());
                datas.add(data);
            });
            payload.setData(datas);
            try {
                ResponseDTO<Object> dto = invoker.call(urlContractRequest + "/getCustomerIdsByContractCodesNews", payload,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                    List<CustomerIdsByContractCode> results = mapper.readValue(mapper.writeValueAsString(dto.getPayload()), new TypeReference<List<CustomerIdsByContractCode>>() {
                    });
                    if (results != null && results.size() > 0) {
                        for (CustomerIdsByContractCode data : results) {
                            //System.out.println("data:" + data.toString());
                            for (PromotionCustomer pc : promotionCustomers) {
                                if (pc.getId() == data.getIdx()) {
                                    if (HDUtil.isNullOrEmpty(data.getValue())) {
                                        pc.setStatus(-1);
                                        lstTemp.add(pc);
                                    } else {
                                        String[] customerUuids = data.getValue().split(",");
                                        for (int i = 0; i < customerUuids.length; i++) {
                                            if (i == 0) {
                                                pc.setCustomerId(UUID.fromString(customerUuids[i]));
                                                lstTemp.add(pc);
                                            } else {
                                                PromotionCustomer promotionCustomer = new PromotionCustomer();
                                                promotionCustomer.setPromotionId(pc.getPromotionId());
                                                promotionCustomer.setTitle(pc.getTitle());
                                                promotionCustomer.setNotificationContent(pc.getNotificationContent());
                                                promotionCustomer.setAccess(pc.getAccess());
                                                promotionCustomer.setImagePath(pc.getImagePath());
                                                promotionCustomer.setStatusNotification(pc.getStatusNotification());
                                                promotionCustomer.setContractCode(pc.getContractCode());
                                                promotionCustomer.setStatus(pc.getStatus());
                                                promotionCustomer.setEndDate(pc.getEndDate());
                                                promotionCustomer.setCustomerId(UUID.fromString(customerUuids[i]));
                                                lstTemp.add(promotionCustomer);
                                            }
                                        }
                                    }
                                    continue;
                                }
                            }
                        }
                        lstTemp.forEach(pc -> {
                            if (pc.getStatusNotification() == Promotion.STATUS_NOTIFICATION.WILL_SEND) {
                                if (pc.getCustomerId() != null) {
                                    if (checkPromotionCustomer(lstTemp, pc)) {
                                        //send notification
                                        NotificationDTO notificationDTO = new NotificationDTO();
                                        notificationDTO.setCustomerUuids(Arrays.asList(pc.getCustomerId()));
                                        notificationDTO.setPromotionId(pc.getPromotionId().toString());
                                        notificationDTO.setTitle(pc.getTitle());
                                        notificationDTO.setContent(pc.getNotificationContent());
                                        notificationDTO.setAccess(pc.getAccess());
                                        notificationDTO.setType(HDConstant.NotificationType.PROMOTION);
                                        notificationDTO.setEndDate(pc.getEndDate());
                                        invokeNotification_sendNotificationQueueByPromotionId(notificationDTO, null);
                                    }
                                    pc.setStatusNotification(Promotion.STATUS_NOTIFICATION.WAS_SEND);
                                    pc.setStatus(1);
                                }
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerErrorException(e.getMessage());
            } finally {
                promotionCustomerService.saveAll(lstTemp);
            }
        }
    }

    boolean checkPromotionCustomer(List<PromotionCustomer> promotionCustomers, PromotionCustomer promotionCustomer) {
        for (PromotionCustomer pc : promotionCustomers) {
            if (pc.getPromotionId().toString().equals(promotionCustomer.getPromotionId().toString())
                    && pc.getCustomerId() != null
                    && pc.getCustomerId().toString().equals(promotionCustomer.getCustomerId().toString())
                    && pc.getStatusNotification() == Promotion.STATUS_NOTIFICATION.WAS_SEND) {
                return false;
            }
        }
        return promotionCustomerService.validateSendNotification(promotionCustomer);
    }

    /**
     * Function auto run clean files in logs folder on promotion service
     */
    @Scheduled(cron = "${scheduled.cron.cleanLog}", zone = "Asia/Bangkok")
    void cleanLog() {
        fileStorageService.cleanLog();
    }

    /**
     * Function auto run find all promotion is valid begin for send notification or save to table promotion_customer
     */

    //@Scheduled(cron = "${scheduled.cron.notification}", zone = "Asia/Bangkok")
    void sendNotification() {
        //System.out.println(new Date() + ":sendNotification");
        List<String> logs = new ArrayList<>();
        List<Promotion> list = promotionService.findSendNotification();
        //System.out.println(list.size());
        if (list != null && list.size() > 0) {
            list.forEach(promotion -> logs.add(findCustomerAndSendNotification(promotion)));
            //write Log
            //fileStorageService.writeLog("promotion_" + new Date().getTime() + ".txt", logs);
        }
    }

    /**
     * Find list of customer needed send notification of promotion to save on database and push notification
     *
     * @param promotion
     * @return log string
     */
    @Transactional
    String findCustomerAndSendNotification(Promotion promotion) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(promotion.getId().toString());
        List<UUID> customerIds = new ArrayList<>();
        List<String> contractCodes = new ArrayList<>();

        File fileFilter = null;
        FileInputStream fileIS = null;
        Workbook workbookIn = null;
        try {

            //get customer id by filters
                /*if (promotion.getAccess() == Promotion.ACCESS.INDIVIDUAL) {
                    List<FilterDTO> filters = new ArrayList<>();
                    List<PromotionFilterCustomer> promotionFilterCustomers = promotionFilterCustomerService.findList(promotion.getId());
                    promotionFilterCustomers.forEach(filterCustomer ->
                            filters.add(new FilterDTO(filterCustomer.getKey(), filterCustomer.getCompare(), filterCustomer.getValue())));

                    //call contact-service get customer uuid by filters
                    //customerIds = invokeContact_getCustomerIdByFilters(new FilterCustomerDTO(filters));
                }*/

            if (promotion.getAccess() == Promotion.ACCESS.INDIVIDUAL) {
                //get file template from s3
                if (HDUtil.isNullOrEmpty(promotion.getPathFilter())) {
                    return (promotion.getId().toString() + ": file filter not found");
                }
                fileFilter = invokeFileHandlerS3_download(new UriRequest(promotion.getPathFilter()));
                if (fileFilter == null) {
                    return (promotion.getId().toString() + ": file filter not found");
                }

                fileIS = new FileInputStream(fileFilter);
                workbookIn = new XSSFWorkbook(fileIS);
                Sheet sheetIn = workbookIn.getSheetAt(0);
                Iterator<Row> rowIteratorIn = sheetIn.iterator();
                while (rowIteratorIn.hasNext()) {
                    Row rowIn = rowIteratorIn.next();
                    if (rowIn.getRowNum() >= 5) {
                        Iterator<Cell> cellIteratorIn = rowIn.iterator();
                        while (cellIteratorIn.hasNext()) {
                            Cell cellIn = cellIteratorIn.next();
                            if (!HDUtil.isNullOrEmpty(cellIn.getStringCellValue()) && !contractCodes.contains(cellIn.getStringCellValue()))
                                contractCodes.add(cellIn.getStringCellValue());
                        }
                    }
                }
                workbookIn.close();
                fileIS.close();
                //call contact-service get customer uuid by filters
                //System.out.println("contractCodes:" + contractCodes);
                if (contractCodes.size() > 0)
                    customerIds = invokeContract_getCustomerIdByContractCode(contractCodes, joiner);
                if (customerIds == null || customerIds.size() == 0)
                    return joiner.toString();

                //insert detail promotionCustomer
                customerIds.forEach(customerId -> {
                    PromotionCustomer promotionCustomer = new PromotionCustomer();
                    promotionCustomer.setPromotionId(promotion.getId());
                    promotionCustomer.setCustomerId(customerId);
                    promotionCustomer.setTitle(promotion.getTitle());
                    promotionCustomer.setImagePath(promotion.getImagePath());
                    promotionCustomerService.insert(promotionCustomer);
                });
            }

            if (promotion.getStatusNotification() == Promotion.STATUS_NOTIFICATION.WILL_SEND) {
                joiner.add(customerIds.toString());
                joiner.add(new Date().toString());
                //System.out.println("customerIds:" + customerIds);
                //send notification
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setCustomerUuids(customerIds);
                notificationDTO.setPromotionId(promotion.getId().toString());
                notificationDTO.setTitle(promotion.getTitle());
                notificationDTO.setContent(promotion.getNotificationContent());
                notificationDTO.setAccess(promotion.getAccess());
                notificationDTO.setType(HDConstant.NotificationType.PROMOTION);
                if (invokeNotification_sendNotificationQueueByPromotionId(notificationDTO, joiner)) {
                    promotion.setStatusNotification(Promotion.STATUS_NOTIFICATION.WAS_SEND);
                    promotionService.updatePromotion(promotion);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            joiner.add("\n" + e.getMessage());
        } finally {
            try {
                if (workbookIn != null)
                    workbookIn.close();
            } catch (IOException e) {
            }
            try {
                if (fileIS != null)
                    fileIS.close();
            } catch (IOException e) {
            }
            if (fileFilter != null)
                fileFilter.delete();
        }
        joiner.add("\n");
        return joiner.toString();
    }

    /**
     * Invoke contract service find list uuid of customer by list contract code
     *
     * @param contractCodes list of contract code
     * @param joiner        object contain string log action
     * @return list uuid of customer
     */
    List<UUID> invokeContract_getCustomerIdByContractCode(List<String> contractCodes, StringJoiner joiner) {
        //System.out.println("invokeContract_getCustomerIdByContractCode:" + contractCodes);
        idPayload = new IdPayload<List<String>>();
        idPayload.setId(contractCodes);

        try {
            ResponseDTO<Object> dto = invoker.call(urlContractRequest + "/getCustomerIdsByContractCodes", idPayload,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            //System.out.println(dto.getCode());
            //System.out.println(dto.getPayload());
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
            joiner.add("can not find customer by file filter " + contractCodes);
        } catch (Exception e) {
            Log.error(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Invoke notification service to send notification
     *
     * @param notificationDTO data transfer object from promotion service to notification service
     * @param joiner          object contain log action
     * @return result send notification is successfully or not
     */
    boolean invokeNotification_sendNotificationQueueByPromotionId(NotificationDTO notificationDTO, StringJoiner joiner) {
        //System.out.println("invokeNotification_sendNotificationQueueByNewsId:" + notificationDTO.getCustomerUuids());
        ResponseDTO<Object> dto = invoker.call(urlNotificationRequest + "/notification_queue", notificationDTO,
                new ParameterizedTypeReference<ResponseDTO<Object>>() {
                });
        //System.out.println(dto.getCode());
        if (dto.getCode() == HttpStatus.OK.value()) {
            return true;
        }
        if (joiner != null)
            joiner.add("error send notification code " + dto.getCode());
        return false;
    }

    /**
     * Invoke file-handler service to upload file
     *
     * @param promotion
     * @param fileNew      local path file
     * @param fileOld      uri s3 of old file
     * @param type         file type
     * @param enablePublic set file is public or not public on s3
     * @return result upload file is successfully or not
     */
    boolean invokeFileHandlerS3_upload(Promotion promotion, String fileNew, String fileOld, int type, boolean enablePublic) {
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
            if (dto != null && dto.getCode() != HttpStatus.OK.value()) {
                if (type == Promotion.FILE.IMAGE_PATH)
                    promotion.setImagePath(fileOld);
                if (type == Promotion.FILE.IMAGE_PATH_BRIEF)
                    promotion.setImagePathBrief(fileOld);
                if (type == Promotion.FILE.PATH_FILTER)
                    promotion.setPathFilter(fileOld);
                promotionService.updatePromotion(promotion);
            }
        }
        if (!HDUtil.isNullOrEmpty(fileNew)) {
            try {
                //create object to request upload s3 server
                FileS3DTORequest s3DTO = new FileS3DTORequest();
                List<FileS3DTORequest.FileReq> lst = new ArrayList<>();
                String b64 = fileStorageService.loadFileAsBase64(fileNew);
                lst.add(new FileS3DTORequest.FileReq(promotion.getId().toString(),
                        promotion.getType(),
                        "promotion",
                        MimeTypes.lookupMimeType(FilenameUtils.getExtension(fileNew)),
                        b64, fileOld, enablePublic));
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
                            if (type == Promotion.FILE.IMAGE_PATH)
                                promotion.setImagePath(fileResponse.getFiles().get(0).getUri());
                            if (type == Promotion.FILE.IMAGE_PATH_BRIEF)
                                promotion.setImagePathBrief(fileResponse.getFiles().get(0).getUri());
                            if (type == Promotion.FILE.PATH_FILTER)
                                promotion.setPathFilter(fileResponse.getFiles().get(0).getUri());
                        } else {
                            if (type == Promotion.FILE.IMAGE_PATH)
                                promotion.setImagePath("");
                            if (type == Promotion.FILE.IMAGE_PATH_BRIEF)
                                promotion.setImagePathBrief("");
                            if (type == Promotion.FILE.PATH_FILTER)
                                promotion.setPathFilter("");
                        }
                    } catch (IOException e) {
                        if (type == Promotion.FILE.IMAGE_PATH)
                            promotion.setImagePath("");
                        if (type == Promotion.FILE.IMAGE_PATH_BRIEF)
                            promotion.setImagePathBrief("");
                        if (type == Promotion.FILE.PATH_FILTER)
                            promotion.setPathFilter("");
                    }
                } else {
                    if (type == Promotion.FILE.IMAGE_PATH)
                        promotion.setImagePath("");
                    if (type == Promotion.FILE.IMAGE_PATH_BRIEF)
                        promotion.setImagePathBrief("");
                    if (type == Promotion.FILE.PATH_FILTER)
                        promotion.setPathFilter("");
                }
                fileStorageService.deleteFile(fileNew);
                promotionService.updatePromotion(promotion);
                if (type == Promotion.FILE.IMAGE_PATH && HDUtil.isNullOrEmpty(promotion.getImagePath())) {
                    return false;
                }
                if (type == Promotion.FILE.IMAGE_PATH_BRIEF && HDUtil.isNullOrEmpty(promotion.getImagePathBrief())) {
                    return false;
                }
                if (type == Promotion.FILE.PATH_FILTER && HDUtil.isNullOrEmpty(promotion.getPathFilter())) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Invoke file-handler service to download a file
     *
     * @param uriRequest contain uri of file s3
     * @return a file after downloaded
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
                        String fileNameIn = "HD_PROMOTION_FILTER_" + UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + "." + FilenameUtils.getExtension(uriRequest.getUri());
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
     * Get list contract type
     *
     * @return list of ConfigContractTypeBackground contain result contract type
     */
    List<ConfigContractTypeBackground> invokeConfigContractType_getListContractType() {
        ResponseDTO<Object> dto = invoker.call(configContractTypeBackgroundRequest + "/list", null, new ParameterizedTypeReference<ResponseDTO<Object>>() {
        });
        if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
            try {
                List<ConfigContractTypeBackground> lst = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<List<ConfigContractTypeBackground>>() {
                        });
                if (lst != null) {
                    lst.forEach(type -> {
                        if (type.getContractType().equals("CL") || type.getContractType().equals("CLO"))
                            type.setContractName("Ưu đãi vay " + type.getContractName().toLowerCase());
                        else
                            type.setContractName("Ưu đãi vay mua " + type.getContractName().toLowerCase());
                    });
                    return lst;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Set contract type for promotion
     *
     * @param lst list of promotion
     */
    void setTypeName(List<Promotion> lst) {
        Date now = new Date();
        List<ConfigContractTypeBackground> types = invokeConfigContractType_getListContractType();
        lst.forEach(promotion -> {
            for (ConfigContractTypeBackground type : types) {
                if (type.getContractType().equals(promotion.getType())) {
                    promotion.setTypeName(type.getContractName());
                }
            }
            if (promotion.getPromotionEndDate() == null || now.before(promotion.getPromotionEndDate())) {
                promotion.setValidRegister(true);
            }
        });
    }

    /**
     * Remove all duplicate values of list string
     *
     * @param list of string
     */
    void removedDuplicates(List<String> list) {
        Set<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
    }

    void invokeNotification_disableNotificationByPromotionId(UuidNotificationRequest request) {
        ResponseDTO<Object> dto = invoker.call(urlNotificationRequest + "/disable", request,
                new ParameterizedTypeReference<ResponseDTO<Object>>() {
                });
        if (dto != null && dto.getCode() == HttpStatus.OK.value()) {

        } else {
            throw new BadRequestException();
        }
    }
}