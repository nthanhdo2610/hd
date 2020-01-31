package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.bean.*;
import com.tinhvan.hd.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController extends HDController {

    @Autowired
    private RoleService roleService;

    private Invoker invoker = new Invoker();
    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    /**
     * get list RoleEntity by status = 1 exist lending app
     *
     * @param req EmptyPayload
     * @return http status code and list RoleEntity
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllRole(@RequestBody RequestDTO<EmptyPayload> req) {
        return ok(roleService.getAll(1));
    }

    /**
     * get list RoleListRespon by status = 1 exist lending app
     *
     * @param req object RoleListRequest contain information pagination
     * @return http status code and list RoleListRespon
     */
    @PostMapping("/pagination")
    public ResponseEntity<?> pagination(@RequestBody RequestDTO<RoleListRequest> req) {
        RoleListRequest roleListRequest = req.init();
        RoleListRespon result = roleService.getList(roleListRequest);
        return ok(result);
    }

    /**
     * Update RoleEntity set status = 0 exist lending app
     *
     * @param req object IdPayload contain information delete
     * @return http status code and RoleEntity
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload idPayload = req.init();
        RoleEntity roleEntity = roleService.getById(Long.parseLong(idPayload.getId().toString()));
        if (roleEntity != null) {
            roleEntity.setStatus(0);
            roleEntity.setModifiedAt(req.now());
            roleEntity.setModifiedBy(req.jwt().getUuid());
            roleService.updateRole(roleEntity);
            //b2 update Staff
            getListUpdateStaff(roleEntity.getRole());
            return ok(roleEntity);
        }
        throw new BadRequestException(1205, "role is not exits");
    }

    /**
     * Create RoleEntity set status = 0 exist lending app
     *
     * @param req object RolePost contain information saveAuthorize
     * @return http status code and RoleEntity
     */
    @PostMapping("/post")
    public ResponseEntity<?> saveAuthorize(@RequestBody RequestDTO<RolePost> req) {

        RolePost rolePost = req.init();
        List<RoleEntity> listRoleEntityCheck = roleService.findAllByRoleOrName(rolePost.getRole(), rolePost.getName());
        if (listRoleEntityCheck != null && !listRoleEntityCheck.isEmpty()) {
            throw new BadRequestException(1251, "role or name exits");
        }

        Date now = req.now();
        UUID uuid = null;
        if (req.jwt() != null) {
            uuid = req.jwt().getUuid();
        }
        RoleEntity roleEntity = new RoleEntity();
        try {
            roleEntity.setName(rolePost.getName());
            roleEntity.setRole(rolePost.getRole());
            roleEntity.setCreatedAt(now);
            roleEntity.setCreatedBy(uuid);
            roleEntity.setModifiedAt(now);
            roleEntity.setModifiedBy(uuid);
            roleEntity.setStatus(1);
            roleService.insertRole(roleEntity);
        } catch (Exception ex) {
            throw new BadRequestException();
        }

        return ok(roleEntity);
    }

    /**
     * Create RoleEntity set status = 0 exist lending app
     *
     * @param req object RolePost contain information saveAuthorize
     * @return http status code and RoleEntity
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateRole(@RequestBody RequestDTO<RoleUpdate> req) {

        RoleUpdate roleUpdate = req.init();
        List<RoleEntity> listRoleEntityCheck = roleService.findAllByRoleOrName(roleUpdate.getRole(), roleUpdate.getName());
        if (listRoleEntityCheck != null && !listRoleEntityCheck.isEmpty()) {
            for (RoleEntity re : listRoleEntityCheck) {
                if (re.getId() != roleUpdate.getId()) {
                    throw new BadRequestException(1251, "role or name exits");
                }
            }
        }
        RoleEntity roleEntity = roleService.getById(roleUpdate.getId());
        if (roleEntity != null) {
            roleEntity.setName(roleUpdate.getName());
            roleEntity.setRole(roleUpdate.getRole());
            roleEntity.setModifiedAt(req.now());
            roleEntity.setModifiedBy(req.jwt().getUuid());
            roleService.updateRole(roleEntity);
            return ok(roleEntity);
        }
        throw new BadRequestException(1205, "role is not exits");
    }

    /**
     * Find RoleEntity exist lending app
     *
     * @param req object RoleFind contain information findByRole
     * @return http status code and RoleEntity
     */
    @PostMapping("/find")
    public ResponseEntity<?> findByRole(@RequestBody RequestDTO<RoleFind> req) {
        RoleFind roleFind = req.init();
        RoleEntity roleEntity = roleService.findByRole(roleFind.getRole());
        if (roleEntity == null) {
            throw new BadRequestException(1205, "role is not exits");
        }
        return ok(roleEntity);
    }

    //invoke staff
    /**
     * Invoke Staff get list update roleCode and roleName
     *
     * @param roleCode
     *
     * @return http status code
     */
    private void getListUpdateStaff(String roleCode) {
        try {
            IdPayload idPayload = new IdPayload();
            idPayload.setId(roleCode);
            ResponseDTO<String> rs = invoker.call(urlStaffRequest + "/list_update", idPayload, new ParameterizedTypeReference<ResponseDTO<String>>() {
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
