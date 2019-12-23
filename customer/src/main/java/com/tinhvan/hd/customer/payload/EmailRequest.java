package com.tinhvan.hd.customer.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class EmailRequest implements HDPayload {

    private String email;   //check format

    @Override
    public void validatePayload() {
        //validate identity_card

        //validate email
        if(!HDUtil.isNullOrEmpty(email)&&!HDUtil.isEmail(email))
            throw new BadRequestException(1101);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}
