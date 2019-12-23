package com.tinhvan.hd.customer.rabbitmq;

import java.util.List;

public class FirebaseRequest {
    private String topic;
    private List<String> tokens;

    public FirebaseRequest(String topic, List<String> tokens) {
        this.topic = topic;
        this.tokens = tokens;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
