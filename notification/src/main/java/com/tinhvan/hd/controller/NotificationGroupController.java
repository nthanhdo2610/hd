//package com.tinhvan.hd.controller;
//
//import com.tinhvan.hd.base.*;
//import com.tinhvan.hd.entity.NotificationGroup;
//import com.tinhvan.hd.service.NotificationGroupService;
//import com.tinhvan.hd.vo.NotificationGroupFilterVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
//
///**
// * @author tuongnk on 6/28/2019
// * @project notification
// * @controller Notification Group
// */
//
//@RestController
//@RequestMapping("/api/v1/notification/group")
//public class NotificationGroupController extends HDController {
//
//    private Map<String, Object> data = new HashMap<String, Object>();
//
//    @Autowired
//    NotificationGroupService notificationGroupService;
//
//    @PostMapping("/list")
//    public  ResponseEntity<?> getAllNotificationGroupActive(@RequestBody RequestDTO<NotificationGroupFilterVO> req){
//        List<NotificationGroup> notificationGroups = new ArrayList<>();
//        try {
//            NotificationGroupFilterVO payload = req.getPayload();
//            notificationGroups = notificationGroupService.getListNotificationGroupModifiedBy(payload);
//
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(notificationGroups);
//    }
//
//    @PostMapping("/num")
//    public  ResponseEntity<?> getAllNotificationGroupModifiedBy(@RequestBody RequestDTO<NotificationGroupFilterVO> req){
//        long total =0;
//        try {
//            NotificationGroupFilterVO payload = req.getPayload();
//
//            total = notificationGroupService.countNotificationGroup(payload);
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(total);
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<?> saveNotificationGroup(@RequestBody RequestDTO<NotificationGroup> req){
//        try {
//            NotificationGroup group = req.getPayload();
//            if(group != null){
//                group.setCreatedAt(new Date());
//                notificationGroupService.saveNotificationGroup(group);
//            }
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(null);
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<?> updateNotificationGroup(@RequestBody RequestDTO<NotificationGroup> req){
//        try {
//            NotificationGroup groupNew = req.getPayload();
//            Integer groupId = groupNew.getId();
//            if(groupId == null || groupId == 0) {
//                throw new BadRequestException(404,"group id is not exits");
//            }
//
//            NotificationGroup groupOld = notificationGroupService.getNotificationGroupById(groupId);
//
//            if(groupOld == null){
//                throw new BadRequestException(404,"NotificationGroup is not exits");
//            }
//
//            groupNew.setModifiedAt(new Date());
//            notificationGroupService.updateNotificationGroup(groupNew);
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(null);
//    }
//
//    @PostMapping("/delete")
//    public ResponseEntity<?> deleteNotificationGroup(@RequestBody RequestDTO<IdPayload> req){
//        try {
//            IdPayload payload = req.getPayload();
//            Integer groupId = (Integer) payload.getId();
//
//            NotificationGroup group = notificationGroupService.getNotificationGroupById(groupId);
//            if (group == null){
//                throw new BadRequestException(404,"NotificationGroup is not exits");
//            }
//            notificationGroupService.deleteNotificationGroup(group);
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(null);
//    }
//
//    @PostMapping("/detail")
//    public ResponseEntity<?> getNotificationGroupById(@RequestBody RequestDTO<IdPayload> req){
//        NotificationGroup group = null;
//        try {
//            IdPayload payload = req.getPayload();
//            Integer groupId = (Integer) payload.getId();
//            group = notificationGroupService.getNotificationGroupById(groupId);
//            if (group == null){
//                throw new BadRequestException(404,"NotificationGroup is not exits");
//            }
//        }catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(group);
//    }
//}
