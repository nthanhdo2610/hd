package com.tinhvan.hd.sms.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "messageId",
        "to",
        "from",
        "text",
        "sentAt",
        "doneAt",
        "smsCount",
        "mccMnc",
        "price",
        "status",
        "error"
})
public class SMSLogsResults {
    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("to")
    private String to;

    @JsonProperty("from")
    private String from;

    @JsonProperty("text")
    private String text;

    @JsonProperty("sentAt")
    private String sentAt;

    @JsonProperty("doneAt")
    private String doneAt;

    @JsonProperty("smsCount")
    private float smsCount;

    @JsonProperty("mccMnc")
    private String mccMnc;

    @JsonProperty("price")
    private SMSLogsPrice price;

    @JsonProperty("status")
    private SMSLogsStatus status;

    @JsonProperty("error")
    private SMSLogsError error;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(String doneAt) {
        this.doneAt = doneAt;
    }

    public float getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(float smsCount) {
        this.smsCount = smsCount;
    }

    public String getMccMnc() {
        return mccMnc;
    }

    public void setMccMnc(String mccMnc) {
        this.mccMnc = mccMnc;
    }

    public SMSLogsPrice getPrice() {
        return price;
    }

    public void setPrice(SMSLogsPrice price) {
        this.price = price;
    }

    public SMSLogsStatus getStatus() {
        return status;
    }

    public void setStatus(SMSLogsStatus status) {
        this.status = status;
    }

    public SMSLogsError getError() {
        return error;
    }

    public void setError(SMSLogsError error) {
        this.error = error;
    }
}
