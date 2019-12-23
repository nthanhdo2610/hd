package com.tinhvan.hd.service;

import com.tinhvan.hd.base.enities.StaffLogAction;
import com.tinhvan.hd.bean.ConfigStaffListContractTypeRespon;
import com.tinhvan.hd.model.ConfigStaff;

import java.util.List;

public interface ConfigStaffService {

    void insertOrUpdate(ConfigStaff config);

    List<ConfigStaff> list();

    String getValueByKey(String key);

    List<ConfigStaffListContractTypeRespon> getListByContractType();

    ConfigStaff findById(int id);

    void createMQ(StaffLogAction object);
}
