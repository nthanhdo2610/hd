package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class RoleAuthorizeListRespon implements HDPayload {
    private int roleId;
    private List<RoleAuthorizeRespon> listRoleAuthorizeRespon;

    public RoleAuthorizeListRespon() {
    }

    public RoleAuthorizeListRespon(int roleId, List<RoleAuthorizeRespon> listRoleAuthorizeRespon) {
        this.roleId = roleId;
        this.listRoleAuthorizeRespon = listRoleAuthorizeRespon;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public List<RoleAuthorizeRespon> getListRoleAuthorizeRespon() {
        return listRoleAuthorizeRespon;
    }

    public void setListRoleAuthorizeRespon(List<RoleAuthorizeRespon> listRoleAuthorizeRespon) {
        this.listRoleAuthorizeRespon = listRoleAuthorizeRespon;
    }

    @Override
    public void validatePayload() {
        if (roleId <= 0)
            throw new BadRequestException(1238, "invalid role id");
        if (listRoleAuthorizeRespon == null && listRoleAuthorizeRespon.isEmpty())
            throw new BadRequestException(1202, "list empty");

    }


}
