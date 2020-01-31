package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class TypeAndContractType implements HDPayload {

    private Integer type;

    private String contractType;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    @Override
    public void validatePayload() {
        if(type==null)
            type = HDConstant.LOAN_TYPE.LOAN_FORM;
        if(HDUtil.isNullOrEmpty(contractType))
            throw new BadRequestException(1227);
    }
}
