package com.tinhvan.hd.service;

import com.tinhvan.hd.bean.ConfigAdjustmentContractListIsCheck;
import com.tinhvan.hd.model.ConfigAdjustmentContract;

import java.util.List;

public interface ConfigAdjustmentContractService {

    void create(ConfigAdjustmentContract object);

    void update(ConfigAdjustmentContract object);

    List<ConfigAdjustmentContract> getList(int status);

    List<ConfigAdjustmentContractListIsCheck> getListByIsCheckDocument();

    ConfigAdjustmentContract getConfigAdjustmentContract(String code);

    List<String> findAllByCodeIn(List<String> codes);
}
