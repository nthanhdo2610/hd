package com.tinhvan.hd.filehandler.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.filehandler.exception.BadRequestException;
import org.apache.commons.io.FilenameUtils;

import java.util.List;

public class DownloadFileContractRequest implements BasePayload {

    @JsonProperty("files")
    private List<String> files;

    @Override
    public void validatePayload() {
        files.forEach(url->{
            if(!FilenameUtils.getExtension(url).toUpperCase().equals("PDF")){
                throw new BadRequestException();
            }
        });
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
