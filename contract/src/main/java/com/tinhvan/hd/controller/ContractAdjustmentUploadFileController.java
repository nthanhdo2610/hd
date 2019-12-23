package com.tinhvan.hd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.file.FileS3DTORequest;
import com.tinhvan.hd.base.file.FileS3DTOResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.dto.AdjustmentUploadFileRequest;
import com.tinhvan.hd.dto.AdjustmentUploadFileSearch;
import com.tinhvan.hd.dto.AdjustmentUploadFileSearchResponse;
import com.tinhvan.hd.entity.ContractAdjustmentUploadFile;
import com.tinhvan.hd.file.service.FileStorageService;
import com.tinhvan.hd.service.ContractAdjustmentUploadFileService;
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
@RequestMapping("/api/v1/contract/adjustment_upload_file")
public class ContractAdjustmentUploadFileController extends HDController {

    @Autowired
    private ContractAdjustmentUploadFileService adjustmentUploadFileService;

    /**
     * Find list of file adjustment
     *
     * @param req AdjustmentUploadFileSearch contain info filter AdjustmentUploadFileSearch
     * @return AdjustmentUploadFileSearchResponse contain result paging style
     */
    @PostMapping("/list")
    public ResponseEntity<?> search(@RequestBody RequestDTO<AdjustmentUploadFileSearch> req) {
        AdjustmentUploadFileSearch searchRequest = req.init();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }

        List<ContractAdjustmentUploadFile> lst = adjustmentUploadFileService.find(searchRequest);
        int total = adjustmentUploadFileService.count(searchRequest);
        return ok(new AdjustmentUploadFileSearchResponse(lst, total));
    }

    /**
     * Create a ContractAdjustmentUploadFile
     *
     * @param req AdjustmentUploadFileRequest contain info request
     * @return ContractAdjustmentUploadFile after created successfully
     */
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> create(@RequestBody RequestDTO<AdjustmentUploadFileRequest> req) {

        AdjustmentUploadFileRequest uploadFileRequest = req.getPayload();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        ContractAdjustmentUploadFile adjustmentUploadFile = new ContractAdjustmentUploadFile();
        adjustmentUploadFile.setCreatedAt(req.now());
        adjustmentUploadFile.setCreatedBy(req.jwt().getUuid());
        adjustmentUploadFile.setCreatedName(uploadFileRequest.getCreatedName());
        adjustmentUploadFile.setDescription(uploadFileRequest.getDescription());
        adjustmentUploadFile.setFilePath(uploadFileRequest.getFilePath());
        adjustmentUploadFile.setSendMail(0);
        adjustmentUploadFileService.create(adjustmentUploadFile);
        invokeFileHandlerS3_upload(adjustmentUploadFile, adjustmentUploadFile.getFilePath(), "");
        return ok(adjustmentUploadFile);
    }

    /**
     * Update a ContractAdjustmentUploadFile exist
     *
     * @param req AdjustmentUploadFileRequest contain info request
     * @return ContractAdjustmentUploadFile after updated successfully
     */
    @PostMapping("/update")
    @Transactional
    public ResponseEntity<?> update(@RequestBody RequestDTO<AdjustmentUploadFileRequest> req) {


        AdjustmentUploadFileRequest uploadFileRequest = req.getPayload();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        if (uploadFileRequest.getId() == 0)
            return badRequest(1431);
        ContractAdjustmentUploadFile adjustmentUploadFile = adjustmentUploadFileService.findById(uploadFileRequest.getId());
        if (adjustmentUploadFile == null)
            return badRequest(1431);
        String fileOld = adjustmentUploadFile.getFilePath();

        adjustmentUploadFile.setModifiedAt(req.now());
        adjustmentUploadFile.setModifiedBy(req.jwt().getUuid());
        adjustmentUploadFile.setDescription(uploadFileRequest.getDescription());
        adjustmentUploadFile.setFilePath(uploadFileRequest.getFilePath());

        adjustmentUploadFileService.update(adjustmentUploadFile);
        if (!fileOld.equals(adjustmentUploadFile.getFilePath())) {
            invokeFileHandlerS3_upload(adjustmentUploadFile, adjustmentUploadFile.getFilePath(), fileOld);
        }
        return ok(adjustmentUploadFile);
    }

    /**
     * Delete one ContractAdjustmentUploadFile exist
     *
     * @param req IdPayload contain id of ContractAdjustmentUploadFile
     * @return http status code
     */
    @PostMapping("/delete")
    @Transactional
    public ResponseEntity<?> delete(@RequestBody RequestDTO<IdPayload> req) {


        IdPayload idPayload = req.getPayload();
        if (req.jwt().getRole() == HDConstant.ROLE.CUSTOMER) {
            return unauthorized();
        }
        Integer id;
        try {
            id = (Integer) idPayload.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest(1431);
        }
        if (id == null || id.intValue() == 0)
            return badRequest(1431);
        ContractAdjustmentUploadFile adjustmentUploadFile = adjustmentUploadFileService.findById(id.intValue());
        if (adjustmentUploadFile == null)
            return badRequest(1431);
        if (adjustmentUploadFile.getSendMail() == 1)
            return badRequest(1432);

        adjustmentUploadFile.setSendMail(HDConstant.STATUS.DELETE_FOREVER);


        adjustmentUploadFileService.update(adjustmentUploadFile);

        //adjustmentUploadFileService.delete(id.intValue());
        return ok();
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
     * @param adjustmentUploadFile file adjustment info
     * @param fileNew file need to upload
     * @param fileOld file old need to delete on aws s3 bucket
     */
    void invokeFileHandlerS3_upload(ContractAdjustmentUploadFile adjustmentUploadFile, String fileNew, String fileOld) {
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
                adjustmentUploadFile.setFilePath(fileOld);
                adjustmentUploadFileService.update(adjustmentUploadFile);
            }
        }
        if (!HDUtil.isNullOrEmpty(fileNew)) {
            try {
                //create object to request upload s3 server
                FileS3DTORequest s3DTO = new FileS3DTORequest();
                List<FileS3DTORequest.FileReq> lst = new ArrayList<>();
                String b64 = fileStorageService.loadFileAsBase64(fileNew);
                lst.add(new FileS3DTORequest.FileReq(String.valueOf(adjustmentUploadFile.getId()),
                        "", "contract_adjustment_upload_file",
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
                            adjustmentUploadFile.setFilePath(fileResponse.getFiles().get(0).getUri());
                        } else {
                            adjustmentUploadFile.setFilePath("");
                        }
                    } catch (IOException e) {
                        adjustmentUploadFile.setFilePath("");
                    }
                } else {
                    adjustmentUploadFile.setFilePath("");
                }
                fileStorageService.deleteFile(fileNew);
                adjustmentUploadFileService.update(adjustmentUploadFile);
                if (HDUtil.isNullOrEmpty(adjustmentUploadFile.getFilePath())) {
                    throw new BadRequestException(1125);
                }
            } catch (Exception e) {
                throw new BadRequestException(1125);
            }
        }
    }
}
