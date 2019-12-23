package com.tinhvan.hd.converter.dto;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class DataDto {

    private int index;

    private String data;

    private byte[] bytes;

    private File file;

    private Resource resource;

    private ResponseEntity responseEntity;

    private MultipartFile multipartFile;

    public DataDto() {
        super();
    }

    public DataDto(int index) {
        this.index = index;
    }

    public DataDto(int index, String data) {
        this.index = index;
        this.data = data;
    }

    public DataDto(int index, byte[] bytes) {
        this.index = index;
        this.data = data;
    }

    public DataDto(int index, File file) {
        this.index = index;
        this.file = file;
    }

    public DataDto(int index, Resource resource) {
        this.index = index;
        this.resource = resource;
    }

    public DataDto(int index, ResponseEntity responseEntity) {
        this.index = index;
        this.responseEntity = responseEntity;
    }
    public DataDto(int index, MultipartFile multipartFile) {
        this.index = index;
        this.multipartFile = multipartFile;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
