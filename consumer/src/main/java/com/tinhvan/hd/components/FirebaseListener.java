package com.tinhvan.hd.components;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.Log;
import com.tinhvan.hd.base.NotFoundException;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.vo.FirebaseRequest;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class FirebaseListener {

    @Value("${app.firebase-topic-all}")
    private String topicAllDevices;

    @RabbitListener(queues = RabbitConfig.QUEUE_SUBSCRIBE_TOPIC_FIREBASE)
    void subscribeTokenToTopic(FirebaseRequest request) {
        //System.out.println("QUEUE_SUBSCRIBE_TOPIC_FIREBASE:"+request.toString());
        if (HDUtil.isNullOrEmpty(request.getTopic())) {
            request.setTopic(topicAllDevices);
        }
        if (ObjectUtils.isEmpty(request.getTokens())) {
            Log.print("Caused by: java.lang.IllegalArgumentException: registration tokens list must not contain null or empty strings");
            //throw new AmqpRejectAndDontRequeueException("");
        } else {
            try {
                if (request.getTokens() != null && request.getTokens().size() > 0)
                    FirebaseMessaging.getInstance().subscribeToTopic(request.getTokens(), request.getTopic());
            } catch (FirebaseMessagingException e) {
                e.getMessage();
                //throw new AmqpRejectAndDontRequeueException(e.getMessage());
            }
        }
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_UNSUBSCRIBE_TOPIC_FIREBASE)
    void unsubscribeTokenToTopic(FirebaseRequest request) {
        if (HDUtil.isNullOrEmpty(request.getTopic())) {
            request.setTopic(topicAllDevices);
        }
        List<String> a = new ArrayList<>();
        if (ObjectUtils.isEmpty(request.getTokens())) {
            Log.print("Caused by: java.lang.IllegalArgumentException: registration tokens list must not contain null or empty strings");
            //throw new AmqpRejectAndDontRequeueException("");
        } else {
            try {
                if (request.getTokens() != null && request.getTokens().size() > 0)
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(request.getTokens(), request.getTopic());
            } catch (FirebaseMessagingException e) {
                e.getMessage();
                //throw new AmqpRejectAndDontRequeueException(e.getMessage());
            }
        }

    }
}
