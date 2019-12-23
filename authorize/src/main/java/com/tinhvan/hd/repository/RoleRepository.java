package com.tinhvan.hd.repository;

import com.tinhvan.hd.base.enities.RoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity,Long> {

    List<RoleEntity> findAllByStatus(int status);

    Optional<RoleEntity> findByRoleAndStatus(String role, int status);
    @Query("FROM RoleEntity WHERE status = 1 and (name= :name or role= :role)")
    List<RoleEntity> findAllByRoleOrName(String role, String name);

    List<RoleEntity> findAllByRoleOrNameAndStatus(String role, String name, int status);
@Query("FROM RoleEntity WHERE status = :status and (name= :name or role= :role)")
    List<RoleEntity> testgetList(String role, String name, int status);
//    Optional<RoleEntity> findByRoleOrName(String role, String name);

    Optional<RoleEntity> findByName(String name);
}
