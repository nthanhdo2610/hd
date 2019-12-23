package com.tinhvan.hd.config;

import com.tinhvan.hd.base.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;

public class InvokeConfig {

    private Invoker invoker = new Invoker();
    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    public void invokeCheckToken(RequestDTO<?> req){
        //call invoke staff
        IdPayload idPayload = new IdPayload();
        idPayload.setId(req);
        ResponseDTO<String> rs = invoker.call(urlStaffRequest + "/check_token", idPayload, new ParameterizedTypeReference<ResponseDTO<String>>() {
        });
        if(rs.getCode() != 200){
            throw new BadRequestException(401, "unauthorized");
        }
    }
}
