package com.tinhvan.hd.filehandler.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.filehandler.exception.BadRequestException;
import com.tinhvan.hd.filehandler.exception.HandlerException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestDTO<T extends BasePayload> {

    private HttpServletRequest httpRequest;

    @JsonProperty(value = "payload", required = true)
    private T payload;

    public RequestDTO() {
        ServletRequestAttributes ra = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (ra != null) {
            httpRequest = ra.getRequest();
        }
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public T init() throws HandlerException {
        return init(true);
    }

    public T init(boolean isValidate) throws HandlerException {
        if (!isValidate) {
            return payload;
        }
        if (payload == null) {
            throw new BadRequestException(400, "invalid payload");
        }
        payload.validatePayload();
        return payload;
    }

}
