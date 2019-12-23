package com.tinhvan.hd.customer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExpiredPasswordResponse {
    @JsonProperty("daysLeft")
    private int daysLeft;
    @JsonProperty("warning")
    private boolean warning;

    public ExpiredPasswordResponse(int daysLeft, boolean warning) {
        this.daysLeft = daysLeft;
        this.warning = warning;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }
}
