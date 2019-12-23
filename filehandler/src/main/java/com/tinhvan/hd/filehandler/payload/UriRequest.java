package com.tinhvan.hd.filehandler.payload;

import com.tinhvan.hd.filehandler.exception.BadRequestException;
import com.tinhvan.hd.filehandler.utils.BaseUtil;

public class UriRequest implements BasePayload {

    public String uri;

    @Override
    public void validatePayload() {
        if(BaseUtil.isNullOrEmpty(uri))
            throw new BadRequestException();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
