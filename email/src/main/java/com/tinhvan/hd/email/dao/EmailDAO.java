/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.email.dao;

import com.tinhvan.hd.email.model.Email;

import java.util.UUID;

/**
 * @author LUUBI
 */
public interface EmailDAO {

//    void create(Email object);
//
//    void update(Email object);

    Email existEmail(String email);

    Email findByUUID(UUID uuid);
}
