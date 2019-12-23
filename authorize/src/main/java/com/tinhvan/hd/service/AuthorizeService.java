package com.tinhvan.hd.service;

import com.tinhvan.hd.base.enities.AuthorizeUserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorizeService {

    void insertAuthorize(AuthorizeUserEntity authorize);

    void updateAuthorize(AuthorizeUserEntity authorize);

    void deleteAuthorize(AuthorizeUserEntity authorize);

    AuthorizeUserEntity getById(Long id);

    List<AuthorizeUserEntity> getAll(Integer status);
}
