package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class RoleFind implements HDPayload {

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void validatePayload() {
        //validate role
        if (HDUtil.isNullOrEmpty(role)) {
            throw new BadRequestException(1204, "invalid role");
        }
    }

}
