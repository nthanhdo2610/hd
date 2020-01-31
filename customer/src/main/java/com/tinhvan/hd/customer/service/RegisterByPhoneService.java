package com.tinhvan.hd.customer.service;

import com.tinhvan.hd.customer.model.RegisterByPhone;

import java.util.List;

public interface RegisterByPhoneService {

    void saveOrUpdate(RegisterByPhone registerByPhone);

    void saveOrUpdateAll(List<RegisterByPhone> registerByPhones);

    RegisterByPhone getByPhoneAndStatus(String phone,Integer status);

    List<RegisterByPhone> getListByPhone(String phone);
}
