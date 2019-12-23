package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.base.Log;
import com.tinhvan.hd.service.PushNotificationsService;
import com.tinhvan.hd.vo.HeaderRequestInterceptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


@Service
public class PushNotificationsServiceImpl implements PushNotificationsService {

    private static final String FIREBASE_SERVER_KEY = "AAAAHeOaUEY:APA91bHQtAfZ-GaNz6myQrxnxxynUhC1QY11betPM7D6A3sAv0aVy5gtvllv8mJgUEoi57NoQ9a3N369ajWWrmHlD4F9tOlAbb6dPm-Q5d8cqNvICN0uw-JFYwGBZfmfaqbotf3_Skrw";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    public CompletableFuture<String> send(HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
