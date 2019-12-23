package com.tinhvan.hd.news.file.service;

import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.base.file.FileResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.base.file.Utils;
import com.tinhvan.hd.news.file.property.FileStorageProperties;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final Path logStorageLocation;
    private final long logExpired;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.logStorageLocation = Paths.get(fileStorageProperties.getLogDir())
                .toAbsolutePath().normalize();
        this.logExpired = fileStorageProperties.getLogExpired();
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.logStorageLocation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * FILE UPLOAD
     */

    public FileResponse.FileRep storeFile(String mimeType, String base64) {

        byte[] base64Bytes = Utils.validateData(base64);
        Tika tika = new Tika();
        if (HDUtil.isNullOrEmpty(mimeType))
            mimeType = tika.detect(base64Bytes);
        String fileName = UUID.randomUUID().toString() + "." + MimeTypes.lookupExtension(mimeType);

        try {
            // Copy file to the target location (Replacing existing file with the same name)
            InputStream targetStream = new ByteArrayInputStream(base64Bytes);
            Path targetLocation = this.fileStorageLocation.resolve(fileName).normalize();
            Files.copy(targetStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return new FileResponse.FileRep(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        Path targetLocation = this.fileStorageLocation.resolve(fileName).normalize();
        try {
            Files.deleteIfExists(targetLocation);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        }
        return null;
    }

    public String loadFileAsBase64(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            byte[] data = Files.readAllBytes(filePath);
            return Base64.getEncoder().encodeToString(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * FILE LOG
     */

    public void writeLog(String fileName, List<String> lines) {

        Charset charset = Charset.forName("US-ASCII");
        Path targetLocation = this.logStorageLocation.resolve(fileName).normalize();
        try {
            Files.write(targetLocation, lines, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanLog() {
        try {
            Files.list(logStorageLocation).forEach(path -> {
                try {
                    FileTime time = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
                    if (HDUtil.getUnixTime(new Date(time.toMillis())) + logExpired <= HDUtil.getUnixTimeNow()) {
                        Files.deleteIfExists(path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}