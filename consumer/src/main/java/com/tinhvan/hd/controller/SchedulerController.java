package com.tinhvan.hd.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.tinhvan.hd.fcm.model.PushNotificationRequest;
import com.tinhvan.hd.fcm.service.PushNotificationService;
import com.tinhvan.hd.vo.FirebaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

@Component
public class SchedulerController {

    @Autowired
    private PushNotificationService pushNotificationService;

    @Value("${app.firebase-topic-all}")
    private String topicAllDevices;

    //@Scheduled(cron = "0/20 * * * * *")
    void pushNotification() {
        try {
            System.out.println("pushNotification");
            String fcm = "fLPP7CO8-us:APA91bFS6W7kGb2vsmJe3BS9LDInEN6E4MVozd1McNONQEunZDAIUZ6KY3_KOyrEFp4kcFTKlM5_ABFy8qNEXgQCkHmx8s4sSYn2QCzFJqqIVRDdt4_LLnNJ3JWHDhhuAz0Cq1fZITX0";
            FirebaseRequest request = new FirebaseRequest();
            request.setTopic(topicAllDevices);
            request.setTokens(Arrays.asList(fcm));
            FirebaseMessaging.getInstance().unsubscribeFromTopic(request.getTokens(), request.getTopic());

            HashMap<String, String> params = new HashMap<>();
            params.put("type", "1");
            params.put("id", "1b1a5b95-d84a-4613-bfa6-4c762eeb8a59");
            PushNotificationRequest pushRequest = new PushNotificationRequest();
            pushRequest.setTitle("Lãi suất siêu hời sắm ngay Winner X");
            pushRequest.setMessage("Lãi suất siêu hời sắm ngay Winner X");
            //pushRequest.setToken(fcm);
            pushRequest.setTopic(topicAllDevices);
            pushNotificationService.sendPushNotification(params, pushRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
