package com.tinhvan.hd.base.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class FileS3DTORequest implements HDPayload {
    @JsonProperty("files")
    private List<FileReq> files;

    public FileS3DTORequest() {
        super();
    }

    public List<FileReq> getFiles() {
        return files;
    }

    public void setFiles(List<FileReq> files) {
        this.files = files;
    }

    @Override
    public void validatePayload() {

    }

    public static class FileReq {
        private String id;
        private String type;
        private String path;
        private String contentType;
        private String data;
        private String fileOld;
        private boolean enablePublic;

        public FileReq(String id, String type, String path, String contentType, String data, String fileOld) {
            this.id = id;
            this.type = type;
            this.path = path;
            this.contentType = contentType;
            this.data = data;
            this.fileOld = fileOld;
        }
        public FileReq(String id, String type, String path, String contentType, String data, String fileOld, boolean enablePublic) {
            this.id = id;
            this.type = type;
            this.path = path;
            this.contentType = contentType;
            this.data = data;
            this.fileOld = fileOld;
            this.enablePublic = enablePublic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getFileOld() {
            return fileOld;
        }

        public void setFileOld(String fileOld) {
            this.fileOld = fileOld;
        }

        public boolean isEnablePublic() {
            return enablePublic;
        }

        public void setEnablePublic(boolean enablePublic) {
            this.enablePublic = enablePublic;
        }
    }
}
