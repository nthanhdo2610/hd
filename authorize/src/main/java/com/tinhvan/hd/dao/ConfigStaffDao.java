package com.tinhvan.hd.dao;

import com.tinhvan.hd.bean.ConfigStaffListContractTypeRespon;
import com.tinhvan.hd.model.ConfigStaff;

import java.util.List;

public interface ConfigStaffDao {

    void insertOrUpdate(ConfigStaff config);
//
//    List<ConfigStaff> list();

    String getValueByKey(String key);

    List<ConfigStaffListContractTypeRespon> getListByContractType();

//    ConfigStaff findById(int id);
}
