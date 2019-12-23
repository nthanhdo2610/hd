//package com.tinhvan.hd.controller;
//
//import com.tinhvan.hd.base.HDController;
//import com.tinhvan.hd.base.InternalServerErrorException;
//import com.tinhvan.hd.base.RequestDTO;
//import com.tinhvan.hd.entity.NotificationFilterCategory;
//import com.tinhvan.hd.service.NotificationFilterCategoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author tuongnk on 6/28/2019
// * @project notification
// * @controller Notification Filter Category
// */
//
//@RestController
//@RequestMapping("/api/v1/filter/category")
//public class NotificationFilterCategoryController extends HDController {
//
//    @Autowired
//    NotificationFilterCategoryService notificationFilterCategoryService;
//
//
//    @PostMapping
//    public ResponseEntity<?> saveNotificationFilterCategory(@RequestBody RequestDTO<NotificationFilterCategory> req){
//        try {
//            NotificationFilterCategory filterCategory = req.getPayload();
//            if(filterCategory != null){
//                notificationFilterCategoryService.saveNotificationFilterCategory(filterCategory);
//            }
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(null);
//    }
//}
