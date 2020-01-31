package com.tinhvan.hd.worker;

import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.payload.NotificationQueueDTO;
import com.tinhvan.hd.service.NotificationActionService;
import com.tinhvan.hd.service.NotificationQueueService;
import com.tinhvan.hd.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NotificationListener {

    @Autowired
    private NotificationQueueService notificationQueueService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationActionService notificationActionService;

    @RabbitListener(queues = RabbitConfig.QUEUE_UPDATE_NOTIFICATION)
    public void updateNotification(NotificationQueueDTO dto) {
        System.out.println("QUEUE_UPDATE_NOTIFICATION");
        try {
            notificationQueueService.update(dto);
            notificationService.update(dto);
            notificationActionService.update(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Scheduled(cron = "0/5 * * * * *", zone = "Asia/Bangkok")
    void test(){
        System.out.println("sssssssssssssss");
        NotificationQueueDTO dto = new NotificationQueueDTO();
        dto.setPromotionId("bf549201-dd06-4069-bb5b-b9ff4411bf0f");
        dto.setEndDate(new Date());
        dto.setContent("<p>aaaaa</p>");
        dto.setTitle("Lãi suất siêu hời sắm ngay Winner X");
        notificationQueueService.update(dto);
        notificationService.update(dto);
        notificationActionService.update(dto);
    }*/
}
