package com.tinhvan.hd.vo;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class NotificationTemplateCreate implements HDPayload {
    private String title;

    private String body;

    private int type = 0;

    private int status = 1;

    private int isDeleted = 0;

    private int stored = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getStored() {
        return stored;
    }

    public void setStored(int stored) {
        this.stored = stored;
    }

    @Override
    public void validatePayload() {

        if (HDUtil.isNullOrEmpty(title))
            throw new BadRequestException(1300, "empty title");
        if (HDUtil.isNullOrEmpty(body))
            throw new BadRequestException(1219, "empty body");
    }
}
