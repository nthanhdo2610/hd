package com.tinhvan.hd.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

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
    private Date sentAt;

    @JsonProperty("doneAt")
    private Date doneAt;

    @JsonProperty("smsCount")
    private int smsCount;

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

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public Date getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(Date doneAt) {
        this.doneAt = doneAt;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
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
