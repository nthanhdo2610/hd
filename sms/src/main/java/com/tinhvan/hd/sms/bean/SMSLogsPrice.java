package com.tinhvan.hd.sms.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pricePerMessage",
        "currency"
})
public class SMSLogsPrice {
    @JsonProperty("pricePerMessage")
    private float pricePerMessage;

    @JsonProperty("currency")
    private String currency;

    public float getPricePerMessage() {
        return pricePerMessage;
    }

    public void setPricePerMessage(float pricePerMessage) {
        this.pricePerMessage = pricePerMessage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
