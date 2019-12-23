package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.base.enities.AuthorizeUserEntity;
import com.tinhvan.hd.repository.AuthorizeUserRepository;
import com.tinhvan.hd.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.tinhvan.hd.dao.AuthorizeDao;

import java.util.List;


@Service
public class AuthorizeServiceImpl implements AuthorizeService {

//    @Autowired
//    private AuthorizeDao authorizeDao;

    @Autowired
    private AuthorizeUserRepository authorizeUserRepository;

    @Override
    public void insertAuthorize(AuthorizeUserEntity authorize) {
        authorizeUserRepository.save(authorize);
    }

    @Override
    public void updateAuthorize(AuthorizeUserEntity authorize) {
        authorizeUserRepository.save(authorize);
    }

    @Override
    public void deleteAuthorize(AuthorizeUserEntity authorize) {
        authorizeUserRepository.delete(authorize);
    }

    @Override
    public AuthorizeUserEntity getById(Long id) {
        return authorizeUserRepository.findById(id).orElse(null);
    }

    @Override
    public List<AuthorizeUserEntity> getAll(Integer status) {
        return authorizeUserRepository.findAllByStatus(status);
    }
}
