package com.tinhvan.hd.promotion.file.controller;

import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.Log;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.base.file.FileRequest;
import com.tinhvan.hd.base.file.FileResponse;
import com.tinhvan.hd.promotion.file.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/promotion")
public class FileRestController extends HDController {

    @Autowired
    FileStorageService fileStorageService;

    /**
     * Upload file from client to server before upload on aws s3 bucket
     *
     * @param req object FileRequest contain list base64 data of file needed upload
     * @return object FileRequest contain local path of file uploaded on server
     */
    @PostMapping(value = "/upload_file")
    public ResponseEntity<?> uploadFiles(@RequestBody RequestDTO<FileRequest> req) {
        FileRequest fileRequest = req.init();
        FileResponse fileResponse = new FileResponse();
        fileResponse.setFiles(fileRequest.getFiles().stream().map(file -> fileStorageService.storeFile(file.getContentType(), file.getData())).collect(Collectors.toList()));
        return ok(fileResponse);
    }

    /**
     * Delete file at local server base on local path file
     *
     * @param req object FileRequest contain list local path of file needed delete on server
     * @return http status code
     */
    @PostMapping(value = "/delete_file")
    public ResponseEntity<?> deleteFiles(@RequestBody RequestDTO<FileRequest> req) {
        FileRequest fileRequest = req.init();
        fileRequest.getFiles().forEach(file -> fileStorageService.deleteFile(file.getData()));
        return ok();
    }
}
