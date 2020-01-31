package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.common.HeaderRequestInterceptor;
import com.tinhvan.hd.service.PushNotificationsService;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


@Service
public class PushNotificationsServiceImpl implements PushNotificationsService {

    private static final String FIREBASE_SERVER_KEY = "AAAAkRAOjQ4:APA91bHh01mG9YjvsbTQhGO4h6mBZYJLfFPTVa9Bxf_h5w-wqOf09nQjBbAIEa6hiM8nTz78U9fvfBmbjNtioPCjRFV7hggtGjdQCKs4piEIBq2BoxYGpLOIRPedGIXo-xM3DQbfwgmr";
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
