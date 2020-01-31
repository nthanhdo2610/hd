package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class RequestConfigSendMail implements HDPayload {

    private List<ConfigSendMailByType> data;

    public List<ConfigSendMailByType> getData() {
        return data;
    }

    public void setData(List<ConfigSendMailByType> data) {
        this.data = data;
    }

    @Override
    public void validatePayload() {

    }
}
