package com.tinhvan.hd.components;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.fcm.model.PushNotificationRequest;
import com.tinhvan.hd.fcm.service.PushNotificationService;
import com.tinhvan.hd.service.NotificationService;
import com.tinhvan.hd.vo.NotificationQueueVO;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Component
public class NotificationQueueListener {
    Logger logger = Logger.getLogger(SMSListener.class.getName());

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private NotificationService notificationService;

    @Value("${app.firebase-topic-all}")
    private String topicAllDevices;

    /**
     * Listener to send notification
     *
     * @param vo NotificationQueueVO contain info need to push notification
     */
    @RabbitListener(queues = RabbitConfig.QUEUE_SEND_NOTIFICATION_QUEUE)
    public void sendNotificationQueue(NotificationQueueVO vo) {
        Notification notification = new Notification(vo);
        try {
            List<String> fcmTokens = vo.getFcmTokens();
            HashMap<String, String> params = new HashMap<>();
            params.put("type", String.valueOf(vo.getType()));
            params.put("access", String.valueOf(vo.getAccess()));
            if (vo.getType() == HDConstant.NotificationType.PAYMENT_ALERT && vo.getContractCode() != null) {
                params.put("contractCode", vo.getContractCode());
            }

            if (vo.getUuid() != null)
                params.put("id", vo.getUuid().toString());

            if (vo.getCustomerId() != null && fcmTokens != null && fcmTokens.size() > 0) {
                fcmTokens.forEach(fcm -> {
                    if (!HDUtil.isNullOrEmpty(fcm)) {
                        PushNotificationRequest request = new PushNotificationRequest();
                        request.setTitle(vo.getTitle());
                        request.setMessage(vo.getContent());
                        request.setToken(fcm);
                        //System.out.println("sendNotificationQueue:" + request.toString());
                        pushNotificationService.sendPushNotification(params, request);
                    }
                });
            } else if (vo.getCustomerId() == null) {
                //send topic
                PushNotificationRequest request = new PushNotificationRequest();
                request.setTitle(vo.getTitle());
                request.setMessage(vo.getContent());
                request.setTopic(topicAllDevices);
                //System.out.println("sendNotificationQueue:" + request.toString());
                pushNotificationService.sendPushNotification(params, request);
            }
            notification.setIsSent(1);
            notification.setSendTime(new Date());

        } catch (AmqpRejectAndDontRequeueException ex) {
            notification.setIsSent(0);
            logger.info(ex.getMessage());
            ex.printStackTrace();
        } finally {
            //System.out.println("notification:" + notification.toString());
            if (vo.getStatus() == 0) {
                notificationService.saveNotification(notification);
            }
        }
    }
}
