package com.tinhvan.hd.filehandler.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import com.tinhvan.hd.filehandler.exception.BadRequestException;
import com.tinhvan.hd.filehandler.exception.InternalServerErrorException;
import com.tinhvan.hd.filehandler.payload.FileResponseList;
import com.tinhvan.hd.filehandler.service.AmazonS3ClientService;
import com.tinhvan.hd.filehandler.utils.BaseUtil;
import com.tinhvan.hd.filehandler.utils.MimeTypes;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;


@Component
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {
    private String awsS3AudioBucket;
    private AmazonS3 amazonS3;
    @Value("${aws.s3.bucket.uri}")
    private String bucketUri;

    @Autowired
    public AmazonS3ClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket) {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    }

    @Async
    public FileResponseList.FileRep uploadFileToS3Bucket(String mimeType, String data, String keyName, boolean enablePublicReadAccess) {

        byte[] base64Bytes = validateData(data);
        Tika tika = new Tika();
        if (BaseUtil.isNullOrEmpty(mimeType))
            mimeType = tika.detect(base64Bytes);
        String fileName = UUID.randomUUID().toString() + "." + MimeTypes.lookupExtension(mimeType);

        keyName += fileName;
        File file = new File(fileName);
        try {

            //creating the file in the server (temporarily)
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(base64Bytes);
            fos.close();
            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, keyName, file);

            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            this.amazonS3.putObject(putObjectRequest);

            //String URL = this.amazonS3.getUrl(this.awsS3AudioBucket, keyName).toString();
            FileResponseList.FileRep rep = new FileResponseList.FileRep();
            rep.setUri(bucketUri + keyName);
            return rep;
        } catch (IOException | AmazonServiceException ex) {
            //ex.printStackTrace();
            throw new InternalServerErrorException(ex.getMessage());
        } finally {
            //removing the file created in the server
            file.delete();
        }
    }

    @Async
    public void deleteFileFromS3Bucket(String uri) {
        try {
            AmazonS3URI s3URI = new AmazonS3URI(uri);
            amazonS3.deleteObject(new DeleteObjectRequest(this.awsS3AudioBucket, s3URI.getKey()));
        } catch (AmazonServiceException ase) {
            //ase.printStackTrace();
            //throw new InternalServerErrorException(ase.getMessage());
        }
    }

    @Override
    public String uploadFileContractToS3Bucket(File contractFile, String keyName, boolean enablePublicReadAccess) {
        String uri;
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, keyName, contractFile);

            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            this.amazonS3.putObject(putObjectRequest);
            //String URL = this.amazonS3.getUrl(this.awsS3AudioBucket, keyName).toString();
            uri = bucketUri + keyName;
        } catch (AmazonServiceException ex) {
            //ex.printStackTrace();
            throw new InternalServerErrorException(ex.getMessage());
        } finally {
            contractFile.delete();
        }
        return uri;
    }

    @Override
    public File downloadFileFromS3Bucket(String uri) {
        //System.out.println("downloadFileFromS3Bucket:" + uri);
        try {
            AmazonS3URI s3URI = new AmazonS3URI(uri);
            S3Object s3object = amazonS3.getObject(awsS3AudioBucket, s3URI.getKey());
            S3ObjectInputStream inputStream = s3object.getObjectContent();
            File file = new File(UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(s3URI.getKey()));
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return file;
        } catch (IOException e) {
            //e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        } catch (AmazonS3Exception e) {
            //e.printStackTrace();
            throw new BadRequestException(1119);
        }
    }

    @Override
    public void replaceFileS3FromUri(File file, String uri, boolean enablePublicReadAccess) {
        if (file.exists()) {
            try {
                AmazonS3URI s3URI = new AmazonS3URI(uri);
                String keyName = s3URI.getKey();
                amazonS3.deleteObject(new DeleteObjectRequest(this.awsS3AudioBucket, s3URI.getKey()));
                PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, keyName, file);
                if (enablePublicReadAccess) {
                    putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
                }
                this.amazonS3.putObject(putObjectRequest);
            } catch (AmazonServiceException ex) {
                file.delete();
                //ex.printStackTrace();
                throw new InternalServerErrorException(ex.getMessage());
            }
        }
    }

    public static byte[] validateData(String data) {

        byte[] base64Bytes = Base64
                .getDecoder()
                .decode(data);
        Tika tika = new Tika();
        String contentType = tika.detect(base64Bytes);
        String type = contentType.split("/")[0];
        if (type.equals("image") && !contentType.equals(MimeTypes.MIME_IMAGE_GIF)) {
            try {
                //Read the image file and store as a BufferedImage
                ByteArrayInputStream bis = new ByteArrayInputStream(base64Bytes);
                BufferedImage convertMe = ImageIO.read(bis);
                bis.close();

                //resize image uploaded
                int width = convertMe.getWidth();
                int height = convertMe.getHeight();
                if (width > 800) {
                    width = 800;
                    height = (width * convertMe.getHeight()) / convertMe.getWidth();
                }
                //Save the BufferedImage object
                BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                result.createGraphics().drawImage(resize(convertMe, width, height), 0, 0, Color.WHITE, null);

                //Write BufferedImage has converted and parse to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(result, MimeTypes.lookupExtension(MimeTypes.MIME_IMAGE_JPEG), bos);
                base64Bytes = bos.toByteArray();
                bos.close();

            } catch (IOException e) {
                //e.printStackTrace();
                throw new InternalServerErrorException(e.getMessage());
            }

        }
        return base64Bytes;
    }

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}