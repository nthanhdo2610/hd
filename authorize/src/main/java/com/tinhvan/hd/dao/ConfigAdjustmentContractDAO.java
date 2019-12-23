package com.tinhvan.hd.dao;

import com.tinhvan.hd.model.ConfigAdjustmentContract;

import java.util.List;

public interface ConfigAdjustmentContractDAO {

//    void create(ConfigAdjustmentContract object);
//
//    void update(ConfigAdjustmentContract object);

    List<ConfigAdjustmentContract> getList();

    List<String> getListByIsCheckDocument();

    ConfigAdjustmentContract getConfigAdjustmentContract(String code);

}
