package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.*;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationAction;
import com.tinhvan.hd.payload.UuidNotificationRequest;
import com.tinhvan.hd.service.NotificationActionService;
import com.tinhvan.hd.service.NotificationService;
import com.tinhvan.hd.service.NotificationTemplatService;
import com.tinhvan.hd.service.PushNotificationsService;
import com.tinhvan.hd.payload.NotificationSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * @author tuongnk on 6/28/2019
 * @project notification
 * @controller Notification
 */
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController extends HDController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationActionService notificationActionService;

    @Autowired
    NotificationTemplatService notificationTemplatService;

    @Autowired
    PushNotificationsService pushNotificationsService;

    /**
     * Find list Notification by filter request
     *
     * @param req NotificationSearchRequest contain request info
     * @return list of Notification
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllNotification(@RequestBody RequestDTO<NotificationSearchRequest> req) {

        NotificationSearchRequest search = req.init();

        List<Notification> notifications = notificationService.getListNotificationByCustomerUuidAndType(search);

        //int total = notificationService.countNotification(search);

        return ok(notifications);
    }

    @PostMapping("/disable")
    public ResponseEntity<?> disable(@RequestBody RequestDTO<UuidNotificationRequest> req) {

        UuidNotificationRequest request = req.init();

        List<Notification> notifications = notificationService.findByUuid(request);
        if (!notifications.isEmpty()) {
            notifications.forEach(no -> no.setStatus(HDConstant.STATUS.DISABLE));
        }
        notificationService.saveAll(notifications);
        return ok();
    }

    /**
     * View detail content one notification
     *
     * @param req IdPayload contain id of notifiaction
     * @return Notification detail
     */
    @PostMapping("/detail")
    public ResponseEntity<?> getNotificationTemplateById(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload payload = req.init();
        Notification notification = validateNotification(payload);
        if (notification == null) {
            return badRequest(1220, "Notification is not exits");
        }
        return ok(notification);
    }

    /**
     * Update action when customer read a notification
     *
     * @param req IdPayload contain id of notification read
     * @return http status code
     */
    @PostMapping("/read")
    public ResponseEntity<?> updateNotificationRead(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.getPayload();
        Notification notification = validateNotification(payload);
        if (notification == null) {
            return badRequest(1220, "Notification is not exits");
        }
        JWTPayload jwtPayload = req.jwt();
        if (jwtPayload == null)
            return unauthorized();
        if (notification.getCustomerUuid() != null
                && jwtPayload.getRole() == HDConstant.ROLE.CUSTOMER
                && jwtPayload.getUuid() == notification.getCustomerUuid()) {
            return unauthorized();
        }

        try {
            NotificationAction action = notificationActionService.find(jwtPayload.getUuid(), notification.getId());
            if (action == null) {
                action = new NotificationAction(notification);
            }
            action.setCustomerUuid(jwtPayload.getUuid());
            action.setIsRead(1);
            action.setReadTime(new Date());
            if (action.getActionAt() == null)
                action.setActionAt(new Date());
            notificationActionService.save(action);
            if (notification.getCustomerUuid() != null) {
                notification.setIsRead(1);
                notification.setReadTime(new Date());
                notificationService.updateNotification(notification);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok();
    }

    /**
     * check notification read or not read
     *
     * @param req IdPayload contain id of notification read
     * @return http status code and true or false
     */
    @PostMapping("/checkReadAllNotification")
    public ResponseEntity<?> checkReadAllNotification(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload idPayload = req.init();
        UUID customerUuid = UUID.fromString(String.valueOf(idPayload.getId()));
        int count = notificationService.countNotReadByCustomerUuid(customerUuid);
        if (count > 0) {
            return ok(true);
        }
        return ok(false);
    }

    /**
     * Update action when customer read a notification
     *
     * @param req IdPayload contain id of notification read
     * @return http status code
     */
    @PostMapping("/readAll")
    public ResponseEntity<?> readAllByCustomer(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload idPayload = req.init();

        String strId = (String) idPayload.getId();
        UUID customerUuid = UUID.fromString(strId);

        try {

            List<Notification> notifications = notificationService.getNotReadByCustomerUuid(customerUuid);
            List<NotificationAction> notificationActions = new ArrayList<>();
            if (notifications != null && notifications.size() > 0) {
                for (Notification notification : notifications) {
                    if (notification.getCustomerUuid() != null) {
                        Notification no = notificationService.getById(notification.getId());
                        no.setIsRead(1);
                        no.setReadTime(new Date());
                        notificationService.updateNotification(no);
                    }
                    NotificationAction notificationAction = notificationActionService.find(customerUuid, notification.getId());
                    if (notificationAction == null) {
                        notificationAction = new NotificationAction(notification);
                        notificationAction.setCustomerUuid(customerUuid);
                    }
                    notificationAction.setIsRead(1);
                    notificationAction.setReadTime(new Date());
                    if (notificationAction.getActionAt() == null)
                        notificationAction.setActionAt(new Date());
                    notificationActions.add(notificationAction);
                }
                notificationActionService.saveAll(notificationActions);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok();
    }

    /**
     * Update action when customer delete a notification
     *
     * @param req IdPayload contain id of notification deleted
     * @return http status code
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload payload = req.getPayload();
        Notification notification = validateNotification(payload);
        if (notification == null) {
            return badRequest(1220, "Notification is not exits");
        }
        JWTPayload jwtPayload = req.jwt();
        if (jwtPayload == null)
            return unauthorized();
        if (notification.getCustomerUuid() != null
                && jwtPayload.getRole() == HDConstant.ROLE.CUSTOMER
                && jwtPayload.getUuid() == notification.getCustomerUuid()) {
            return unauthorized();
        }

        try {
            NotificationAction action = notificationActionService.find(jwtPayload.getUuid(), notification.getId());
            if (action == null) {
                action = new NotificationAction(notification);
            }
            action.setCustomerUuid(jwtPayload.getUuid());
            action.setIsDeleted(1);
            action.setDeletedTime(new Date());
            if (action.getActionAt() == null)
                action.setActionAt(new Date());
            notificationActionService.save(action);
            if (notification.getCustomerUuid() != null)
                notificationService.deleteNotification(notification);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok();
    }

    /**
     * Check Notification is exist or not
     *
     * @param payload IdPayload contain id of Notification
     * @return object Notification
     */
    public Notification validateNotification(IdPayload payload) {
        Integer notificationId;
        Notification notification = null;
        try {
            notificationId = (Integer) payload.getId();
            notification = notificationService.getById(notificationId);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }

        return notification;
    }


}
