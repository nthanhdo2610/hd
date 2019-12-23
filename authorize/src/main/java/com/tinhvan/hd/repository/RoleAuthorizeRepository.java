package com.tinhvan.hd.repository;

import com.tinhvan.hd.bean.RoleAuthorizeListByRoleIdRespon;
import com.tinhvan.hd.bean.RoleAuthorizeListRespon;
import com.tinhvan.hd.bean.RoleAuthorizeRespon;
import com.tinhvan.hd.model.RoleAuthorize;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleAuthorizeRepository extends CrudRepository<RoleAuthorize, Integer> {
    @Query("SELECT new com.tinhvan.hd.bean.RoleAuthorizeRespon((CASE WHEN ra.id is not null THEN ra.id ELSE 0 END) AS raId ,s.id, s.menu, s.crud, s.action, (CASE WHEN ra.status=1 THEN 1 ELSE 0 END) AS status) " +
            "FROM Services s LEFT JOIN RoleAuthorize ra ON s.id = ra.servicesId AND ra.roleId= :roleId order by s.id")
    List<RoleAuthorizeRespon> listRoleAuthorizeRespon(int roleId);

    Optional<RoleAuthorize> findByRoleIdAndServicesId(int roleId, int serviceId);

    @Query("SELECT new com.tinhvan.hd.bean.RoleAuthorizeListByRoleIdRespon(ra.id as role_authorize_id, s.id as service_id, s.crud, s.entryPoint, s.menu, s.action, s.parent) " +
            "FROM RoleAuthorize ra right join Services s on ra.servicesId = s.id where ra.roleId = :roleId AND ra.status = 1 order by s.idx ASC")
    List<RoleAuthorizeListByRoleIdRespon> getListByRoleId(int roleId);
}
