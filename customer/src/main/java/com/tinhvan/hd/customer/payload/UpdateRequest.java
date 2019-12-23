package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class UpdateRequest implements HDPayload {

    private String uuid;
    private String fullName;
    private String email;
//    private String permanentAddress;
    private int objectVersion;

    @Override
    public void validatePayload() {
        try {
            UUID.fromString(uuid);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
        //validate fullName
        if (!HDUtil.isNullOrEmpty(fullName)&&fullName.length() > 255)
            throw new BadRequestException(1100);

        //validate email
        if (!HDUtil.isNullOrEmpty(email) && !HDUtil.isEmail(email))
            throw new BadRequestException(1101);

        //validate phone_number
        /*if (!HDUtil.isPhoneNumber(phoneNumber))
            throw new BadRequestException(1102);*/

        //validate address
//        if (!HDUtil.isNullOrEmpty(permanentAddress)&&permanentAddress.length() > 255)
//            throw new BadRequestException(1103);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getObjectVersion() {
        return objectVersion;
    }

    public void setObjectVersion(int objectVersion) {
        this.objectVersion = objectVersion;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", objectVersion=" + objectVersion +
                '}';
    }
}
