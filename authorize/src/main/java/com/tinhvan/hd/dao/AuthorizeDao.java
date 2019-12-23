package com.tinhvan.hd.dao;

import com.tinhvan.hd.base.enities.AuthorizeUserEntity;

import java.util.List;

public interface AuthorizeDao {

//    void insertAuthorize(AuthorizeUserEntity authorize);
//
//    void updateAuthorize(AuthorizeUserEntity authorize);
//
//    void deleteAuthorize(AuthorizeUserEntity authorize);
//
//    AuthorizeUserEntity getById(Long id);

    List<AuthorizeUserEntity> getAll(Integer status);
}
