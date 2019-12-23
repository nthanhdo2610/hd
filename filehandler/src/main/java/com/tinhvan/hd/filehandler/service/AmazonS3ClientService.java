package com.tinhvan.hd.filehandler.service;

import com.tinhvan.hd.filehandler.payload.FileResponseList;

import java.io.File;

public interface AmazonS3ClientService
{
    FileResponseList.FileRep uploadFileToS3Bucket(String mimeType, String data, String key, boolean enablePublicReadAccess);
    void deleteFileFromS3Bucket(String uri);
    String uploadFileContractToS3Bucket(File contractFile, String keyName, boolean enablePublicReadAccess);
    File downloadFileFromS3Bucket(String uri);
    void replaceFileS3FromUri(File file, String uri, boolean enablePublicReadAccess);
}