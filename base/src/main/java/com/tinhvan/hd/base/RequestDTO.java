package com.tinhvan.hd.base;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestDTO<T extends HDPayload> {

    private HttpServletRequest httpRequest;

    @JsonProperty(value = "payload", required = true)
    private T payload;
    private Date now;
    private String langCode;
    private JWTPayload jwt;
    private String environment;

    public RequestDTO() {
        this.now = new Date();
        ServletRequestAttributes ra = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (ra != null) {
            httpRequest = ra.getRequest();
            langCode = handleLanguage(httpRequest);
            environment = handleEnvironment(httpRequest);
            jwt = (JWTPayload) httpRequest.getAttribute("JWT");
        }
    }

    @SuppressWarnings("unchecked")
    public RequestDTO(HDPayload payload) {
        this.payload = (T) payload;
    }

    public String getHeader(String name) {
        if (httpRequest == null) {
            return "";
        }
        return httpRequest.getHeader(name);
    }

    private String handleLanguage(HttpServletRequest req) {
        String language = req.getHeader("accept-language");
        if (HDUtil.isNullOrEmpty(language)) {
            language = "vi";
        }
        return language;
    }

    private String handleEnvironment(HttpServletRequest req) {
        String environment = req.getHeader("x-environment");
        if (!HDUtil.isNullOrEmpty(environment)) {
            //environment = HDConstant.ENVIRONMENT.WEB_ESIGN;
            return environment.toUpperCase();
        }
        return "";
    }

    public Date now() {
        return now;
    }

    public String langCode() {
        return langCode;
    }

    public JWTPayload jwt() {
        return jwt;
    }

    public void setPayload(T v) {
        payload = v;
    }

    public T getPayload() {
        return payload;
    }

    public String environment() {
        return environment;
    }

    public T init() throws HDException {
        return init(true);
    }

    public T init(boolean isValidate) throws HDException {
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
