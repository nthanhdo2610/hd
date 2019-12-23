/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
public class StaffSetRole implements HDPayload {

    private UUID uuid;
    private int role = 0;

    public StaffSetRole() {
    }

    public StaffSetRole(UUID uuid, int role) {
        this.uuid = uuid;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public void validatePayload() {
        //validate uuid
        if (uuid == null) {
            throw new BadRequestException(1200, "empty uuid");
        }
        //validate role
        if (role == 0) {
            throw new BadRequestException(1204, "invalid role");
        }

    }

}
