package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class RolePost implements HDPayload {
    private String name;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void validatePayload() {
        //validate name
        if (HDUtil.isNullOrEmpty(name)) {
            throw new BadRequestException(1203, "empty name");
        }
        //validate role
        if (HDUtil.isNullOrEmpty(role)) {
            throw new BadRequestException(1204, "invalid role");
        }
    }
}
