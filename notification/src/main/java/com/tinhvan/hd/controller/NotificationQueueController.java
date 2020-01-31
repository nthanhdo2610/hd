package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.*;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationQueue;
import com.tinhvan.hd.payload.NotificationQueueDTO;
import com.tinhvan.hd.service.NotificationQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification/notification_queue")
public class NotificationQueueController extends HDController {

    @Autowired
    private NotificationQueueService notificationQueueService;

    /**
     * Insert notification into queue wait for push notification to customer
     *
     * @param req contain content for send notification
     * @return http status code
     */
    @PostMapping
    public ResponseEntity<?> saveNotificationQueue(@RequestBody RequestDTO<NotificationQueueDTO> req) {
        NotificationQueueDTO queue = req.init();

        if (queue.getCustomerUuids() != null && queue.getCustomerUuids().size() > 0) {
            for (int i = 0; i < queue.getCustomerUuids().size(); i++) {
                saveNotificationQueue(queue, req.now(), queue.getCustomerUuids().get(i));
            }
        } else {
            saveNotificationQueue(queue, req.now(), null);
        }
        return ok();
    }

    /**
     * Insert NotificationQueue
     *
     * @param queue      NotificationQueueDTO contain notification info
     * @param createAt   time send notification
     * @param customerId customer uuid receipt notification
     */
    void saveNotificationQueue(NotificationQueueDTO queue, Date createAt, UUID customerId) {
        NotificationQueue notificationQueue = new NotificationQueue();
        notificationQueue.setCreatedAt(createAt);
        notificationQueue.setContent(queue.getContent());
        notificationQueue.setTitle(queue.getTitle());
        notificationQueue.setType(queue.getType());
        if (!HDUtil.isNullOrEmpty(queue.getNewsId())) {
            //notificationQueue.setType(HDConstant.NotificationType.NEWS);
            notificationQueue.setNewsId(UUID.fromString(queue.getNewsId()));
        }
        if (!HDUtil.isNullOrEmpty(queue.getPromotionId())) {
            //notificationQueue.setType(HDConstant.NotificationType.PROMOTION);
            notificationQueue.setPromotionId(UUID.fromString(queue.getPromotionId()));
        }
        notificationQueue.setAccess(queue.getAccess());
        notificationQueue.setStatus(0);
        notificationQueue.setLangCode(queue.getLangCode());
        if (customerId != null) {
            notificationQueue.setCustomerId(customerId);
        }
        if (queue.getEndDate() != null)
            notificationQueue.setEndDate(queue.getEndDate());
        notificationQueueService.insert(notificationQueue);
    }
}
