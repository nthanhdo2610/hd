/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
public class SMSTemplateFind implements HDPayload{
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void validatePayload() {
        //validate uuid
        if (uuid == null) {
            throw new BadRequestException(1200, "empty uuid");
        }
    }
    
}
