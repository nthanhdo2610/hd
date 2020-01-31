package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class ConfigSendMailByType {

    private Integer type;

    private List<FileAndType> fileByTypes;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<FileAndType> getFileByTypes() {
        return fileByTypes;
    }

    public void setFileByTypes(List<FileAndType> fileByTypes) {
        this.fileByTypes = fileByTypes;
    }
}
