package com.tinhvan.hd.filehandler.payload;

import java.util.List;

public class FileRequest implements BasePayload{
    private String fileTemplate;
    private List<MergeFilePdf> fields;

    public String getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(String fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public List<MergeFilePdf> getFields() {
        return fields;
    }

    public void setFields(List<MergeFilePdf> fields) {
        this.fields = fields;
    }

    @Override
    public void validatePayload() {

    }
}
