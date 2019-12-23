package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class ConfigStaffGetValue implements HDPayload {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(key)) {
            throw new BadRequestException(1221, "empty key");
        }
    }

    @Override
    public String toString() {
        return "ConfigStaffGetValue{" +
                "key='" + key + '\'' +
                '}';
    }
}
