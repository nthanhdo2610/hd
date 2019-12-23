package com.tinhvan.hd.vo;

import com.tinhvan.hd.entity.NotificationTemplate;

import java.util.List;

public class NotificationTemplateListRespon {

    private List<NotificationTemplate> list;
    private int count;

    public NotificationTemplateListRespon() {
    }

    public NotificationTemplateListRespon(List<NotificationTemplate> list, int count) {
        this.list = list;
        this.count = count;
    }

    public List<NotificationTemplate> getList() {
        return list;
    }

    public void setList(List<NotificationTemplate> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
