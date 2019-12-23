package com.tinhvan.hd.payload;

import com.tinhvan.hd.entity.Notification;

import java.util.List;

public class NotificationSearchResponse {
    private List<Notification> list;
    private int total;

    public List<Notification> getList() {
        return list;
    }

    public void setList(List<Notification> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public NotificationSearchResponse(List<Notification> list, int total) {
        this.list = list;
        this.total = total;
    }
}
