/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.bean;

import com.tinhvan.hd.base.AESProvider;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

/**
 *
 * @author LUUBI
 */
public class StaffSignin implements HDPayload{

    private String email;
    private String password;
    private String[] OU;

    public StaffSignin(String email, String password, String[] OU) {
        this.email = email;
        this.password = password;
        this.OU = OU;
    }

    public StaffSignin() {
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getOU() {
        return OU;
    }

    public void setOU(String[] OU) {
        this.OU = OU;
    }
    
    

    @Override
    public void validatePayload() {
        //validate email
        if(!HDUtil.isEmail(email))
            throw new BadRequestException(1101, "wrong format email");
        if(HDUtil.isNullOrEmpty(email))
            throw new BadRequestException(1225, "empty email");
        //validate password
        if(HDUtil.isNullOrEmpty(password))
            throw new BadRequestException(1207, "empty password");
        else
            this.password = AESProvider.decrypt(password);
        if(HDUtil.isNullOrEmpty(password))
            throw new BadRequestException(1132, "invalid password");
        //validate ou
        if(OU.length < 2 )
            throw new BadRequestException(1208, "invalid ou");
        
    }

}
