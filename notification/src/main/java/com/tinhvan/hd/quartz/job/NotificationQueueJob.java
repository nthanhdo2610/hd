package com.tinhvan.hd.quartz.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.entity.Notification;
import com.tinhvan.hd.entity.NotificationQueue;
import com.tinhvan.hd.service.NotificationQueueService;
import com.tinhvan.hd.service.NotificationService;
import com.tinhvan.hd.vo.NotificationQueueVO;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.*;

@DisallowConcurrentExecution
public class NotificationQueueJob extends HDController implements Job {

    @Autowired
    private NotificationQueueService notificationQueueService;

    @Autowired
    private NotificationService notificationService;

    /**
     * Function auto run to read NotificationQueue need to push notification
     *
     * @param jobExecutionContext object config time to scan NotificationQueue
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        //send notification_queue to rabbitMQ
        List<NotificationQueue> queues = notificationQueueService.findPushNotification();
        for (NotificationQueue queue : queues) {
            boolean isSendNotification = true;
            NotificationQueueVO vo = new NotificationQueueVO();
            vo.setStatus(queue.getStatus());
            if (queue.getTitle() != null)
                vo.setTitle(queue.getTitle());
            if (queue.getContent() != null)
                vo.setContent(queue.getContent());
            if (queue.getLangCode() != null)
                vo.setLangCode(queue.getLangCode());
            if (queue.getNewsId() != null && queue.getType() != null && (queue.getType() == HDConstant.NotificationType.NEWS || queue.getType() == HDConstant.NotificationType.EVENT)) {
                if (queue.getStatus() == NotificationQueue.STATUS.FAIL)
                    isSendNotification = validate_SendNotification(queue.getNewsId(), queue.getType());
                vo.setUuid(queue.getNewsId());
            }
            if (queue.getPromotionId() != null && queue.getType() != null && queue.getType() == HDConstant.NotificationType.PROMOTION) {
                if (queue.getStatus() == NotificationQueue.STATUS.FAIL)
                    isSendNotification = validate_SendNotification(queue.getPromotionId(), queue.getType());
                vo.setUuid(queue.getPromotionId());
            }
            if (queue.getType() == HDConstant.NotificationType.PAYMENT_ALERT) {
                Calendar createDate = Calendar.getInstance();
                Calendar currentDate = Calendar.getInstance();
                createDate.setTime(queue.getCreatedAt());
                if (queue.getStatus() == NotificationQueue.STATUS.FAIL)
                    isSendNotification = createDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR) &&
                            createDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR);
            }
            if (queue.getType() != null)
                vo.setType(queue.getType());
            if (queue.getCustomerId() != null) {
                vo.setCustomerId(queue.getCustomerId());
                if (HDUtil.isNullOrEmpty(queue.getFcmToken())) {
                    queue.setFcmToken(invokeCustomer_getFcmTokenByCustomerUuid(queue.getCustomerId()));
                }
                vo.setFcmTokens(Arrays.asList(queue.getFcmToken()));
            }
            if (queue.getAccess() != null)
                vo.setAccess(queue.getAccess());
            if (queue.getContractCode() != null)
                vo.setContractCode(queue.getContractCode());
            if (queue.getEndDate() != null)
                vo.setEndDate(queue.getEndDate());
            queue.setSendAt(new Date());

            if (isSendNotification) {
                notificationService.sendNotificationQueue(vo);
                //if notification to customer but not have fcm token then update status notification queue = -1 and not push notification
                if (vo.getCustomerId() != null && HDUtil.isNullOrEmpty(queue.getFcmToken())) {
                    // update status notification queue
                    queue.setStatus(NotificationQueue.STATUS.FAIL);
                    notificationQueueService.update(queue);
                } else {
                    notificationQueueService.delete(queue);
                }
            } else {
                queue.setStatus(NotificationQueue.STATUS.IGNORE);
                notificationQueueService.update(queue);
            }
        }
    }


    @Value("${app.module.customer-service.url}")
    private String urlCustomerRequest;
    @Value("${app.module.promotion-service.url}")
    private String urlPromotionRequest;
    @Value("${app.module.news-service.url}")
    private String urlNewsRequest;

    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();
    private IdPayload idPayload = new IdPayload();

    /**
     * Invoke customer service get fcm token by customer uuid
     *
     * @param uuid customer uuid
     * @return string value of fcm token
     */
    String invokeCustomer_getFcmTokenByCustomerUuid(UUID uuid) {
        String fcmToken = "";
        try {
            idPayload.setId(uuid);
            ResponseDTO<Object> dto = invoker.call(urlCustomerRequest + "/fcm_token", idPayload,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                fcmToken = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                        new TypeReference<String>() {
                        });
            } else {
            }
        } catch (IOException e) {
            Log.error(e.getMessage());
            e.printStackTrace();
        }
        return fcmToken;
    }

    /**
     * Check object is valid to send notification
     *
     * @param uuid object uuid
     * @param type object type to valid
     * @return
     */
    boolean validate_SendNotification(UUID uuid, int type) {
        String uri = "";
        if (type == HDConstant.NotificationType.PROMOTION)
            uri = urlPromotionRequest;
        if (type == HDConstant.NotificationType.NEWS || type == HDConstant.NotificationType.EVENT)
            uri = urlNewsRequest;
        if (HDUtil.isNullOrEmpty(uri))
            return false;
        idPayload.setId(uuid);
        try {
            ResponseDTO<Object> dto = invoker.call(uri + "/checkValid", idPayload,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto.getCode() == HttpStatus.OK.value()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
