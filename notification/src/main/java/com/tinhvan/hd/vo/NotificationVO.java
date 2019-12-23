package com.tinhvan.hd.vo;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import java.util.List;
import java.util.UUID;

public class NotificationVO implements HDPayload {

    private Integer typeTemplate;

    private String[] params;

    private UUID customerUuid;

    private List<UUID> customerUuids;

    private String langCode;

    public Integer getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(Integer typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public List<UUID> getCustomerUuids() {
        return customerUuids;
    }

    public void setCustomerUuids(List<UUID> customerUuids) {
        this.customerUuids = customerUuids;
    }

    @Override
    public void validatePayload() {
        if(typeTemplate == null || typeTemplate <= 0){
            throw new BadRequestException(400, "type is empty");
        }
    }
}
