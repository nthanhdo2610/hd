package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class RoleAuthorizeUpdateRequest implements HDPayload {
    private RoleAuthorizeRequest roleAuthorizeRequest;
    private List<RoleAuthorizeRespon> listRoleAuthorizeRespon;

    public RoleAuthorizeUpdateRequest(RoleAuthorizeRequest roleAuthorizeRequest, List<RoleAuthorizeRespon> listRoleAuthorizeRespon) {
        this.roleAuthorizeRequest = roleAuthorizeRequest;
        this.listRoleAuthorizeRespon = listRoleAuthorizeRespon;
    }

    public RoleAuthorizeRequest getRoleAuthorizeRequest() {
        return roleAuthorizeRequest;
    }

    public void setRoleAuthorizeRequest(RoleAuthorizeRequest roleAuthorizeRequest) {
        this.roleAuthorizeRequest = roleAuthorizeRequest;
    }

    public List<RoleAuthorizeRespon> getListRoleAuthorizeRespon() {
        return listRoleAuthorizeRespon;
    }

    public void setListRoleAuthorizeRespon(List<RoleAuthorizeRespon> listRoleAuthorizeRespon) {
        this.listRoleAuthorizeRespon = listRoleAuthorizeRespon;
    }

    @Override
    public void validatePayload() {
        if (roleAuthorizeRequest == null)
            throw new BadRequestException(1239, "object is null");
        if (listRoleAuthorizeRespon == null && listRoleAuthorizeRespon.isEmpty())
            throw new BadRequestException(1202, "list empty");

    }

    @Override
    public String toString() {
        return "roleAuthorizeRequest=" + roleAuthorizeRequest +
                ", listRoleAuthorizeRespon=" + listRoleAuthorizeRespon;
    }
}
