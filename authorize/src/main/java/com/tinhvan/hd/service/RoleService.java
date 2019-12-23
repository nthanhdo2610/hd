package com.tinhvan.hd.service;

import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.bean.RoleListRequest;
import com.tinhvan.hd.bean.RoleListRespon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

    void insertRole(RoleEntity role);

    void updateRole(RoleEntity role);

    void deleteRole(RoleEntity role);

    RoleEntity getById(Long id);

    RoleEntity findByRole(String role);

    List<RoleEntity> getAll(Integer status);

    RoleListRespon getList(RoleListRequest roleListRequest);

    List<RoleEntity> findAllByRoleOrName(String role, String name);

    List<RoleEntity> findAllByRoleOrNameAndStatus(String role, String name, int status);

    RoleEntity findByName(String name);
}
