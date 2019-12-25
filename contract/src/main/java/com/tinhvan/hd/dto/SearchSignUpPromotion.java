package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.Date;

public class SearchSignUpPromotion implements HDPayload {

    private Date fromDate;

    private Date toDate;

    private String promotionCode;

    private String promotionType;

    private String provinceCode;

    private String districtCode;

    private Integer isSent;

    private int pageNum = 1;

    private int pageSize = 10;


    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }


    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public Integer getIsSent() {
        return isSent;
    }

    public void setIsSent(Integer isSent) {
        this.isSent = isSent;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    @Override
    public String toString() {
        return "SearchSignUpPromotion{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", promotionCode='" + promotionCode + '\'' +
                ", promotionType='" + promotionType + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", isSent=" + isSent +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    @Override
    public void validatePayload() {

    }
}
