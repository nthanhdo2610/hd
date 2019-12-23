//package com.tinhvan.hd.controller;
//import com.tinhvan.hd.base.HDController;
//import com.tinhvan.hd.base.InternalServerErrorException;
//import com.tinhvan.hd.base.RequestDTO;
//import com.tinhvan.hd.entity.NotificationGroupQueue;
//import com.tinhvan.hd.service.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
///**
// * @author tuongnk on 6/28/2019
// * @project notification
// * @controller Notification Queue
// */
//
//@RestController
//@RequestMapping("/api/v1/notification/group_queue")
//public class NotificationGroupQueueController extends HDController {
//
//
//    @Autowired
//    NotificationGroupQueueService notificationGroupQueueService;
//
//    @PostMapping
//    public ResponseEntity<?> saveNotificationGroupQueue(@RequestBody RequestDTO<NotificationGroupQueue> req){
//        try {
//            NotificationGroupQueue queue = req.getPayload();
//            if(queue != null){
//                notificationGroupQueueService.saveNotificationQueue(queue);
//            }
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(null);
//    }
//}
