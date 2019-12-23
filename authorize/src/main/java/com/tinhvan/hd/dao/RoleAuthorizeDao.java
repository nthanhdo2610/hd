package com.tinhvan.hd.dao;

import com.tinhvan.hd.base.IdPayload;
import com.tinhvan.hd.bean.RoleAuthorizeListByRoleIdRespon;
import com.tinhvan.hd.bean.RoleAuthorizeRequest;
import com.tinhvan.hd.bean.RoleAuthorizeListRespon;
import com.tinhvan.hd.model.RoleAuthorize;

import java.util.List;

public interface RoleAuthorizeDao {

//    void insertOrUpdate(RoleAuthorize object);
//
//    void deleteRole();

    //RoleAuthorize find(int id);

    RoleAuthorizeListRespon getlist(int roleId);

    RoleAuthorize findByRoleIdAndServiceId(int roleId, int serviceId);

    List<RoleAuthorizeListByRoleIdRespon> getListByRoleId(int roleId);
}
