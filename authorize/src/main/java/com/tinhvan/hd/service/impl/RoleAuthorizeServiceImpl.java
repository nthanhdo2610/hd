package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.bean.RoleAuthorizeListByRoleIdRespon;
import com.tinhvan.hd.bean.RoleAuthorizeListRespon;
import com.tinhvan.hd.dao.RoleAuthorizeDao;
import com.tinhvan.hd.model.RoleAuthorize;
import com.tinhvan.hd.repository.RoleAuthorizeRepository;
import com.tinhvan.hd.service.RoleAuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleAuthorizeServiceImpl implements RoleAuthorizeService {

//    @Autowired
//    RoleAuthorizeDao roleAuthorizeDao;
//    @Autowired
//    RoleDao roleDao;

    @Autowired
    private RoleAuthorizeRepository roleAuthorizeRepository;

    @Override
    public void insertOrUpdate(RoleAuthorize object) {
        roleAuthorizeRepository.save(object);
    }

    @Override
    public void deleteRole() {
        roleAuthorizeRepository.deleteAll();
    }

    @Override
    public RoleAuthorize find(int id) {
        return roleAuthorizeRepository.findById(id).orElse(null);
    }

    @Override
    public RoleAuthorizeListRespon getlist(int roleId) {
//        return roleAuthorizeDao.getlist(roleId);
        return new RoleAuthorizeListRespon(roleId, roleAuthorizeRepository.listRoleAuthorizeRespon(roleId));
    }

    @Override
    public RoleAuthorize findByRoleIdAndServiceId(int roleId, int serviceId) {
//        return roleAuthorizeDao.findByRoleIdAndServiceId(roleId, serviceId);
        return roleAuthorizeRepository.findByRoleIdAndServicesId(roleId, serviceId).orElse(null);
    }

    @Override
    public List<RoleAuthorizeListByRoleIdRespon> getListByRoleId(int roleId) {
//        return roleAuthorizeDao.getListByRoleId(roleId);
        return roleAuthorizeRepository.getListByRoleId(roleId);
    }
}
