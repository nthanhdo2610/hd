package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class UpdatePasswordRequest implements HDPayload {

    private String uuid;
    private String currentPassword;
    private String newPassword;
    private String newPasswordRewrite;

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(uuid))
            throw new BadRequestException(1106, "invalid id");
        try {
            UUID.fromString(this.uuid);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
       /* if(HDUtil.isNullOrEmpty(newPassword))
            throw new BadRequestException(1123);
        if(newPasswordRewrite==null||!newPassword.equals(newPasswordRewrite))
            throw new BadRequestException(1124);*/
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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
        return "UpdatePasswordRequest{" +
                "uuid='" + uuid + '\'' +
                ", currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", newPasswordRewrite='" + newPasswordRewrite + '\'' +
                '}';
    }
}
