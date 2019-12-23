package com.tinhvan.hd.base.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class FileS3DTOResponse implements HDPayload {
    @JsonProperty("files")
    private List<FileRep> files;

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

    }
}
