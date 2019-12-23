package com.tinhvan.hd.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.vo.Pagination;

import java.util.UUID;

public class NotificationSearchRequest implements HDPayload {

    private UUID customerUuid;

    private Integer type;

    private Integer pageNum;

    private Integer pageSize;

    @Override
    public void validatePayload() {
        if (pageNum == null || pageNum < 0) {
            pageNum = 1;
        }

        if (pageSize == null) {
            pageSize = 10;
        }
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }
}
