/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.dao;

import com.tinhvan.hd.staff.bean.StaffSearch;
import com.tinhvan.hd.staff.bean.StaffSearchRespon;
import com.tinhvan.hd.staff.filter.StaffFilter;
import com.tinhvan.hd.staff.model.Staff;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
public interface StaffDAO {

//    void createOrUpdate(Staff object);

    Staff existEmail(String email, int status);

    Staff findByUUID(UUID uuid);

    List<Staff> getList(StaffFilter staffFilter);

    StaffSearchRespon search(StaffSearch staffSearch);

    int num(StaffFilter staffFilter);

    //find by uuid return id on table role
    long findRoleIdByUUID(UUID uuid);

    Staff findByToken(String token);
}
