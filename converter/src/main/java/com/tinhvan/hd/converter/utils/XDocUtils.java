package com.tinhvan.hd.converter.utils;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.documents4j.job.RemoteConverter;
import com.tinhvan.hd.converter.dto.DataDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class XDocUtils {

    @Async
    public DataDto convertPDF(IConverter converter, int index, MultipartFile file) {
        long start = new Date().getTime();
        DataDto dataDto = new DataDto(index);

        File pdf = new File(UUID.randomUUID().toString() + ".pdf");
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            Future<Boolean> conversion = converter
                    .convert(file.getInputStream()).as(DocumentType.MS_WORD)
                    .to(bo).as(DocumentType.PDF)
                    .prioritizeWith(1000) // optional
                    .schedule();
            conversion.get();
            OutputStream out = new FileOutputStream(pdf);
            bo.writeTo(out);
            out.close();
            bo.close();
            byte[] bytes = XDocUtils.loadFileAsByteArray(pdf);
            dataDto.setBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pdf != null)
                pdf.delete();
            long end = new Date().getTime();
            System.out.println("file " + index + ": " + (end - start));
            return dataDto;
        }
    }

    /**
     * Load bytes array of one file
     *
     * @param file
     * @return bytes array of file
     * @throws IOException
     */
    public static byte[] loadFileAsByteArray(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
}
