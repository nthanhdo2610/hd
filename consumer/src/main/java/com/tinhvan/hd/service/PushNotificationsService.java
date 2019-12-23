package com.tinhvan.hd.service;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface PushNotificationsService {

    CompletableFuture<String> send(HttpEntity<String> entity);

}
