package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.HDPayload;

public class StatisticsRegisterByDaysRequest implements HDPayload {
    private int numOfDays;
    @Override
    public void validatePayload() {
        if(numOfDays<=0)
            numOfDays=30;
    }

    public int getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    @Override
    public String toString() {
        return "StatisticsRegisterByDaysRequest{" +
                "numOfDays=" + numOfDays +
                '}';
    }
}
