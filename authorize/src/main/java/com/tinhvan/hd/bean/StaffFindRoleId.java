package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.HDPayload;

public class StaffFindRoleId implements HDPayload {
    private int roleId;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public void validatePayload() {

    }
}
