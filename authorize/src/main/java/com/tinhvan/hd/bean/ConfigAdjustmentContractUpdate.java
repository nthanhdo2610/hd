package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.model.ConfigAdjustmentContract;

import java.util.List;

public class ConfigAdjustmentContractUpdate implements HDPayload {

    private List<ConfigAdjustmentContract> list;

    public List<ConfigAdjustmentContract> getList() {
        return list;
    }

    public void setList(List<ConfigAdjustmentContract> list) {
        this.list = list;
    }

    @Override
    public void validatePayload() {
        if(list == null || list.isEmpty()){
            throw new BadRequestException(1202, "list is empty");
        }



    }
}
