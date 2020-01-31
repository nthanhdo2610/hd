package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.base.HDPayload;

import javax.persistence.*;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

public class CustomerLogAction implements HDPayload {

    private UUID customerId;

    private String action;

    private String contractCode;

    private String para;

    private String objectName;

    private String valueOld;

    private String valueNew;

    private String device;

    private String type;

    private UUID createdBy;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getValueOld() {
        return valueOld;
    }

    public void setValueOld(String valueOld) {
        this.valueOld = valueOld;
    }

    public String getValueNew() {
        return valueNew;
    }

    public void setValueNew(String valueNew) {
        this.valueNew = valueNew;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public String getType() {
        return type;
    }

    public void setTypeResent(String type){
        this.type = type;
    }

    public void setType(String typeOTP, String codeOTP, String phoneNumber, int type) {
        String resultType = "";
        switch (typeOTP) {
            case "AccVeri":
                resultType = "register";
                //getotp
                if (type == 0) {
                    this.setObjectName("Gửi mã xác thực");
                    this.setAction(setActionRegisterAndEsign(contractCode, codeOTP, phoneNumber));
                    break;
                }
                //verify otp none check otp
                if (type == 1) {
                    this.setAction("Khách hàng nhập mã xác thực " + codeOTP);
                    this.setObjectName("Nhập mã xác thực");
                    break;
                }
                //verify otp check otp
                this.setAction("Đúng mã xác thực " + codeOTP);
                this.setObjectName("Kiểm tra mã xác thực");
                break;
            case "SignOTP":
                resultType = "esign";
                //getotp
                if (type == 0) {
                    this.setObjectName("Gửi mã xác thực");
                    this.setAction(setActionRegisterAndEsign(contractCode, codeOTP, phoneNumber));
                    break;
                }
                //verify otp none check otp
                if (type == 1) {
                    this.setAction("Khách hàng nhập mã xác thực " + codeOTP);
                    this.setObjectName("Nhập mã xác thực");
                    break;
                }
                //verify otp check otp
                this.setAction("Đúng mã xác thực " + codeOTP);
                this.setObjectName("Kiểm tra mã xác thực");
                break;
            case "AccResPass":
                resultType = "change_pass";
                //getotp
                if (type == 0) {
                    this.setObjectName("Gửi mã xác thực thay đổi mật khẩu");
                    this.setAction(setActionChangePass(codeOTP, phoneNumber));
                    break;
                }
                //verify otp none check otp
                if (type == 1) {
                    this.setObjectName("Nhập mã xác thực thay đổi mật khẩu");
                    this.setAction("Khách hàng nhập mã xác thực " + codeOTP);
                    break;
                }
                //verify otp check otp
                this.setObjectName("Xác thực đúng mã OTP");
                this.setAction("đúng mã xác thực " + codeOTP);
                break;
            default:
                break;
        }
        this.type = resultType;
    }


    public void setGetOtp(SMSGetOTP smsGetOTP, String codeOTP, String phoneNumber, String environment) {
        UUID customerId = smsGetOTP.getCustomerUUID();
        this.setContractCode(smsGetOTP.getContractCode());
        this.setType(smsGetOTP.getOtpType(), codeOTP, phoneNumber, 0);
        this.setDevice(environment);
        this.setCustomerId(customerId);
        this.setCreatedBy(customerId);
        this.setPara(smsGetOTP.toString());
    }

    public void setGetOtpRegisterByPhone(GetOtpByRegisterPhone smsGetOTP, String codeOTP, String phoneNumber, String environment) {

        this.setContractCode(null);
        this.setType(smsGetOTP.getOtpType(), codeOTP, phoneNumber, 0);
        this.setDevice(environment);
        this.setCustomerId(null);
        this.setCreatedBy(null);
        this.setPara(smsGetOTP.toString());
    }

    public void setSMSVerifyOTP(SMSVerifyOTP smsVerifyOTP, String environment, int type) {
        UUID customerId = smsVerifyOTP.getCustomerUUID();
        this.setContractCode(smsVerifyOTP.getContractCode());
        this.setType(smsVerifyOTP.getOtpType(), smsVerifyOTP.getCodeOTP(), "", type);
        this.setCustomerId(customerId);
        this.setCreatedBy(customerId);
        this.setDevice(environment);
        this.setPara(smsVerifyOTP.toString());
    }

    public void setSMSVerifyOTPRegisterByPhone(VerifyOTPRegisterByPhone smsVerifyOTP, String environment, int type) {
        this.setContractCode(null);
        this.setType(smsVerifyOTP.getOtpType(), smsVerifyOTP.getCodeOTP(), "", type);
        this.setCustomerId(customerId);
        this.setCreatedBy(customerId);
        this.setDevice(environment);
        this.setPara(smsVerifyOTP.toString());
    }

    private String setActionRegisterAndEsign(String contractcode, String codeOTP, String phoneNumber) {
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống HDSaison gửi mã xác thực");
        joiner.add("- Mã số Hợp đồng tham chiếu: " + contractcode);
        joiner.add("- Mã xác thực: " + codeOTP);
        joiner.add("- Số điện thoại nhận mã xác thực: " + phoneNumber);
        return joiner.toString();
    }

    private String setActionChangePass(String codeOTP, String phoneNumber) {
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Hệ thống gửi mã xác thực (OTP)");
        joiner.add("Số điện thoại nhận mã xác thực: " + phoneNumber);
        joiner.add("Mã xác thực: " + codeOTP);
        return joiner.toString();
    }

    @Override
    public void validatePayload() {

    }

}
