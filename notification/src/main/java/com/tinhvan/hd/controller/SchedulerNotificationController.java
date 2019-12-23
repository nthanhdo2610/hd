//package com.tinhvan.hd.controller;
//
//import com.tinhvan.hd.base.*;
//import com.tinhvan.hd.entity.NotificationGroupFilter;
//import com.tinhvan.hd.entity.NotificationGroupQueue;
//import com.tinhvan.hd.service.NotificationGroupFilterService;
//import com.tinhvan.hd.service.NotificationGroupQueueService;
//import com.tinhvan.hd.service.NotificationService;
//import com.tinhvan.hd.vo.CustomerFilterVO;
//import com.tinhvan.hd.vo.NotificationVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//
///**
// * @author tuongnk on 6/28/2019
// * @project notification
// * @controller Notification scheduler
// */
//
//@RestController
//@RequestMapping("/api/v1/notification/cron")
//public class SchedulerNotificationController extends HDController {
//
//    @Autowired
//    private NotificationGroupQueueService notificationGroupQueueService;
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private NotificationGroupFilterService notificationGroupFilterService;
//
//
//    @Value("${app.module.service.url}")
//    private String urlRequest;
//
//    /**
//     * Get list notification queue and sent to rabbit mq
//     *
//     * @params
//     *
//     * @return ResponseEntity
//     *
//     */
//    private static boolean _runSendNotificationMarketing = false;
//    @PostMapping("/scan")
//    public ResponseEntity<?> sendNotification(@RequestBody RequestDTO<EmptyPayload> req) {
//        if (_runSendNotificationMarketing) {
//            return ok(null);
//        }
//        _runSendNotificationMarketing = true;
//        try
//        {
//            //send notification group to queue
//            List<NotificationGroupQueue> groupQueues = notificationGroupQueueService.getListNotificationQueueByStatus(0);
//            for(NotificationGroupQueue queue : groupQueues){
//
//                List<UUID> customers = getListUuidCustomer(queue.getNotificationGroupId());
//
//                NotificationVO vo = new NotificationVO();
//                vo.setCustomerUuids(customers);
//                vo.setParams(queue.getContentPara());
//                vo.setTypeTemplate(queue.getNotificationType());
//
//                // update status notification queue
//                queue.setStatus(1);
//                notificationGroupQueueService.updateNotificationQueue(queue);
//
//                // send to rabbit mq
//                notificationService.sendNotificationToQueue(vo);
//
//            }
//
//        }catch (Exception ex){
//            Log.system("Sent to rabbit mq error :" + ex.getMessage());
//            return serverError(500);
//        }finally {
//            _runSendNotificationMarketing = false;
//        }
//        return ok(null);
//    }
//
//    public List<UUID> getListUuidCustomer(Integer groupId){
//        Invoker invoker = new Invoker();
//        List<UUID> customers = new ArrayList<>();
//        List<NotificationGroupFilter> filters = notificationGroupFilterService.getListFilterByGroupId(groupId);
//        CustomerFilterVO customerFilterVO = new CustomerFilterVO();
//        for(NotificationGroupFilter filter : filters){
//
//            if(filter.getKey().equals("ageFrom")){
//                customerFilterVO.setAgeFrom(Integer.valueOf(filter.getValue()));
//                continue;
//            }
//
//            if(filter.getKey().equals("ageTo")){
//                customerFilterVO.setAgeTo(Integer.valueOf(filter.getValue()));
//                continue;
//            }
//
//            if(filter.getKey().equals("gender")){
//                customerFilterVO.setGender(Short.valueOf(filter.getValue()));
//                continue;
//            }
//
//            if(filter.getKey().equals("province")){
//                customerFilterVO.setProvince(Integer.valueOf(filter.getValue()));
//                continue;
//            }
//
//            if(filter.getKey().equals("district")){
//                customerFilterVO.setDistrict(Integer.valueOf(filter.getValue()));
//                continue;
//            }
//        }
//
//        ResponseDTO results = invoker.call(urlRequest,customerFilterVO,null);
////        if(results.getCode() == 200){
////            List<CustomerVO> customerVOS = results.getPayload();
////            for(CustomerVO vo : customerVOS){
////                customers.add(vo.getUuid());
////            }
////        }
//        return customers;
//    }
//
//    private static boolean _runSendNotificationCronBirthday = false;
//    @PostMapping("/birthday")
//    public ResponseEntity<?> sendNotificationCronBirthday(@RequestBody RequestDTO<EmptyPayload> req) {
//        if (_runSendNotificationCronBirthday) {
//            return ok(null);
//        }
//        _runSendNotificationCronBirthday = true;
//        try
//        {
//
//        }catch (Exception ex){
//            Log.system("Sent to rabbit mq error :" + ex.getMessage());
//            return serverError(500);
//        }finally {
//            _runSendNotificationCronBirthday = false;
//        }
//        return ok(null);
//    }
//}
