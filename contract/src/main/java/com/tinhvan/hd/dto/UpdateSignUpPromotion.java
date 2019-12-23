package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.List;

public class UpdateSignUpPromotion implements HDPayload {

    private String ids;

    private Integer isSent;

    private String sentMailList;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getIsSent() {
        return isSent;
    }

    public void setIsSent(Integer isSent) {
        this.isSent = isSent;
    }

    public String getSentMailList() {
        return sentMailList;
    }

    public void setSentMailList(String sentMailList) {
        this.sentMailList = sentMailList;
    }

    @Override
    public String toString() {
        return "UpdateSignUpPromotion{" +
                "ids=" + ids +
                ", isSent=" + isSent +
                ", sentMailList='" + sentMailList + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(ids)) {
            throw new BadRequestException(1430,"Id is null or empty !");
        }
    }
}
