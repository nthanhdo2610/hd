package com.tinhvan.hd.base.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.List;

public class FileRequest implements HDPayload {

    @JsonProperty("files")
    private List<FileReq> files;

    public FileRequest() {
        super();
    }

    public List<FileReq> getFiles() {
        return files;
    }

    public static class FileReq {
        @JsonProperty("contentType")
        private String contentType;
        @JsonProperty("data")
        private String data;

        public String getData() {
            return data;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    @Override
    public void validatePayload() {

        files.forEach(file -> {
            if (file != null && HDUtil.isNullOrEmpty(file.getContentType()))
                file.setContentType("");
//            if (file != null && HDUtil.isNullOrEmpty(file.getData()))
//                throw new BadRequestException(1118, "empty file");
        });
    }
}

