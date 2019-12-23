package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class UpdateDisbursementInfo implements HDPayload {


    private Long id;

    private Integer isSent;

    private String sentMailList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "UpdateDisbursementInfo{" +
                "id=" + id +
                ", isSent=" + isSent +
                ", sentMailList=" + sentMailList +
                '}';
    }

    @Override
    public void validatePayload() {
        if (id == null || id < 1) {
            throw new BadRequestException(1430,"Id is null or empty !");
        }
    }
}
