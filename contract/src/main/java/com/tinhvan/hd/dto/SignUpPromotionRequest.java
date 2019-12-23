package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.math.BigDecimal;
import java.util.UUID;

public class SignUpPromotionRequest implements HDPayload {

    private String fullName;
    private String phone;
    private String provinceCode;
    private String districtCode;
    private String promotionCode;
    private String promotionType; //ED, MC, CL, CLO
    private String promotionId;
    private String title;
    private String customerUuid;

    @Override
    public void validatePayload() {
        System.out.println(toString());
        if (HDUtil.isNullOrEmpty(fullName))
            throw new BadRequestException(1203);
        if (fullName.length() > 255)
            throw new BadRequestException(1100);
        if (!HDUtil.isPhoneNumber(phone))
            throw new BadRequestException(1237);
        if (HDUtil.isNullOrEmpty(promotionId)) {
            throw new BadRequestException(1117, "Promotion is not exist");
        } else {
            try {
                UUID.fromString(promotionId);
            } catch (Exception e) {
                throw new BadRequestException(1117, "Promotion is not exist");
            }
        }
        if (!HDUtil.isNullOrEmpty(customerUuid)) {
            try {
                UUID.fromString(customerUuid);
            } catch (Exception e) {
                throw new BadRequestException(1106, "invalid customer uuid");
            }
        }
    }

    @Override
    public String toString() {
        return "SignUpPromotionRequest{" +
                "fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", promotionCode='" + promotionCode + '\'' +
                ", promotionType='" + promotionType + '\'' +
                ", promotionId='" + promotionId + '\'' +
                ", title='" + title + '\'' +
                ", customerUuid='" + customerUuid + '\'' +
                '}';
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }
}
