package com.tinhvan.hd.file.controller;

import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.Log;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.base.file.FileRequest;
import com.tinhvan.hd.base.file.FileResponse;
import com.tinhvan.hd.file.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/authorize")
public class FileRestController extends HDController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/upload_file")
    public ResponseEntity<?> uploadFiles(@RequestBody RequestDTO<FileRequest> req) {
        Log.system(this.getClass().getName() + ": [BEGIN] uploadFiles");

        FileRequest fileRequest = req.init();

        FileResponse fileResponse = new FileResponse();

        List<FileResponse.FileRep> fileReps =new ArrayList<>();

        List<FileRequest.FileReq> fileReqs = fileRequest.getFiles();

        if (fileReqs != null && fileReqs.size() > 0) {
            for (FileRequest.FileReq file : fileReqs) {
                if (file != null) {
                    FileResponse.FileRep fileRep = fileStorageService.storeFile(file.getContentType(), file.getData());
                    fileReps.add(fileRep);
                }else {
                    fileReps.add(null);
                }
            }
        }
        fileResponse.setFiles(fileReps);

//        FileResponse fileResponse = new FileResponse();
//        fileResponse.setFiles(fileRequest.getFiles().stream().map(file -> fileStorageService.storeFile(file.getContentType(), file.getData())).collect(Collectors.toList()));

        Log.system(this.getClass().getName() + ": [END] uploadFiles");
        return ok(fileResponse);
    }

    @PostMapping(value = "/delete_file")
    public ResponseEntity<?> deleteFiles(@RequestBody RequestDTO<FileRequest> req) {
        Log.system(this.getClass().getName() + ": [BEGIN] deleteFiles");

        FileRequest fileRequest = req.init();
        fileRequest.getFiles().forEach(file -> fileStorageService.deleteFile(file.getData()));

        Log.system(this.getClass().getName() + ": [END] deleteFiles");
        return ok();
    }
}
