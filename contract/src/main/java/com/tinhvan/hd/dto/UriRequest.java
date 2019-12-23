package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class UriRequest implements HDPayload {

    private String uri;

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(uri))
            throw new BadRequestException();
    }

    public UriRequest(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
