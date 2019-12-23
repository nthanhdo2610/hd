package com.tinhvan.hd.base.repository;

import com.tinhvan.hd.base.enities.AuthorizeUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorizeUserEntityRepository extends CrudRepository<AuthorizeUserEntity,Long> {

    List<AuthorizeUserEntity> findAllByPath(String path);
}
