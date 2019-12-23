package com.tinhvan.hd.staff.repository;

import com.tinhvan.hd.staff.bean.StaffFindRoleId;
import com.tinhvan.hd.staff.model.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends CrudRepository<Staff,Long> {

    Optional<Staff> findByEmailAndStatus(String email, int status);

    Optional<Staff> findById(UUID id);

    Optional<Staff> findByStaffToken(String token);

    @Query(value="select new com.tinhvan.hd.staff.bean.StaffFindRoleId(r.id) from Staff s inner join RoleEntity r on s.roleCode= r.role and s.id = :id")
    Optional<StaffFindRoleId> findRoleIdByUUID(UUID id);

}
