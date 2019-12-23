package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.model.ConfigStaff;

import java.util.List;

public class ConfigStaffUpdate implements HDPayload {
    private List<ConfigStaff> list;

    public List<ConfigStaff> getList() {
        return list;
    }

    public void setList(List<ConfigStaff> list) {
        this.list = list;
    }

    @Override
    public void validatePayload() {
        if (list == null && list.isEmpty())
            throw new BadRequestException(1202, "list is empty");
    }
}
