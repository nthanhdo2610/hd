package com.tinhvan.hd.base.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileResponse{

    @JsonProperty("files")
    private List<FileRep> files;

    public static class FileRep {
        @JsonProperty("data")
        private String data;
        public FileRep(String data) {
            this.data = data;
        }
    }

    public void setFiles(List<FileRep> files) {
        this.files = files;
    }
}