package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;
import io.swagger.models.auth.In;

public class ContractFileTemplateRQ implements HDPayload {

    private Integer id;

    private String path;

    private String type;

    private String fileName;

    private int idx;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    @Override
    public void validatePayload() {

    }
}
