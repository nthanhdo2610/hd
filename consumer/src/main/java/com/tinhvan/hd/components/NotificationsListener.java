package com.tinhvan.hd.components;

import com.tinhvan.hd.base.Config;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.entity.Device;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.entity.enumtype.NotificationType;
import com.tinhvan.hd.service.DeviceService;
import com.tinhvan.hd.service.NotificationService;
import com.tinhvan.hd.service.NotificationTemplatService;
import com.tinhvan.hd.service.PushNotificationsService;
import com.tinhvan.hd.vo.NotificationVO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NotificationsListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationsListener.class);

    @Autowired
    private PushNotificationsService pushNotificationsService;

    @Autowired
    private NotificationTemplatService notificationTemplatService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Config config;

    @RabbitListener(queues = RabbitConfig.QUEUE_SEND_NOTIFICATIONS)
    public void sendNotification(NotificationVO vo) {

        List<UUID> customers = vo.getCustomerUuids();
        for (UUID customerUuid : customers) {
            String language = vo.getLangCode();
            if (language == null) {
                language = "vi";
            }
            Device device = null;
            int typeTemplate = vo.getTypeTemplate();
            try {
                device = deviceService.getDeviceByCustomerUuid(customerUuid);
            } catch (Exception ex) {
                logger.error("Device by CustomerUuid not exits : " + customerUuid);
                throw new AmqpRejectAndDontRequeueException(ex.getMessage());
            }

            if (device != null) {
                NotificationTemplate template = null;
                try {
                    template = notificationTemplatService.getByType(vo.getTypeTemplate(), language);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new AmqpRejectAndDontRequeueException(ex.getMessage());
                }

                switch (NotificationType.parseValue(typeTemplate)) {
                    case NOTIFICATION_CONTRACT_ONLINE:
                        sendNotificationToFirebase(customerUuid, device.getFcmToken(), NotificationType.NOTIFICATION_CONTRACT_ONLINE, template, vo.getParams());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void sendNotificationToFirebase(UUID customerUuid, String fcmToken, NotificationType topic, NotificationTemplate template, String[] params) {

        String title = template.getTitle();
        String content = template.getBody();

        String contentNew = convertContentByParams(content, params);

        JSONObject body = new JSONObject();
        body.put("to", fcmToken);
        //body.put("priority", "high");

        JSONObject noti = new JSONObject();
        noti.put("title", title);
        noti.put("body", contentNew);

        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("body", contentNew);

        body.put("notification", noti);
        body.put("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = pushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();
        String valueConfig = config.get("NOTIFICATION_STORE");
        try {
            pushNotification.get();
            logger.error("Push Notification Sent OK!");
            // NOTIFICATION_STORE = 0 : no save; =1 : save when send fcm success;=2 : auto save
            if (!valueConfig.equals("0")) {
                saveNotification(template.getType(), customerUuid, content, params);
            }
        } catch (InterruptedException e) {
            logger.error("Error Sending Push Notifications : " + e.getMessage());
            if (valueConfig.equals("2")) {
                saveNotification(template.getType(), customerUuid, content, params);
            }
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        } catch (ExecutionException e) {
            logger.error("Error Sending Push Notifications : " + e.getMessage());
            if (valueConfig.equals("2")) {
                saveNotification(template.getType(), customerUuid, content, params);
            }
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }

    void saveNotification(int type, UUID customerUuid, String content, String[] params) {
        try {
            Notification notification = new Notification();
            notification.setTemplateType(type);
            notification.setStatus(1);
            notification.setIsSent(1);
            notification.setIsRead(0);
            notification.setContent(content);
            notification.setContentPara(params);
            notification.setCreatedAt(new Date());
            notification.setSendTime(new Date());
            notification.setCustomerUuid(customerUuid);
            notificationService.saveNotification(notification);
        } catch (Exception ex) {
            throw new AmqpRejectAndDontRequeueException(ex.getMessage());
        }
    }

    String convertContentByParams(String content, String[] params) {
        try {
            String contentNew = MessageFormat.format(content, params);
            return contentNew;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error convertContentByParams Notifications : " + ex.getMessage());
            return "";
        }
    }
}
