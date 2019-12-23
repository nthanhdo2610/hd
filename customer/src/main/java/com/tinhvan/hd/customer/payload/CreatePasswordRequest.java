package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class CreatePasswordRequest implements HDPayload {

    private String uuid;
    private String newPassword;
    private String newPasswordRewrite;
    @Override
    public void validatePayload() {
        try {
            UUID.fromString(uuid);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        return "CreatePasswordRequest{" +
                "uuid='" + uuid + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", newPasswordRewrite='" + newPasswordRewrite + '\'' +
                '}';
    }
}
