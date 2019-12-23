package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.bean.RoleListRequest;
import com.tinhvan.hd.bean.RoleListRespon;
import com.tinhvan.hd.dao.RoleDao;
import com.tinhvan.hd.repository.RoleRepository;
import com.tinhvan.hd.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void insertRole(RoleEntity role) {
        roleRepository.save(role);
    }

    @Override
    public void updateRole(RoleEntity role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(RoleEntity role) {
        roleRepository.delete(role);
    }

    @Override
    public RoleEntity getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public RoleEntity findByRole(String role) {
//        return roleDao.findByRole(role);
        return roleRepository.findByRoleAndStatus(role, 1).orElse(null);
    }

    @Override
    public List<RoleEntity> getAll(Integer status) {
//        return roleDao.getAll(1);
        return roleRepository.findAllByStatus(status);
    }

    @Override
    public RoleListRespon getList(RoleListRequest roleListRequest) {
        return roleDao.getList(roleListRequest);
    }

    @Override
    public List<RoleEntity> findAllByRoleOrName(String role, String name) {
        return roleRepository.findAllByRoleOrName(role, name);
    }

    @Override
    public List<RoleEntity> findAllByRoleOrNameAndStatus(String role, String name, int status) {
        return roleRepository.findAllByRoleOrNameAndStatus(role, name,status);
    }

    @Override
    public RoleEntity findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }
}
