package com.tinhvan.hd.dto;

import java.math.BigInteger;

public class DashBoardContract {

    private Long totalContract;

    private Integer totalWaitingSign;

    private BigInteger totalOverDuePayment;

    private Integer totalSignUpLoan;

    public Long getTotalContract() {
        return totalContract;
    }

    public void setTotalContract(Long totalContract) {
        this.totalContract = totalContract;
    }

    public Integer getTotalWaitingSign() {
        return totalWaitingSign;
    }

    public void setTotalWaitingSign(Integer totalWaitingSign) {
        this.totalWaitingSign = totalWaitingSign;
    }

    public BigInteger getTotalOverDuePayment() {
        return totalOverDuePayment;
    }

    public Integer getTotalSignUpLoan() {
        return totalSignUpLoan;
    }

    public void setTotalSignUpLoan(Integer totalSignUpLoan) {
        this.totalSignUpLoan = totalSignUpLoan;
    }

    public void setTotalOverDuePayment(BigInteger totalOverDuePayment) {
        this.totalOverDuePayment = totalOverDuePayment;
    }

}
