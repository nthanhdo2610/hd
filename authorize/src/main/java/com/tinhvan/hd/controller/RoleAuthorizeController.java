package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.base.enities.StaffLogAction;
import com.tinhvan.hd.bean.*;
import com.tinhvan.hd.model.RoleAuthorize;
import com.tinhvan.hd.service.*;
import com.tinhvan.hd.utils.WriteLog;
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
@RequestMapping("/api/v1/role_authorize")
public class RoleAuthorizeController extends HDController {

    @Autowired
    RoleAuthorizeService roleAuthorizeService;

    @Autowired
    ServicesService servicesService;

    @Autowired
    RoleService roleService;

    @Autowired
    private ConfigStaffService configStaffService;

    @Autowired
    WriteLog log;

    private final String logName = "Phân quyền";

    private Invoker invoker = new Invoker();
    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    /**
     * get list RoleAuthorizeListRespon exist lending app
     *
     * @param req IdPayload
     *
     * @return http status code and list RoleAuthorizeListRespon
     */
    @PostMapping("/list")
    public ResponseEntity<?> list(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload idPayload = req.init();
        int roleId = (int) idPayload.getId();
        RoleAuthorizeListRespon roleAuthorizeListRespon = roleAuthorizeService.getlist(roleId);
        //write log
        log.writeLogAction(req, logName, "list", idPayload.getId().toString(), "", roleAuthorizeListRespon.toString(), "", "");
        return ok(roleAuthorizeListRespon);
    }

    /**
     * Update role, roleAuthorize exist lending app
     *
     * @param req RoleAuthorizeUpdateRequest object contain information update
     *
     * @return http status code and RoleAuthorizeListRespon
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<RoleAuthorizeUpdateRequest> req) {
        RoleAuthorizeUpdateRequest roleAuthorizeUpdateRequest = req.init();
        int roleId = roleAuthorizeUpdateRequest.getRoleAuthorizeRequest().getRoleId();
        UUID uuid = null;
        if(req.jwt() != null){
         uuid = req.jwt().getUuid();
        }
        RoleAuthorizeListRespon oldValues = roleAuthorizeService.getlist(roleId);
        //update role
        updateRole(roleAuthorizeUpdateRequest, uuid, req.now());
        //update role authorize
        updateRoleAuthorize(uuid, roleAuthorizeUpdateRequest.getListRoleAuthorizeRespon(), roleId);
        RoleAuthorizeListRespon roleAuthorizeListRespon = roleAuthorizeService.getlist(roleId);
        //write log
        log.writeLogAction(req, logName, "update", roleAuthorizeUpdateRequest.toString(), oldValues.toString(), roleAuthorizeListRespon.toString(), "", "");
        return ok(roleAuthorizeListRespon);
    }

    /**
     * Update role exist lending app
     *
     * @param roleAuthorizeUpdateRequest
     *
     * @return no result
     */
    //update role
    private void updateRole(RoleAuthorizeUpdateRequest roleAuthorizeUpdateRequest, UUID uuid, Date now) {
        RoleAuthorizeRequest roleAuthorizeRequest = roleAuthorizeUpdateRequest.getRoleAuthorizeRequest();
        int roleId = roleAuthorizeRequest.getRoleId();
        String roleName = roleAuthorizeRequest.getRoleName();
        RoleEntity roleEntity = roleService.getById(Long.parseLong("" + roleId));
        RoleEntity roleEntityCheck = roleService.findByName(roleName);
        if (roleEntity != null) {
            if (roleEntityCheck != null && !roleEntity.getName().equals(roleName)) {
                throw new BadRequestException(1251, "name exits");
            }
            roleEntity.setName(roleName);
            roleEntity.setModifiedAt(now);
            roleEntity.setModifiedBy(uuid);
            roleService.updateRole(roleEntity);
        } else {
            throw new BadRequestException(1204, "invalid role");
        }
    }

    /**
     * Update roleAuthorize exist lending app
     *
     * @param list
     * @param roleId
     *
     * @return no result
     */
    //update role authorize
    private void updateRoleAuthorize(UUID uuid, List<RoleAuthorizeRespon> list, int roleId) {
        if (list != null && !list.isEmpty()) {
            for (RoleAuthorizeRespon rar : list) {
                int serviceId = rar.getServicesId();
                RoleAuthorize roleAuthorize = roleAuthorizeService.findByRoleIdAndServiceId(roleId, serviceId);
                if (roleAuthorize == null && rar.getStatus() == 1) {
                    roleAuthorize = new RoleAuthorize();
                    roleAuthorize.setRoleId(roleId);
                    roleAuthorize.setServicesId(serviceId);
                    roleAuthorize.setStatus(rar.getStatus());
                    roleAuthorize.setCreatedAt(new Date());
                    roleAuthorize.setCreateBy(uuid);
                }
                if (roleAuthorize != null) {
                    roleAuthorize.setStatus(rar.getStatus());
                    roleAuthorizeService.insertOrUpdate(roleAuthorize);
                }
            }
        } else {
            throw new BadRequestException(1202, "list empty");
        }
    }

    /**
     * Get RoleAuthorizeListByRoleIdRespon
     *
     * @param req object StaffFind contain information getListByRoleId
     *
     * @return http status code and list RoleAuthorizeListByRoleIdRespon
     */
    @PostMapping("/list_by_role_id")
    public ResponseEntity<?> getListByRoleId(@RequestBody RequestDTO<StaffFind> req) {
        StaffFind staffFind = req.init();
        //call invoke staff
        ResponseDTO<StaffFindRoleId> rs = invoker.call(urlStaffRequest + "/find_role_id", staffFind, new ParameterizedTypeReference<ResponseDTO<StaffFindRoleId>>() {
        });

        if (rs == null || rs.getCode() != 200) {
            throw new BadRequestException(1202, "invoke staff error");
        }
        //result role code --- roleid
        List<RoleAuthorizeListByRoleIdRespon> listResult = roleAuthorizeService.getListByRoleId(rs.getPayload().getRoleId());

        //write log
        log.writeLogAction(req, logName, "list_by_role_id", staffFind.toString(), "", listResult.toString(), "", "");
        return ok(listResult);
    }

//    @PostMapping("/insert-services")
//    public ResponseEntity<?> insertServices(@RequestBody RequestDTO<Services> req) {
//        Services services = req.init();
//        List<Services> list = servicesService.list();
//        String action = "Danh sách,Phân quyền,Danh sách,Xem hợp đồng ,Khóa tài khoản,Reset mật khẩu,Danh sách,Chi tiết hợp đồng,Kiểm tra hồ sơ,Sửa phụ lục,Kiểm tra hồ sơ,Sửa phụ lục,Danh sách,Thêm,Sửa,Xóa,Danh sách,Thêm,Sửa,Xóa,Xem,Sửa,Xem,Sửa,Danh sách,Thêm,Sửa,Danh sách,Thêm,Sửa,Danh sách,Sửa,Danh sách,Thêm,Sửa,Xóa,";
//        String[] arr = action.split(",");
//        for (int i = 0; i < arr.length; i++) {
//            Services s =  list.get(i);
//            s.setAction(arr[i]);
//        servicesService.update(s);
//        }
//
//
//        return ok("ok");
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<?> update(@RequestBody RequestDTO<RoleAuthorizeUpdate> req) {
//        RoleAuthorizeUpdate roleAuthorizeUpdate = req.getPayload();
//        RoleAuthorize roleAuthorize = roleAuthorizeService.find(roleAuthorizeUpdate.getId());
//        if (roleAuthorize != null) {
//            roleAuthorize.setRoleId(roleAuthorizeUpdate.getRoleId());
//            roleAuthorize.setServicesId(roleAuthorizeUpdate.getServicesId());
//            roleAuthorizeService.insertOrUpdate(roleAuthorize);
//            return ok(roleAuthorizeUpdate);
//        }
//        return unauthorized();
//    }


}
