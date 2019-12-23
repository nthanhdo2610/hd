package com.tinhvan.hd.staff.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class StaffSignOut implements HDPayload {
    private UUID id;

    public StaffSignOut() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void validatePayload() {
        if (id == null)
            throw new BadRequestException(1200, "empty uuid");
    }
}
