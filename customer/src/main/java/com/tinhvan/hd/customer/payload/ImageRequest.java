package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class ImageRequest implements HDPayload {

    private String uuid;
    private String fileName;
    private int type;

    @Override
    public void validatePayload() {
        try {
            UUID.fromString(uuid);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ImageRequest{" +
                "uuid='" + uuid + '\'' +
                ", fileName='" + fileName + '\'' +
                ", type=" + type +
                '}';
    }
}
