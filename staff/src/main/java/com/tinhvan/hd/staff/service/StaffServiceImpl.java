/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.service;

import com.tinhvan.hd.staff.bean.StaffFindRoleId;
import com.tinhvan.hd.staff.bean.StaffSearch;
import com.tinhvan.hd.staff.bean.StaffSearchRespon;
import com.tinhvan.hd.staff.dao.StaffDAO;
import com.tinhvan.hd.staff.filter.StaffFilter;
import com.tinhvan.hd.staff.model.Staff;
import com.tinhvan.hd.staff.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author LUUBI
 */
@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class StaffServiceImpl implements StaffService {

    @Autowired
    StaffDAO staffDAO;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public void createOrUpdate(Staff object) {
        staffRepository.save(object);
    }

    @Override
    public Staff existEmail(String email, int status) {
//        return staffDAO.existEmail(email, status);
        return staffRepository.findByEmailAndStatus(email, status).orElse(null);
    }

    @Override
    public Staff findByUUID(UUID uuid) {
//        return staffDAO.findByUUID(uuid);
        return staffRepository.findById(uuid).orElse(null);
    }

    @Override
    public List<Staff> getList(StaffFilter staffFilter) {
        return staffDAO.getList(staffFilter);
    }

    @Override
    public StaffSearchRespon search(StaffSearch staffSearch) {
        return staffDAO.search(staffSearch);
    }

    @Override
    public int num(StaffFilter staffFilter) {
        return staffDAO.num(staffFilter);
    }

    @Override
    public long findRoleIdByUUID(UUID uuid) {
//        return staffDAO.findRoleIdByUUID(uuid);
        StaffFindRoleId staffFindRoleId = staffRepository.findRoleIdByUUID(uuid).orElse(null);
        if (staffFindRoleId != null)
            return staffFindRoleId.getRoleId();
        return 0;
    }

    @Override
    public Staff findByToken(String token) {
//        return staffDAO.findByToken(token);
        return staffRepository.findByStaffToken(token).orElse(null);
    }


}
