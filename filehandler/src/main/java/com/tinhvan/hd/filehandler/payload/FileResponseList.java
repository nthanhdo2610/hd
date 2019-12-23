package com.tinhvan.hd.filehandler.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.filehandler.exception.BadRequestException;
import com.tinhvan.hd.filehandler.utils.BaseUtil;

import java.util.List;

public class FileResponseList implements BasePayload {

    @JsonProperty("files")
    private List<FileRep> files;

    public FileResponseList(){
        super();
    }

    public void setFiles(List<FileRep> files) {
        this.files = files;
    }
    public List<FileRep> getFiles() {
        return files;
    }

    public static class FileRep {
        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    @Override
    public void validatePayload() {
        files.forEach(file -> {
            if (BaseUtil.isNullOrEmpty(file.getUri()))
                throw new BadRequestException(1119, "empty uri file");
        });
    }
}
