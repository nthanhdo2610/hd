//package com.tinhvan.hd.controller;
//
//import com.tinhvan.hd.base.HDController;
//import com.tinhvan.hd.base.InternalServerErrorException;
//import com.tinhvan.hd.base.RequestDTO;
//import com.tinhvan.hd.entity.NotificationGroupFilter;
//import com.tinhvan.hd.service.NotificationGroupFilterService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
///**
// * @author tuongnk on 6/28/2019
// * @project notification
// * @controller Notification Group Filter
// */
//
//@RestController
//@RequestMapping("/api/v1/group/filter")
//public class NotificationGroupFilterController extends HDController {
//
//    @Autowired
//    NotificationGroupFilterService notificationGroupFilterService;
//
//
//    @PostMapping
//    public ResponseEntity<?> saveNotificationGroupFilter(@RequestBody RequestDTO<NotificationGroupFilter> req){
//        try {
//            NotificationGroupFilter groupFilter = req.getPayload();
//            if(groupFilter != null){
//                notificationGroupFilterService.saveNotificationGroupFilter(groupFilter);
//            }
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(null);
//    }
//}
