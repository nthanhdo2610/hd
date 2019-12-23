package com.tinhvan.hd.repository;

import com.tinhvan.hd.base.enities.AuthorizeUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorizeUserRepository extends CrudRepository<AuthorizeUserEntity, Long> {
    List<AuthorizeUserEntity> findAllByStatus(Integer status);
}
