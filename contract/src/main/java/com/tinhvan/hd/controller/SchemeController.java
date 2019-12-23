package com.tinhvan.hd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.file.FileS3DTORequest;
import com.tinhvan.hd.base.file.FileS3DTOResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.dto.SchemePost;
import com.tinhvan.hd.entity.ContractAdjustmentUploadFile;
import com.tinhvan.hd.entity.Scheme;
import com.tinhvan.hd.file.service.FileStorageService;
import com.tinhvan.hd.service.SchemeService;
import com.tinhvan.hd.utils.ContractUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/scheme")
public class SchemeController extends HDController {

    @Autowired
    private SchemeService schemeService;

    /**
     * Find list scheme
     *
     * @param req
     * @return list of scheme
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllScheme(@RequestBody RequestDTO<EmptyPayload> req) {

        return ok(schemeService.getAll());
    }

//    @PostMapping("/pagination")
//    public ResponseEntity<?> pagination(@RequestBody RequestDTO<RoleListRequest> req) {
//        RoleListRequest roleListRequest = req.init();
//        List<RoleListRespon> list = schemeService.getList(roleListRequest);
//        return ok(list);
//    }

    /**
     * Create a new scheme
     *
     * @param req SchemePost contain info request to create a new scheme
     * @return Scheme after created successfully
     */
    @PostMapping("/post")
    @Transactional
    public ResponseEntity<?> saveScheme(@RequestBody RequestDTO<SchemePost> req) {


        SchemePost schemePost = req.getPayload();
        Scheme scheme = new Scheme();
        try {
            scheme.setSchemeName(schemePost.getSchemeName());
            scheme.setSchemeValue(schemePost.getSchemeValue());
            scheme.setCreatedAt(req.now());
            scheme.setCreatedBy(req.jwt().getUuid());
            scheme.setFileLink(schemePost.getFileLink());
            schemeService.insertScheme(scheme);
            if (!invokeFileHandlerS3_upload(scheme, scheme.getFileLink(), "")) {
                return serverError(1125, scheme);
            }
        } catch (Exception ex) {
            throw new BadRequestException();
        }

        return ok(scheme);
    }

    /**
     * Update a scheme exist
     *
     * @param req SchemePost contain info request to update scheme
     * @return Scheme after update successfully
     */
    @PostMapping("/update")
    @Transactional
    public ResponseEntity<?> updateRole(@RequestBody RequestDTO<SchemePost> req) {

        SchemePost schemePost = req.getPayload();
        Scheme scheme = schemeService.getById(schemePost.getId());
        if (scheme == null)
            throw new BadRequestException(1205, "scheme is not exits");
        String fileOld = scheme.getFileLink();
        scheme.setFileLink(schemePost.getFileLink());
        scheme.setSchemeValue(schemePost.getSchemeValue());
        scheme.setSchemeName(schemePost.getSchemeName());
        scheme.setModifiedAt(req.now());
        scheme.setModifiedBy(req.jwt().getUuid());
        schemeService.updateScheme(scheme);
        if (HDUtil.isNullOrEmpty(fileOld)) {
            if (!invokeFileHandlerS3_upload(scheme, schemePost.getFileLink(), "")) {
                return serverError(1125, scheme);
            }
        }

        if (!HDUtil.isNullOrEmpty(fileOld) && !HDUtil.isNullOrEmpty(schemePost.getFileLink()) && !fileOld.equals(schemePost.getFileLink())) {
            if (!invokeFileHandlerS3_upload(scheme, schemePost.getFileLink(), fileOld)) {
                return serverError(1125, scheme);
            }
        }
        return ok(scheme);
    }

    /**
     * Delete a scheme exist
     *
     * @param req IdPayload contain scheme id
     * @return http status code
     */
    @PostMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteScheme(@RequestBody RequestDTO<IdPayload> req) {


        IdPayload payload = req.getPayload();
        Integer id = (Integer) payload.getId();

        Scheme scheme = schemeService.getById(Long.valueOf(id));
        if (scheme == null) {
            throw new BadRequestException(404, "scheme does not exits");
        }

        try {
//            authorize.setModifiedAt(new Date());
            schemeService.deleteScheme(scheme);
        } catch (Exception ex) {
            throw new BadRequestException();
        }
        return ok(null);
    }

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${service.filehandler.endpoint}")
    private String urlFileHandlerRequest;

    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();

    /**
     * Invoke file-handler service to upload file adjustment
     *
     * @param scheme scheme info
     * @param fileNew file need to upload
     * @param fileOld file old need to delete on aws s3 bucket
     */
    boolean invokeFileHandlerS3_upload(Scheme scheme, String fileNew, String fileOld) {
        if (!HDUtil.isNullOrEmpty(fileOld)) {
            //create object to request upload s3 server
            FileS3DTOResponse s3DTO = new FileS3DTOResponse();
            List<FileS3DTOResponse.FileRep> lst = new ArrayList<>();
            FileS3DTOResponse.FileRep fileRep = new FileS3DTOResponse.FileRep();
            fileRep.setUri(fileOld);
            lst.add(fileRep);
            s3DTO.setFiles(lst);

            //delete file old
            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/delete", s3DTO,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto.getCode() != HttpStatus.OK.value()) {
                scheme.setFileLink(fileOld);
                schemeService.updateScheme(scheme);
            }
        }
        if (!HDUtil.isNullOrEmpty(fileNew)) {
            //create object to request upload s3 server
            FileS3DTORequest s3DTO = new FileS3DTORequest();
            List<FileS3DTORequest.FileReq> lst = new ArrayList<>();
            String b64 = fileStorageService.loadFileAsBase64(fileNew);
            lst.add(new FileS3DTORequest.FileReq(String.valueOf(scheme.getId()),
                    "", "scheme_upload_file",
                    MimeTypes.lookupMimeType(FilenameUtils.getExtension(fileNew)),
                    b64, fileOld));
            s3DTO.setFiles(lst);

            //upload file new
            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/upload", s3DTO,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                try {
                    FileS3DTOResponse fileResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
                            new TypeReference<FileS3DTOResponse>() {
                            });
                    if (fileResponse.getFiles().size() > 0) {
                        scheme.setFileLink(fileResponse.getFiles().get(0).getUri());
                    } else {
                        scheme.setFileLink("");
                    }
                } catch (IOException e) {
                    scheme.setFileLink("");
                }
            } else {
                scheme.setFileLink("");
            }
            fileStorageService.deleteFile(fileNew);
            schemeService.updateScheme(scheme);
            if (HDUtil.isNullOrEmpty(scheme.getFileLink())) {
                return false;
            }
        }
        return true;
    }
}
