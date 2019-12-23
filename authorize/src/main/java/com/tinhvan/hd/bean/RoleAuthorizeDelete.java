package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class RoleAuthorizeDelete implements HDPayload {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void validatePayload() {
        if(id <= 0)
            throw new BadRequestException(1106, "invalid id");
    }
}
