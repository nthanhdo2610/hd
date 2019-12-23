package com.tinhvan.hd.controller;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.enities.AuthorizeUserEntity;
import com.tinhvan.hd.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/authorize")
public class AuthorizeController extends HDController {

    @Autowired
    private AuthorizeService authorizeService;

    /**
     * get list AuthorizeUserEntity exist lending app
     *
     * @param req EmptyPayload
     *
     * @return http status code and List<AuthorizeUserEntity>
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllAuthorize(@RequestBody RequestDTO<EmptyPayload> req){
        List<AuthorizeUserEntity> authorizeUserEntities = new ArrayList<>();
        try {
            authorizeUserEntities= authorizeService.getAll(1);
        }catch (Exception ex) {
            throw new BadRequestException();
        }
        return ok(authorizeUserEntities);
    }

    /**
     * create AuthorizeUserEntity
     *
     * @param req AuthorizeUserEntity contain information create
     *
     * @return http status code
     */
    @PostMapping("/post")
    public ResponseEntity<?> saveAuthorize(@RequestBody RequestDTO<AuthorizeUserEntity> req){

        AuthorizeUserEntity payload = req.getPayload();

        try {
            authorizeService.insertAuthorize(payload);
        }catch (Exception ex){
            throw new BadRequestException();
        }

        return ok(null);
    }

    /**
     * Update AuthorizeUserEntity exist lending app
     *
     * @param req AuthorizeUserEntity contain information update
     *
     * @return http status code
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateAuthorize(@RequestBody RequestDTO<AuthorizeUserEntity> req){

        AuthorizeUserEntity payload = req.init();

        Long id = payload.getId();
        if(id == null || id == 0) {
            throw new BadRequestException(404,"authorize id is empty");
        }

        AuthorizeUserEntity authorize = authorizeService.getById(id);

        if(authorize == null){
            throw new BadRequestException(404,"authorize does not exits");
        }

        try {
            authorizeService.updateAuthorize(payload);
        }catch (Exception ex){
            throw new BadRequestException();
        }

        return ok(null);
    }

    /**
     * Delete AuthorizeUserEntity exist lending app
     *
     * @param req IdPayload contain information delete
     *
     * @return http status code
     */
    @PostMapping("/delete")
    @Transactional
    public  ResponseEntity<?> deleteAuthorize(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload payload = req.getPayload();
        Long id = (Long) payload.getId();

        AuthorizeUserEntity authorize = authorizeService.getById(id);
        if (authorize == null){
            throw new BadRequestException(404,"authorize does not exits");
        }

        try {
//            authorize.setModifiedAt(new Date());
            authorizeService.deleteAuthorize(authorize);
        }catch (Exception ex){
            throw new BadRequestException();
        }
        return ok(null);
    }


}
