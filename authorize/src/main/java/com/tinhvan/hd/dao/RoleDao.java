package com.tinhvan.hd.dao;

import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.bean.RoleListRequest;
import com.tinhvan.hd.bean.RoleListRespon;

import java.util.List;

public interface RoleDao {

//    void insertRole(RoleEntity role);
//
//    void updateRole(RoleEntity role);
//
//    void deleteRole(RoleEntity role);
//
//    RoleEntity getById(Long id);

    RoleEntity findByRole(String role);

    List<RoleEntity> getAll(Integer status);

    RoleListRespon getList(RoleListRequest roleListRequest);
}
