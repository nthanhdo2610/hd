package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.sms.model.SMSTemplate;

import java.util.List;

public class SMSTemplateListRespon {
    private List<SMSTemplate> list;
    private int count;

    public SMSTemplateListRespon() {
    }

    public SMSTemplateListRespon(List<SMSTemplate> list, int count) {
        this.list = list;
        this.count = count;
    }

    public List<SMSTemplate> getList() {
        return list;
    }

    public void setList(List<SMSTemplate> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
