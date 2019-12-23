package com.tinhvan.hd.vo;

import java.util.List;

public class FirebaseRequest {
    private String topic;
    private List<String> tokens;

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

    @Override
    public String toString() {
        return "FirebaseRequest{" +
                "topic='" + topic + '\'' +
                ", tokens=" + tokens +
                '}';
    }
}
