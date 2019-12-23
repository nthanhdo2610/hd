package com.tinhvan.hd.dao;

import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.model.Services;

import java.util.List;

public interface ServicesDao {

    void insertServices(Services object);

    void update(Services object);

    List<Services> list();

}
