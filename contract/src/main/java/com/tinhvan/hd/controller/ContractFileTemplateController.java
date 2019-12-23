package com.tinhvan.hd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.file.FileS3DTORequest;
import com.tinhvan.hd.base.file.FileS3DTOResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.dto.ContractFileTemplateRQ;
import com.tinhvan.hd.entity.Contract;
import com.tinhvan.hd.entity.ContractFileTemplate;
import com.tinhvan.hd.entity.Scheme;
import com.tinhvan.hd.file.service.FileStorageService;
import com.tinhvan.hd.service.ContractFileTemplateService;
import io.swagger.models.auth.In;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contract/file_template")
public class ContractFileTemplateController extends HDController {

    @Autowired
    private ContractFileTemplateService contractFileTemplateService;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${service.filehandler.endpoint}")
    private String urlFileHandlerRequest;

    /**
     * Find all file template of contract file
     *
     * @return list of ContractFileTemplate contain info result
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAll(/*@RequestBody RequestDTO<EmptyPayload> req*/){

        List<ContractFileTemplate> contractFileTemplates = contractFileTemplateService.findAll();

        return ok(contractFileTemplates);
    }

    /**
     * Create a ContractFileTemplate object
     * @param req ContractFileTemplateRQ contain info need to generate ContractFileTemplate
     * @return http status code
     */
    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody RequestDTO<ContractFileTemplateRQ> req){

        ContractFileTemplateRQ contractFileTemplateRQ = req.init();

        if (contractFileTemplateRQ != null) {
            String type = contractFileTemplateRQ.getType();
            int idx = contractFileTemplateRQ.getIdx();

            ContractFileTemplate contractFileTemplate = new ContractFileTemplate();
            contractFileTemplate.setFileName(contractFileTemplateRQ.getFileName());
            contractFileTemplate.setPath(contractFileTemplateRQ.getPath());
            contractFileTemplate.setType(type);
            contractFileTemplate.setCreatedAt(new Date());


            ContractFileTemplate fileTemplate = contractFileTemplateService.getTemplateFileByTypeAndIdx(type,idx);

            if (fileTemplate != null) {
                List<ContractFileTemplate> fileTemplates = contractFileTemplateService.findByTypeAndIdx(type,idx);
                updateAllFileTemplate(fileTemplates);
            } else {
                List<ContractFileTemplate> contractFileTemplates = contractFileTemplateService.findByTypeOrderByDesc(type);
                if (contractFileTemplates != null && contractFileTemplates.size()> 0) {
                    ContractFileTemplate fileTemplate1 = contractFileTemplates.get(0);
                    if (idx > fileTemplate1.getIdx()) {
                        idx = fileTemplate1.getIdx() + 1;
                    }
                }
            }

            contractFileTemplate.setIdx(idx);

            contractFileTemplate = contractFileTemplateService.saveOrUpdate(contractFileTemplate);

            if (!invokeFileHandlerS3_upload(contractFileTemplate, contractFileTemplateRQ.getPath(), "")) {
                return serverError(1125, "Update image error");
            }
        }

        return ok();
    }

    /**
     * Update a ContractFileTemplate object
     * @param req ContractFileTemplateRQ contain info need to update a ContractFileTemplate
     * @return http status code
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<ContractFileTemplateRQ> req){

        ContractFileTemplateRQ contractFileTemplateRQ = req.init();

        Integer id = contractFileTemplateRQ.getId();

        if (id == null || id <= 0) {
            throw new BadRequestException(1440,"Contract template không tồn tại");
        }

        ContractFileTemplate contractFileTemplate = contractFileTemplateService.findById(id);

        if (contractFileTemplate == null) {
            throw new BadRequestException(1440,"Contract template không tồn tại");
        }

        String type = contractFileTemplateRQ.getType();
        String path = contractFileTemplate.getPath();
        int idx = contractFileTemplateRQ.getIdx();
        String fileName = contractFileTemplateRQ.getFileName();

        if (!HDUtil.isNullOrEmpty(type)) {
            contractFileTemplate.setType(type);
        }

        if (idx > 0) {
            ContractFileTemplate fileTemplate = contractFileTemplateService.getTemplateFileByTypeAndIdx(type,idx);
            if (fileTemplate != null) {
                if (idx != contractFileTemplate.getIdx()) {
                    if (idx < contractFileTemplate.getIdx()) {
                        List<ContractFileTemplate> fileTemplates = contractFileTemplateService.findByTypeAndIdx(type,idx);
                        updateAllFileTemplate(fileTemplates);
                    }else {
                        List<ContractFileTemplate> fileTemplates = contractFileTemplateService.findAllByTypeAndIdxLessThanEqual(type,idx);
                        if (fileTemplates != null && fileTemplates.size() > 0) {
                            for (ContractFileTemplate template : fileTemplates) {
                                template.setIdx(template.getIdx() - 1);
                            }
                            contractFileTemplateService.saveOrUpdateAll(fileTemplates);
                        }
                    }
                }
            }else {
                List<ContractFileTemplate> contractFileTemplates = contractFileTemplateService.findByTypeOrderByDesc(type);
                if (contractFileTemplates != null && contractFileTemplates.size()> 0) {
                    ContractFileTemplate fileTemplate1 = contractFileTemplates.get(0);
                    if (idx > fileTemplate1.getIdx()) {
                        idx = fileTemplate1.getIdx() + 1;
                    }
                }
            }
            contractFileTemplate.setIdx(idx);
        }

        if (!HDUtil.isNullOrEmpty(fileName)) {
            contractFileTemplate.setFileName(fileName);
        }

        contractFileTemplate.setUpdatedAt(new Date());
        contractFileTemplateService.saveOrUpdate(contractFileTemplate);

        if (HDUtil.isNullOrEmpty(path)) {
            if (!invokeFileHandlerS3_upload(contractFileTemplate, contractFileTemplateRQ.getPath(), "")) {
                return serverError(1125, contractFileTemplate);
            }
        }

        if (!HDUtil.isNullOrEmpty(path) && !HDUtil.isNullOrEmpty(contractFileTemplateRQ.getPath()) && !path.equals(contractFileTemplateRQ.getPath())) {
            if (!invokeFileHandlerS3_upload(contractFileTemplate, contractFileTemplateRQ.getPath(), path)) {
                return serverError(1125, contractFileTemplate);
            }
        }

        return ok();
    }

    /**
     * Update idx of template
     *
     * @param fileTemplates list of ContractFileTemplate
     */
    public void updateAllFileTemplate(List<ContractFileTemplate> fileTemplates) {
        if (fileTemplates != null && fileTemplates.size() > 0) {
            for (ContractFileTemplate template : fileTemplates) {
                template.setIdx(template.getIdx() + 1);
            }
            contractFileTemplateService.saveOrUpdateAll(fileTemplates);
        }
    }

    /**
     * Delete a ContractFileTemplate
     *
     * @param req contain id of ContractFileTemplate need to delete
     * @return http status code
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody RequestDTO<IdPayload> req){

        IdPayload idPayload = req.init();

        Integer id = (Integer) idPayload.getId();

        if (id == null || id <= 0) {
            throw new BadRequestException(1440,"Contract template không tồn tại");
        }

        ContractFileTemplate contractFileTemplate = contractFileTemplateService.findById(id);

        if (contractFileTemplate == null) {
            throw new BadRequestException(1440,"Contract template không tồn tại");
        }

        List<ContractFileTemplate> fileTemplates = contractFileTemplateService.findByTypeAndIdx(contractFileTemplate.getType(),contractFileTemplate.getIdx());

        if (fileTemplates != null && fileTemplates.size() > 0) {
            if (fileTemplates != null && fileTemplates.size() > 0) {
                for (ContractFileTemplate template : fileTemplates) {
                    template.setIdx(template.getIdx() - 1);
                }
                contractFileTemplateService.saveOrUpdateAll(fileTemplates);
            }
        }

        contractFileTemplateService.delete(contractFileTemplate);

        return ok();

    }

    private ObjectMapper mapper = new ObjectMapper();
    private Invoker invoker = new Invoker();

    /**
     * Invoke file-handler service to upload file of ContractFileTemplate
     *
     * @param contractFileTemplate info of ContractFileTemplate
     * @param fileNew file need to upload
     * @param fileOld file old need to delete and replace by fileNew
     * @return result of function is success or not
     */
    boolean invokeFileHandlerS3_upload(ContractFileTemplate contractFileTemplate, String fileNew, String fileOld) {
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
                contractFileTemplate.setPath(fileOld);
                contractFileTemplateService.saveOrUpdate(contractFileTemplate);
            }
        }
        if (!HDUtil.isNullOrEmpty(fileNew)) {
            //create object to request upload s3 server
            FileS3DTORequest s3DTO = new FileS3DTORequest();
            List<FileS3DTORequest.FileReq> lst = new ArrayList<>();
            String b64 = fileStorageService.loadFileAsBase64(fileNew);
            lst.add(new FileS3DTORequest.FileReq(String.valueOf(contractFileTemplate.getId()),
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
                        contractFileTemplate.setPath(fileResponse.getFiles().get(0).getUri());
                    } else {
                        contractFileTemplate.setPath("");
                    }
                } catch (IOException e) {
                    contractFileTemplate.setPath("");
                }
            } else {
                contractFileTemplate.setPath("");
            }
            fileStorageService.deleteFile(fileNew);
            contractFileTemplateService.saveOrUpdate(contractFileTemplate);
            if (HDUtil.isNullOrEmpty(contractFileTemplate.getPath())) {
                return false;
            }
        }
        return true;
    }

}
