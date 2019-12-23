package com.tinhvan.hd.news.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.file.FileS3DTORequest;
import com.tinhvan.hd.base.file.FileS3DTOResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.news.entity.News;
import com.tinhvan.hd.news.entity.NewsCustomer;
import com.tinhvan.hd.news.entity.NewsFilterCustomer;
import com.tinhvan.hd.news.file.service.FileStorageService;
import com.tinhvan.hd.news.payload.*;
import com.tinhvan.hd.news.service.NewsCustomerService;
import com.tinhvan.hd.news.service.NewsFilterCustomerService;
import com.tinhvan.hd.news.service.NewsService;
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
import java.util.*;

@RestController
@RequestMapping("/api/v1/news")
public class NewsRestController extends HDController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsCustomerService newsCustomerService;

    @Autowired
    private NewsFilterCustomerService newsFilterCustomerService;

    @Autowired
    FileStorageService fileStorageService;

    /*@PostMapping("/test")
    public ResponseEntity<?> test() {
        return ok(newsService.findSendNotification());
    }*/

    @PostMapping("/type")
    public ResponseEntity<?> getType(HttpServletRequest req) {
        Map<Integer, String> lst = new HashMap<>();
        lst.put(0, "all");
        lst.put(1, "general");
        lst.put(2, "individual");
        return ok(lst);
    }

    /**
     * Search news pagination style
     *
     * @param req contain condition search
     * @return object NewsSearchResponse contain result search
     */
    @PostMapping("/list")
    public ResponseEntity<?> search(@RequestBody RequestDTO<NewsSearchRequest> req) {
        NewsSearchRequest searchRequest = req.init();
        /*if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            Log.warn("news", this.getClass().getName() + ": [unauthorized] list");
            return unauthorized();
        }*/
        List<News> news = newsService.find(searchRequest);
        int total = newsService.count(searchRequest);
        return ok(new NewsSearchResponse(news, total));
    }

    /**
     * Find list of news is featured
     *
     * @return list news
     */
    @PostMapping("/featured")
    public ResponseEntity<?> getNewsFeatured() {
        List<News> news = newsService.getListFeatured(0);
        return ok(news);
    }

    /**
     * Insert one news into database
     *
     * @param req contain info of one news needed insert
     * @return object News after insert successfully
     */
    @PostMapping("/post")
    @Transactional
    public ResponseEntity<?> insertNews(@RequestBody RequestDTO<NewsRequest> req) {
        //PostRequest postRequest = req.init();
        NewsRequest newsRequest = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        if (!HDUtil.isNullOrEmpty(newsRequest.getPathFilter())
                && !FilenameUtils.getExtension(newsRequest.getPathFilter()).equals(MimeTypes.lookupExtension(MimeTypes.MIME_APPLICATION_VND_MSEXCEL))
                && !FilenameUtils.getExtension(newsRequest.getPathFilter()).equals(MimeTypes.lookupExtension(MimeTypes.MIME_APPLICATION_VND_MSEXCEL_2007))) {
            fileStorageService.deleteFile(newsRequest.getPathFilter());
            return badRequest(1309);
        }
        //post news
        //NewsRequest newsRequest = postRequest.getNews();
        //System.out.println("/post" + newsRequest.toString());
        News news = new News();
        news.init(newsRequest);
        news.setId(UUID.randomUUID());
        news.setCreatedAt(req.now());
        news.setCreatedBy(req.jwt().getUuid());
        newsService.postNews(news);
        //updateNewsFilterCustomer(news, postRequest.getFilters(), req.now(), req.jwt().getUuid());
        boolean b = invokeFileHandlerS3_upload(news, news.getImagePath(), "", News.FILE.IMAGE_PATH, true);
        boolean b1 = invokeFileHandlerS3_upload(news, news.getImagePathBrief(), "", News.FILE.IMAGE_PATH_BRIEF, true);
        boolean b2 = invokeFileHandlerS3_upload(news, news.getPathFilter(), "", News.FILE.PATH_FILTER, false);
        if (!b || !b1 || !b2)
            return serverError(1125, news);
        checkSendNotification(news);
        return ok(news);
    }

    /**
     * Update one news exist database
     *
     * @param req contain info of one news needed update
     * @return object News after update successfully
     */
    @PostMapping("/update")
    @Transactional
    public ResponseEntity<?> updateNews(@RequestBody RequestDTO<NewsRequest> req) {
        //PostRequest postRequest = req.init();
        NewsRequest newsRequest = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        if (!HDUtil.isNullOrEmpty(newsRequest.getPathFilter())
                && !FilenameUtils.getExtension(newsRequest.getPathFilter()).equals(MimeTypes.lookupExtension(MimeTypes.MIME_APPLICATION_VND_MSEXCEL))
                && !FilenameUtils.getExtension(newsRequest.getPathFilter()).equals(MimeTypes.lookupExtension(MimeTypes.MIME_APPLICATION_VND_MSEXCEL_2007))) {
            fileStorageService.deleteFile(newsRequest.getPathFilter());
            return badRequest(1309);
        }
        //NewsRequest newsRequest = postRequest.getNews();
        //System.out.println("/update" + newsRequest.toString());

        //update news
        News news = newsService.findById(UUID.fromString(newsRequest.getId()));
        if (news == null) {
            return badRequest(1306, "News is not exits");
        }
        String fileOld = "";
        if (news.getImagePath() != null)
            fileOld = news.getImagePath();
        String fileOld1 = "";
        if (news.getImagePathBrief() != null)
            fileOld1 = news.getImagePathBrief();
        String fileOld2 = "";
        if (news.getPathFilter() != null)
            fileOld2 = news.getPathFilter();

        news.init(newsRequest);
        news.setModifiedAt(req.now());
        news.setModifiedBy(req.jwt().getUuid());
        newsService.updateNews(news);
        //updateNewsFilterCustomer(news, postRequest.getFilters(), req.now(), req.jwt().getUuid());

        boolean b = true;
        if (newsRequest.getImagePath() != null && !fileOld.equals(newsRequest.getImagePath())) {
            b = invokeFileHandlerS3_upload(news, newsRequest.getImagePath(), fileOld, News.FILE.IMAGE_PATH, true);
        }
        boolean b1 = true;
        if (newsRequest.getImagePathBrief() != null && !fileOld1.equals(newsRequest.getImagePathBrief())) {
            b1 = invokeFileHandlerS3_upload(news, newsRequest.getImagePathBrief(), fileOld1, News.FILE.IMAGE_PATH_BRIEF, true);
        }
        boolean b2 = true;
        if (newsRequest.getPathFilter() != null && !fileOld2.equals(newsRequest.getPathFilter())) {
            b2 = invokeFileHandlerS3_upload(news, newsRequest.getPathFilter(), fileOld2, News.FILE.PATH_FILTER, false);
        }
        if (!b || !b1 || !b2)
            return serverError(1125, news);
        checkSendNotification(news);

        return ok(news);
    }

    /**
     * Check status on one news
     *
     * @param req contain uuid of news
     * @return http status code
     */
    @PostMapping("/checkValid")
    public ResponseEntity<?> checkValid(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        validIdPayload(payload);
        News news = newsService.findById(UUID.fromString(payload.getId().toString()));
        if (news == null || news.getStatus() != HDConstant.STATUS.ENABLE) {
            return badRequest();
        }
        if (news.getEndDate() != null && HDUtil.getUnixTime(news.getEndDate()) <= HDUtil.getUnixTimeNow()) {
            return badRequest();
        }
        return ok();
    }

    /**
     * View detail of one news
     *
     * @param req contain news uuid
     * @return object news contain info required
     */
    @PostMapping("/detail")
    public ResponseEntity<?> getNewsById(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        validIdPayload(payload);
        News news = newsService.findById(UUID.fromString(payload.getId().toString()));
        if (news == null || news.getStatus() == HDConstant.STATUS.DELETE_FOREVER) {
            return badRequest(1306, "News is not exits");
        }
        if (news.getAccess() == News.ACCESS.INDIVIDUAL && req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            if (newsCustomerService.find(news.getId(), req.jwt().getUuid()) == null) {
                return badRequest(1117, "promotion is not exits");
            }
        }
        //news.setFilterCustomers(newsFilterCustomerService.findList(news.getId()));
        return ok(news);
    }

    /**
     * Update status of one news, if current status is enable then update status is disable or opposite
     *
     * @param req contain news uuid
     * @return object news contain info required
     */
    @PostMapping("/change_status")
    @Transactional
    public ResponseEntity<?> changeStatusNewsById(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        validIdPayload(payload);
        News news = newsService.findById(UUID.fromString(payload.getId().toString()));
        if (news == null) {
            return badRequest(1306, "News is not exits");
        }
        if (news.getStatus() == HDConstant.STATUS.ENABLE)
            news.setStatus(HDConstant.STATUS.DISABLE);
        else
            news.setStatus(HDConstant.STATUS.ENABLE);
        news.setModifiedAt(req.now());
        news.setModifiedBy(req.jwt().getUuid());
        newsService.updateNews(news);
        return ok(news);
    }

    /**
     * Update status of one news to deleted
     *
     * @param req contain news uuid
     * @return http status code
     */
    @PostMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteNewsById(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        validIdPayload(payload);
        News news = newsService.findById(UUID.fromString(payload.getId().toString()));
        if (news == null) {
            return badRequest(1306, "News is not exits");
        }
        news.setStatus(HDConstant.STATUS.DELETE_FOREVER);
        news.setModifiedAt(req.now());
        news.setModifiedBy(req.jwt().getUuid());
        newsService.updateNews(news);
        return ok();
    }

    /**
     * View detail one news when access is general
     *
     * @param req contain news uuid
     * @return object news contain info required
     */
    @PostMapping("/detail_general")
    public ResponseEntity<?> detailGeneral(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.init();
        validIdPayload(payload);
        News news = newsService.findById(UUID.fromString(payload.getId().toString()));
        if (news == null || news.getStatus() == HDConstant.STATUS.DELETE_FOREVER || news.getAccess() == News.ACCESS.INDIVIDUAL) {
            return badRequest(1117, "News is not exits");
        }
        //news.setFilterCustomers(newsFilterCustomerService.findList(news.getId()));
        return ok(news);
    }

    /**
     * View detail one news when access is individual
     *
     * @param req contain news uuid
     * @return object news contain info required
     */
    @PostMapping("/individual")
    public ResponseEntity<?> getNewsByCustomerId(@RequestBody RequestDTO<IndividualNewsRequest> req) {
        IndividualNewsRequest request = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER && !req.jwt().getUuid().toString().equals(request.getCustomerUuid())) {
            return unauthorized();
        }
        List<News> lst = newsService.findIndividual(UUID.fromString(request.getCustomerUuid()));
        return ok(lst);
    }

    /**
     * Find list news when access is general
     *
     * @return list of news
     */
    @PostMapping("/general")
    public ResponseEntity<?> general() {
        List<News> lst = newsService.findGeneral();
        return ok(lst);
    }

    /**
     * Find list news at home screen when customer not logged
     *
     * @param req object HomeRequest contain info needed filter
     * @return list news result
     */
    @PostMapping("/home")
    public ResponseEntity<?> home(@RequestBody RequestDTO<HomeRequest> req) {
        HomeRequest payload = req.init();
        List<News> lst = new ArrayList<>();
        if (payload.getType() <= 0) {
            lst.addAll(newsService.findHome(1, payload.getLimit()));
            lst.addAll(newsService.findHome(2, payload.getLimit()));
        }
        if (payload.getType() == 1)
            lst = newsService.findHome(1, payload.getLimit());
        if (payload.getType() > 1)
            lst = newsService.findHome(2, payload.getLimit());

        return ok(lst);
    }

    /**
     * Find list news at home screen when customer was logged
     *
     * @param req object HomeLoggedRequest contain info needed filter
     * @return list news result
     */
    @PostMapping("/home_logged")
    public ResponseEntity<?> homeLogged(@RequestBody RequestDTO<HomeLoggedRequest> req) {
        HomeLoggedRequest payload = req.init();
        if (HDUtil.isNullOrEmpty(payload.getCustomerUuid()))
            throw new BadRequestException(1106, "invalid id");
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER && !req.jwt().getUuid().toString().equals(payload.getCustomerUuid())) {
            return unauthorized();
        }
        List<News> lst = new ArrayList<>();
        if (payload.getType() <= 0) {
            lst.addAll(newsService.findHomeLogged(UUID.fromString(payload.getCustomerUuid()), payload.getAccess(), 1, payload.getLimit()));
            lst.addAll(newsService.findHomeLogged(UUID.fromString(payload.getCustomerUuid()), payload.getAccess(), 2, payload.getLimit()));
        }
        if (payload.getType() == 1)
            lst = newsService.findHomeLogged(UUID.fromString(payload.getCustomerUuid()), payload.getAccess(), 1, payload.getLimit());
        if (payload.getType() > 1)
            lst = newsService.findHomeLogged(UUID.fromString(payload.getCustomerUuid()), payload.getAccess(), 2, payload.getLimit());

        return ok(lst);
    }

    /**
     * Find list news at menu screen when customer was logged
     *
     * @param req object MenuRequest contain info needed filter
     * @return object NewsSearchResponse contain result pagination style
     */
    @PostMapping("/menu")
    public ResponseEntity<?> menu(@RequestBody RequestDTO<MenuRequest> req) {
        MenuRequest payload = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER && !req.jwt().getUuid().toString().equals(payload.getCustomerUuid())) {
            return unauthorized();
        }
        List<News> lst = newsService.findMenu(payload);
        int total = newsService.countMenu(payload);
        return ok(new NewsSearchResponse(lst, total));
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

    /*void updateNewsFilterCustomer(News news, List<FilterCustomerRequest> filters, Date timeUpdate, UUID updateBy) {
        //get list news filter customer old
        List<Integer> newsFilterCustomerIds = new ArrayList<>();
        List<NewsFilterCustomer> newsFilterCustomers = newsFilterCustomerService.findList(news.getId());
        if (news.getStatusNotification() == News.STATUS_NOTIFICATION.WILL_SEND) {
            if (filters != null && filters.size() > 0) {
                //insert new filter
                filters.forEach(filter -> {
                    if (filter.getId() == 0) {
                        NewsFilterCustomer newsFilterCustomer = new NewsFilterCustomer();
                        newsFilterCustomer.setNewsId(news.getId());
                        newsFilterCustomer.setKey(filter.getKey());
                        newsFilterCustomer.setCompare(filter.getCompare());
                        newsFilterCustomer.setValue(filter.getValue());
                        newsFilterCustomer.setCreatedAt(timeUpdate);
                        newsFilterCustomer.setCreatedBy(updateBy);
                        newsFilterCustomerService.insert(newsFilterCustomer);
                    }
                    if (filter.getId() > 0) {
                        newsFilterCustomers.forEach(newsFilterCustomer -> {
                            if (newsFilterCustomer.getId() == filter.getId()) {
                                newsFilterCustomer.setKey(filter.getKey());
                                newsFilterCustomer.setCompare(filter.getCompare());
                                newsFilterCustomer.setValue(filter.getValue());
                                newsFilterCustomer.setModifiedAt(timeUpdate);
                                newsFilterCustomer.setModifiedBy(updateBy);
                                newsFilterCustomerService.update(newsFilterCustomer);
                                newsFilterCustomerIds.add(filter.getId());
                            }
                        });
                    }
                });
            }
        }
        if (news.getStatusNotification() != News.STATUS_NOTIFICATION.WAS_SEND) {
            newsFilterCustomers.forEach(newsFilterCustomer -> {
                if (!newsFilterCustomerIds.contains(newsFilterCustomer.getId())) {
                    newsFilterCustomerService.delete(newsFilterCustomer.getId());
                }
            });
        }
        long now = HDUtil.getUnixTimeNow();
        if (news.getStatus() == HDConstant.STATUS.ENABLE
                && news.getStatusNotification() == News.STATUS_NOTIFICATION.WILL_SEND
                && HDUtil.getUnixTime(news.getStartDate()) <= now
                && now <= HDUtil.getUnixTime(news.getEndDate())) {
            List<String> logs = new ArrayList<>();
            logs.add(findCustomerAndSendNotification(news));
            //write Log
            fileStorageService.writeLog("news_" + new Date().getTime() + ".txt", logs);
        }
    }*/

    /**
     * Check news is valid begin for send notification or save to table news_customer
     *
     * @param news
     */
    void checkSendNotification(News news) {
        long now = HDUtil.getUnixTimeNow();
        if (news.getStatus() == HDConstant.STATUS.ENABLE
                && news.getStartDate() != null
                && news.getEndDate() != null
                && HDUtil.getUnixTime(news.getStartDate()) <= now
                && now <= HDUtil.getUnixTime(news.getEndDate())) {
            List<String> logs = new ArrayList<>();
            logs.add(findCustomerAndSendNotification(news));
            //write Log
            //fileStorageService.writeLog("news_" + new Date().getTime() + ".txt", logs);
        }
    }

    @Value("${app.module.notification.service.url}")
    private String urlNotificationRequest;

    @Value("${app.module.contract.service.url}")
    private String urlContractRequest;

    @Value("${app.module.filehandler.service.url}")
    private String urlFileHandlerRequest;

    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    @Value("${app.module.customer.service.url}")
    private String urlCustomerRequest;

    private IdPayload idPayload = new IdPayload();
    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();

    /**
     * Function auto run find all news is valid begin for send notification or save to table news_customer
     */
    @Scheduled(cron = "${scheduled.cron.notification}", zone = "Asia/Bangkok")
    void sendNotification() {
        //System.out.println(new Date() + ":sendNotification");
        List<String> logs = new ArrayList<>();
        List<News> list = newsService.findSendNotification();
        //System.out.println(list.size());
        if (list != null && list.size() > 0) {
            list.forEach(news -> logs.add(findCustomerAndSendNotification(news)));
            //write Log
            //fileStorageService.writeLog("news_" + new Date().getTime() + ".txt", logs);
        }
    }

    /**
     * Function auto run clean files in logs folder on news service
     */
    @Scheduled(cron = "${scheduled.cron.cleanLog}", zone = "Asia/Bangkok")
    void cleanLog() {
        fileStorageService.cleanLog();
    }

    /**
     * Find list of customer needed send notification of news to save on database and push notification
     *
     * @param news
     * @return log string
     */
    String findCustomerAndSendNotification(News news) {

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(news.getId().toString());
        List<UUID> customerIds = new ArrayList<>();
        List<String> contractCodes = new ArrayList<>();

        File fileFilter = null;
        FileInputStream fileIS = null;
        Workbook workbookIn = null;
        try {
            //get customer id by filters
                /*if (news.getAccess() == News.ACCESS.INDIVIDUAL) {
                    List<FilterDTO> filters = new ArrayList<>();
                    List<NewsFilterCustomer> newsFilterCustomers = newsFilterCustomerService.findList(news.getId());
                    newsFilterCustomers.forEach(filterCustomer ->
                            filters.add(new FilterDTO(filterCustomer.getKey(), filterCustomer.getCompare(), filterCustomer.getValue())));

                    //call contact-service get customer uuid by filters
                    //customerIds = invokeContact_getCustomerIdByFilters(new FilterCustomerDTO(filters));

                }*/
            if (news.getAccess() == News.ACCESS.INDIVIDUAL) {
                //get file template from s3
                if (HDUtil.isNullOrEmpty(news.getPathFilter())) {
                    return (news.getId().toString() + ": file filter not found");
                }
                fileFilter = invokeFileHandlerS3_download(new UriRequest(news.getPathFilter()));
                if (fileFilter == null) {
                    return (news.getId().toString() + ": file filter not found");
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
                            if (!HDUtil.isNullOrEmpty(cellIn.getStringCellValue()))
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

                //insert detail NewsCustomer
                customerIds.forEach(customerId -> {
                    NewsCustomer newsCustomer = new NewsCustomer();
                    newsCustomer.setNewsId(news.getId());
                    newsCustomer.setCustomerId(customerId);
                    newsCustomer.setTitle(news.getTitle());
                    newsCustomer.setImagePath(news.getImagePath());
                    newsCustomerService.insert(newsCustomer);
                });
            }

            if (news.getStatusNotification() == News.STATUS_NOTIFICATION.WILL_SEND) {
                joiner.add(customerIds.toString());
                joiner.add(new Date().toString());
                //System.out.println("customerIds:" + customerIds);
                //send notification
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setCustomerUuids(customerIds);
                notificationDTO.setNewsId(news.getId().toString());
                notificationDTO.setTitle(news.getTitle());
                notificationDTO.setContent(news.getNotificationContent());
                notificationDTO.setAccess(news.getAccess());
                if (invokeNotification_sendNotificationQueueByNewsId(notificationDTO, joiner)) {
                    news.setStatusNotification(News.STATUS_NOTIFICATION.WAS_SEND);
                    newsService.updateNews(news);
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
     * @param notificationDTO data transfer object from new service to notification service
     * @param joiner          object contain log action
     * @return result send notification is successfully or not
     */
    boolean invokeNotification_sendNotificationQueueByNewsId(NotificationDTO notificationDTO, StringJoiner joiner) {
        //System.out.println("invokeNotification_sendNotificationQueueByNewsId:" + notificationDTO.getCustomerUuids());
        ResponseDTO<Object> dto = invoker.call(urlNotificationRequest + "/notification_queue", notificationDTO,
                new ParameterizedTypeReference<ResponseDTO<Object>>() {
                });
        //System.out.println(dto.getCode());
        if (dto.getCode() == HttpStatus.OK.value()) {
            return true;
        }
        joiner.add("error send notification code " + dto.getCode());
        return false;
    }

    /**
     * Invoke file-handler service to upload file
     *
     * @param news
     * @param fileNew      local path file
     * @param fileOld      uri s3 of old file
     * @param type         file type
     * @param enablePublic set file is public or not public on s3
     * @return result upload file is successfully or not
     */
    boolean invokeFileHandlerS3_upload(News news, String fileNew, String fileOld, int type, boolean enablePublic) {
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
                if (type == News.FILE.IMAGE_PATH)
                    news.setImagePath(fileOld);
                if (type == News.FILE.IMAGE_PATH_BRIEF)
                    news.setImagePathBrief(fileOld);
                if (type == News.FILE.PATH_FILTER)
                    news.setPathFilter(fileOld);
                newsService.updateNews(news);
            }
        }
        if (!HDUtil.isNullOrEmpty(fileNew)) {
            try {
                //create object to request upload s3 server
                FileS3DTORequest s3DTO = new FileS3DTORequest();
                List<FileS3DTORequest.FileReq> lst = new ArrayList<>();
                String b64 = fileStorageService.loadFileAsBase64(fileNew);
                lst.add(new FileS3DTORequest.FileReq(news.getId().toString(),
                        news.getType().toString(),
                        "news",
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
                            if (type == News.FILE.IMAGE_PATH)
                                news.setImagePath(fileResponse.getFiles().get(0).getUri());
                            if (type == News.FILE.IMAGE_PATH_BRIEF)
                                news.setImagePathBrief(fileResponse.getFiles().get(0).getUri());
                            if (type == News.FILE.PATH_FILTER)
                                news.setPathFilter(fileResponse.getFiles().get(0).getUri());
                        } else {
                            if (type == News.FILE.IMAGE_PATH)
                                news.setImagePath("");
                            if (type == News.FILE.IMAGE_PATH_BRIEF)
                                news.setImagePathBrief("");
                            if (type == News.FILE.PATH_FILTER)
                                news.setPathFilter("");
                        }
                    } catch (IOException e) {
                        if (type == News.FILE.IMAGE_PATH)
                            news.setImagePath("");
                        if (type == News.FILE.IMAGE_PATH_BRIEF)
                            news.setImagePathBrief("");
                        if (type == News.FILE.PATH_FILTER)
                            news.setPathFilter("");
                    }
                } else {
                    if (type == News.FILE.IMAGE_PATH)
                        news.setImagePath("");
                    if (type == News.FILE.IMAGE_PATH_BRIEF)
                        news.setImagePathBrief("");
                    if (type == News.FILE.PATH_FILTER)
                        news.setPathFilter("");
                }
                fileStorageService.deleteFile(fileNew);
                newsService.updateNews(news);
                if (type == News.FILE.IMAGE_PATH && HDUtil.isNullOrEmpty(news.getImagePath())) {
                    return false;
                }
                if (type == News.FILE.IMAGE_PATH_BRIEF && HDUtil.isNullOrEmpty(news.getImagePathBrief())) {
                    return false;
                }
                if (type == News.FILE.PATH_FILTER && HDUtil.isNullOrEmpty(news.getPathFilter())) {
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
                        String fileNameIn = "HD_NEWS_FILTER_" + UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + "." + FilenameUtils.getExtension(uriRequest.getUri());
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
     * Remove all duplicate values of list string
     *
     * @param list of string
     */
    void removedDuplicates(List<String> list) {
        Set<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
    }
}