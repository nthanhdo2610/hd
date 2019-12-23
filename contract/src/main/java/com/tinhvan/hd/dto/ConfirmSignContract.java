package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class ConfirmSignContract implements HDPayload {

    private UpdateMonthlyDueDate updateMonthlyDueDate;

    private UpdateChassisNoAndEnginerNo updateChassisNoAndEnginerNo;

    private DisbursementInfo disbursementInfo;

    public UpdateMonthlyDueDate getUpdateMonthlyDueDate() {
        return updateMonthlyDueDate;
    }

    public void setUpdateMonthlyDueDate(UpdateMonthlyDueDate updateMonthlyDueDate) {
        this.updateMonthlyDueDate = updateMonthlyDueDate;
    }

    public UpdateChassisNoAndEnginerNo getUpdateChassisNoAndEnginerNo() {
        return updateChassisNoAndEnginerNo;
    }

    public void setUpdateChassisNoAndEnginerNo(UpdateChassisNoAndEnginerNo updateChassisNoAndEnginerNo) {
        this.updateChassisNoAndEnginerNo = updateChassisNoAndEnginerNo;
    }

    public DisbursementInfo getDisbursementInfo() {
        return disbursementInfo;
    }

    public void setDisbursementInfo(DisbursementInfo disbursementInfo) {
        this.disbursementInfo = disbursementInfo;
    }

    @Override
    public void validatePayload() {

    }
}
