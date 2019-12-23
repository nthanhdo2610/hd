package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class RoleUpdate implements HDPayload {
    private long id;
    private String name;
    private String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
        if (id <= 0)
            throw new BadRequestException(1106, "invalid id");
        if (HDUtil.isNullOrEmpty(name))
            throw new BadRequestException(1203, "empty name");
        if (HDUtil.isNullOrEmpty(role))
            throw new BadRequestException(1204, "invalid role");
    }
}
