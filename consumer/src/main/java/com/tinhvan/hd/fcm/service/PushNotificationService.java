package com.tinhvan.hd.fcm.service;

import com.tinhvan.hd.fcm.firebase.FCMService;
import com.tinhvan.hd.fcm.model.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PushNotificationService {

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Autowired
    private FCMService fcmService;

    public void sendPushNotification(Map<String, String> data, PushNotificationRequest request) {
        try {
            fcmService.sendMessage(data, request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

}
