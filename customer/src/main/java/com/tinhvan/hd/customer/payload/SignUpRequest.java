package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class SignUpRequest implements HDPayload {

//    private String fullName;
    private String username;

    private String phoneNumber;

    private String userNameFm;
//    private String password;

//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserNameFm() {
        return userNameFm;
    }

    public void setUserNameFm(String userNameFm) {
        this.userNameFm = userNameFm;
    }

    //    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(username))
            throw new BadRequestException(1110, "invalid username or password");
//        if (!HDUtil.isNullOrEmpty(fullName)&&fullName.length() > 255)
//            throw new BadRequestException(1100);
        /*if(HDUtil.isNullOrEmpty(password))
            throw new BadRequestException(1110, "invalid username or password");*/
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userNameFm='" + userNameFm + '\'' +
                '}';
    }
}
