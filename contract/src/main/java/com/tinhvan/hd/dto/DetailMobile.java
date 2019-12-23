package com.tinhvan.hd.dto;

import java.util.List;

public class DetailMobile {

    private ContractInfo contract;

    private List<PaymentInformation> historyPayments;

    private List<String> attachments;

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }
    public ContractInfo getContract() {
        return contract;
    }

    public void setContract(ContractInfo contract) {
        this.contract = contract;
    }

    public List<PaymentInformation> getHistoryPayments() {
        return historyPayments;
    }

    public void setHistoryPayments(List<PaymentInformation> historyPayments) {
        this.historyPayments = historyPayments;
    }
}
