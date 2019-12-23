package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class AdjustmentUploadFileRequest implements HDPayload {

    private int id;
    private String createdName;
    private String filePath;
    private String description;

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(filePath))
            throw new BadRequestException(1429);
        if(description.length()>5000)
            throw new BadRequestException(1430);
        if(createdName.length()>255)
            throw new BadRequestException(1433);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
