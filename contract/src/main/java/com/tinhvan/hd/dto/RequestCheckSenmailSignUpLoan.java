package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class RequestCheckSenmailSignUpLoan implements HDPayload {

    private List<ResultSearchSignUpLoan> searchSignUpLoans;

    private Integer type;

    public List<ResultSearchSignUpLoan> getSearchSignUpLoans() {
        return searchSignUpLoans;
    }

    public void setSearchSignUpLoans(List<ResultSearchSignUpLoan> searchSignUpLoans) {
        this.searchSignUpLoans = searchSignUpLoans;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public void validatePayload() {

    }
}
