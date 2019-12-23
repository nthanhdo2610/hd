package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class NewPasswordFromEmailRequest implements HDPayload {
    private String email;   //check format
    private String token;
    private String newPassword;
    private String newPasswordRewrite;

    @Override
    public void validatePayload() {

        //validate email
        if(!HDUtil.isNullOrEmpty(email)&&!HDUtil.isEmail(email))
            throw new BadRequestException(1110, "invalid email");
        //validate token
        if(HDUtil.isNullOrEmpty(token))
            throw new BadRequestException();

        //validate password
        if(HDUtil.isNullOrEmpty(newPassword))
            throw new BadRequestException(1123);
        if(newPasswordRewrite==null||!newPassword.equals(newPasswordRewrite))
            throw new BadRequestException(1124);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRewrite() {
        return newPasswordRewrite;
    }

    public void setNewPasswordRewrite(String newPasswordRewrite) {
        this.newPasswordRewrite = newPasswordRewrite;
    }

    @Override
    public String toString() {
        return "NewPasswordFromEmailRequest{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", newPasswordRewrite='" + newPasswordRewrite + '\'' +
                '}';
    }
}
