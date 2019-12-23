package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class ReportRequest  implements HDPayload {
private String fileTemplate;

    public String getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(String fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    @Override
    public void validatePayload() {

    }
}
