package com.tinhvan.hd.customer.payload;

public class StatisticsRegisterByDaysResponse {
    private String date;
    private int count;

    public StatisticsRegisterByDaysResponse(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "StatisticsRegisterByDaysResponse{" +
                "date='" + date + '\'' +
                ", count=" + count +
                '}';
    }
}
