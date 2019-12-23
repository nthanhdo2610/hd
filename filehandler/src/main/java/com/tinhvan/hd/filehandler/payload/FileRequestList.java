package com.tinhvan.hd.filehandler.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.filehandler.exception.BadRequestException;
import com.tinhvan.hd.filehandler.utils.BaseUtil;
import com.tinhvan.hd.filehandler.utils.MimeTypes;

import java.util.List;

public class FileRequestList implements BasePayload {

    @JsonProperty("files")
    private List<FileReq> files;

    public FileRequestList() {
        super();
    }

    public List<FileReq> getFiles() {
        return files;
    }

    public static class FileReq {
        private String contentType;
        private String data;
        private String path;
        private String type;
        private String id;
        private String fileOld;
        private boolean enablePublic;

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

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

    @Override
    public void validatePayload() {

        files.forEach(file -> {
            if (BaseUtil.isNullOrEmpty(file.getContentType()))
                file.setContentType("");
            if (BaseUtil.isNullOrEmpty(file.getData()))
                throw new BadRequestException(1118, "empty file");
            /*try {
                UUID.fromString(file.getId());
            } catch (Exception e) {
                throw new BadRequestException(1106, "invalid id");
            }*/
        });
    }


}
