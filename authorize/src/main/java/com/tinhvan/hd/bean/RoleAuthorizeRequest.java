package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class RoleAuthorizeRequest implements HDPayload {
    private int roleId;
    private String roleCode;
    private String roleName;

    public RoleAuthorizeRequest() {
    }

    public RoleAuthorizeRequest(int roleId) {
        this.roleId = roleId;
    }


    public RoleAuthorizeRequest(int roleId, String roleCode, String roleName) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public void validatePayload() {
        //validate role
        if (roleId <= 0)
            throw new BadRequestException(1204, "invalid role");
        //role code
        if (HDUtil.isNullOrEmpty(roleCode))
            throw new BadRequestException(1213, "empty role code");
        //role name
        if (HDUtil.isNullOrEmpty(roleName))
            throw new BadRequestException(1214, "empty role name");
    }
}
