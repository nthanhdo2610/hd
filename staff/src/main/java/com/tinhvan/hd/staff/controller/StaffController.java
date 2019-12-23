/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.staff.controller;

import com.sun.jndi.ldap.LdapCtxFactory;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.staff.bean.Role;
import com.tinhvan.hd.staff.bean.*;
import com.tinhvan.hd.staff.model.Staff;
import com.tinhvan.hd.staff.payload.FileSizeResponse;
import com.tinhvan.hd.staff.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * @author LUUBI
 */
@RestController
@RequestMapping("api/v1/staff")
public class StaffController extends HDController {

    @Autowired
    StaffService staffService;


    @Value("${app.module.role.service.url}")
    private String url;

    @Value("${app.module.contract.service.url}")
    private String contractUrl;

    /**
     * get file size on system config
     *
     *
     * @return http status code and FileSizeResponse
     */
    @PostMapping(value = "/file_size")
    public ResponseEntity<?> fileSize() {
        Config config = HDConfig.getInstance();
        return ok(new FileSizeResponse(Integer.valueOf(config.get("MAX_FILE_SIZE_UPLOAD_MB")),"MB"));
    }

    /**
     * Update a Staff exist in lending app
     *
     * @param request object StaffSetRole contain information needed update
     *
     * @return http status code and object staff has been updated
     */
    @PostMapping(value = "/set_role")
    public ResponseEntity<?> setRole(@RequestBody RequestDTO<StaffSetRole> request) {
        StaffSetRole staffSetRole = request.init();
        Staff staffResult = staffService.findByUUID(staffSetRole.getUuid());
        if (staffResult != null) {
            int version = staffResult.getObjectVersion();
            String roleName = getRoleName(staffSetRole);
            staffResult.setRoleCode(String.valueOf(staffSetRole.getRole()));
            staffResult.setRoleName(roleName);
            staffResult.setModifiedAt(request.now());
            staffResult.setModifiedBy(request.jwt().getUuid());
            staffResult.setObjectVersion(version + 1);
            staffService.createOrUpdate(staffResult);
            return ok(staffResult);
        }
        return unauthorized(1206, "uuid is not exits");
    }

    /**
     * find a Staff by uuid exist in lending app
     *
     * @param request object StaffFind contain information needed find
     *
     * @return http status code and object staff has been find
     */
    @PostMapping(value = "/find")
    public ResponseEntity<?> find(@RequestBody RequestDTO<StaffFind> request) {
        StaffFind staffFind = request.init();
        Staff staffResult = staffService.findByUUID(staffFind.getUuid());
        if (staffResult != null) {
            return ok(staffResult);
        }
        return unauthorized(1206, "uuid is not exits");
    }

    /**
     * findRoleId(id RoleEntity) a Staff by uuid exist in lending app
     *
     * @param request object StaffFind contain information needed find role id
     * @return http status code and object StaffFindRoleId has been findRoleId
     */
    @PostMapping(value = "/find_role_id")
    public ResponseEntity<?> findRoleIdByUUID(@RequestBody RequestDTO<StaffFind> request) {
        StaffFind staffFind = request.init();
        return ok(new StaffFindRoleId(staffService.findRoleIdByUUID(staffFind.getUuid())));
    }

    /**
     * Get list Staff
     *
     * @param request object StaffSearch contain information needed list
     *
     * @return http status code and object StaffSearchResponse has been list
     */
    @PostMapping(value = "/list")
    public ResponseEntity<?> list(@RequestBody RequestDTO<StaffSearch> request) {
        StaffSearch staffSearch = request.init();
        StaffSearchRespon staffSearchResponse = staffService.search(staffSearch);
        return ok(staffSearchResponse);
    }

    /**
     * check token a Staff exist in lending app
     *
     * @param request object IdPayload contain information needed check token
     *
     * @return http status code
     */
    @PostMapping(value = "/check_token")
    public ResponseEntity<?> checkToken(@RequestBody RequestDTO<IdPayload> request) {

        IdPayload idPayload = request.init();
        String token = (String) idPayload.getId();
        validateToken(token);
        return ok("ok");
    }


    /**
     * signIn(login LDAP) a Staff exist in lending app
     *
     * @param request object StaffSignin contain information needed SignIn
     *
     * @return http status code and StaffSignInResponse
     */
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signIn(@RequestBody RequestDTO<StaffSignin> request) throws Exception {
        StaffSignin staffSignin = request.init();
        String email = staffSignin.getEmail();
        String password = staffSignin.getPassword();
        String[] ou = staffSignin.getOU();
        long time = HDUtil.getUnixTime(request.now());
        String displayName = email;
//        String displayName = validateAccountWithLDap(email, password, ou);

        // ous format example : CN=marketing,OU=Marketing,OU=HCMC,OU=Users,OU=HQ,DC=sgvf,DC=sgcf
//        getSigninLDAP(email,  password, ou);
        if (HDUtil.isNullOrEmpty(displayName)) {
            return unauthorized(1110, "invalid username or password");
        }

        //check email exist db, not then create
        Staff staff = staffService.existEmail(email, 1);
        if (staff == null) {
            staff = new Staff();
            staff.init();
            staff.setEmail(email);
            staff.setFullName(displayName);
            staff.setDepartment(ou[1]);
            staff.setArea(ou[0]);
            staff.setCreatedAt(request.now());
            staff.setRoleCode(String.valueOf(HDConstant.ROLE.STAFF));
            staff.setRoleName("Nhân viên");
        }
        String token = JWTProvider.encode(new JWTPayload(staff.getId(), Integer.parseInt(staff.getRoleCode()), time, HDUtil.getUnixTime(request.now()), request.environment()));
        staff.setStaffToken(token);
        staffService.createOrUpdate(staff);
        return ok(new StaffSigninRespon(token, staff));
    }

    /**
     * validate Account With LDAP invoke contract-service
     *
     * @param email
     * @param password
     * @param ou
     *
     * @return displayName
     */
    public String validateAccountWithLDap(String email, String password, String[] ou) {
        String displayName = "";

        RequestConnectLDap requestConnectLDap = new RequestConnectLDap();
        requestConnectLDap.setEmail(email);
        requestConnectLDap.setPassword(password);
        String ous = getOU(ou, email);
        requestConnectLDap.setOus(ous);
        try {
            Invoker invoker = new Invoker();
            ResponseDTO<Object> dto = invoker.call( contractUrl+ "/ldap_connect", requestConnectLDap, new ParameterizedTypeReference<ResponseDTO<Object>>() {
            });

            if (dto != null && dto.getCode() == 200) {
                displayName = (String) dto.getPayload();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return displayName;
    }
/*
    public String getSigninLDAP(String email, String password, String[] ou) {
//        StaffSigninLDAP signinLDAP = null;
        String serverName = "sgvf.sgcf";
        String port = "389";
        String cn = "cn="+ email+ ",";
        String ous = getOU(ou, email);
        String dc = "dc=sgvf,dc=sgcf";
//        String username = "marketing@hdsaison.co.vn";
        String domainName = "sgvf.sgcf";
//        LDAP_BASE_DN=OU=Users,OU=HQ,dc=mycompany,dc=local
//        String password = "Aa123456@";
        // bind by using the specified username/password
//        String principalName = "cn=luandd@mycompany.local,ou=IT,ou=HCMC,ou=HQ,dc=mycompany,dc=local";
        Hashtable props = new Hashtable();
        String principalName = cn + ous + dc;
        System.out.println("principalName "+principalName);

        props.put(Context.SECURITY_PRINCIPAL, principalName);
        props.put(Context.SECURITY_CREDENTIALS, password);
        DirContext context;
        String displayName = "";
        try {
            context = LdapCtxFactory.getLdapCtxInstance("ldap://" + serverName + ":" + port, props);
            // locate this user's record
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> renum = context.search(toDC(domainName),
                    "(& (userPrincipalName=" + principalName + ")(objectClass=user))", controls);
            if (!renum.hasMore()) {
                serverError(500);
                return displayName;
            }
            SearchResult result = renum.next();
            displayName = result.getAttributes().get("displayName").get(0).toString();
            context.close();
        } catch (AuthenticationException a) {
//            serverError(500, a.getMessage());
            return displayName;
        } catch (NamingException e) {
//            serverError(500, e.getMessage());
            return displayName;
        }
        return displayName;
    }
    private String toDC(String domainName) {
        StringBuilder buf = new StringBuilder();
        for (String token : domainName.split("\\.")) {
            if (token.length() == 0) {
                continue; // defensive check
            }
            if (buf.length() > 0) {
                buf.append(",");
            }
            buf.append("DC=").append(token);
        }
        return buf.toString();
    }
*/

    /**
     * Convert ou to cn
     *
     * @param email
     * @param ou
     *
     * @return result value cn connect LDAP
     */
    private String getOU(String[] ou, String email) {
        int index = email.indexOf('@');
        String em = email.substring(0,index)+",";
        String result = "CN="+em;
        int length = ou.length;
        for (int i = length; i > 0; --i) {
            result += "OU=" + ou[i - 1] + ",";
        }
        result +="OU=Users,OU=HQ,DC=sgvf,DC=sgcf";
        return result;
    }

    /**
     * check validate token
     *
     * @param token
     *
     * @return http  status code, a Staff exist lending app
     */
    private void validateToken(String token) {
        Staff staff = staffService.findByToken(token);
        if (staff == null) {
            throw new UnauthorizedException(401, "unauthorized");
        }
    }

    /**
     * Get role name invoke authorize service
     *
     * @param staffSetRole object contain information needed getRoleName
     *
     * @return http status code, role name
     */
    private String getRoleName(StaffSetRole staffSetRole) {
        //find role on authorize
        Invoker invoker = new Invoker();
        ResponseDTO<Role> rs = invoker.call(url, staffSetRole, new ParameterizedTypeReference<ResponseDTO<Role>>() {
        });
        if (rs.getCode() != 200) {
            throw new BadRequestException(1205, "role is not exits");
        }
        return rs.getPayload().getName();
    }


//    @PostMapping(value = "/list")
//    public ResponseEntity<?> list(@RequestBody RequestDTO<Pagination<StaffFilter>> req) {
//        Pagination<StaffFilter> page = req.init();
//        StaffFilter filter = page.filter(StaffFilter.class);
//        List<Staff> list = staffService.getList(filter);
//        Log.debug(this.getClass().getName() + ": [END] list");
//        return ok(page.response(list));
//    }



//    @PostMapping(value = "/signout")
//    public ResponseEntity<?> signOut(@RequestBody RequestDTO<StaffSignOut> req) {
//        StaffSignOut staffSignOut = req.init();
//        validateToken(req);
//        Staff staffResult = staffService.findByUUID(staffSignOut.getId());
//        if (staffResult != null) {
//            staffResult.setStaffToken("");
//            staffService.createOrUpdate(staffResult);
//            return ok(staffResult);
//        }
//        return unauthorized(1206, "uuid is not exits");
//    }

}
