package com.tinhvan.hd.vo;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationQueueVO implements HDPayload {

    private String title;

    private String content;

    private UUID customerId;

    private String contractCode;

    private List<String> fcmTokens;

    private String langCode;

    private int type;

    private UUID uuid;

    private int access;

    private int status;

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(langCode))
            langCode = "vi";
        if (HDUtil.isNullOrEmpty(title))
            title = "";
        if (!HDUtil.isNullOrEmpty(title) && title.length() > 128)
            throw new BadRequestException(1301);

        if (HDUtil.isNullOrEmpty(content))
            content = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getFcmTokens() {
        return fcmTokens;
    }

    public void setFcmTokens(List<String> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}