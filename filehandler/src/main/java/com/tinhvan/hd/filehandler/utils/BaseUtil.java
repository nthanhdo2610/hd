package com.tinhvan.hd.filehandler.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.tinhvan.hd.filehandler.payload.GenerateFileRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BaseUtil {

    private static Gson gson;

    public static Gson gson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static long getUnixTimeNow() {
        return Instant.now().getEpochSecond();
    }

    public static long getUnixTime(Date time) {
        return time.getTime() / 1000L;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private static boolean isMatch(String pattern, String value) {
        Pattern r = Pattern.compile(pattern);
        Matcher matches = r.matcher(value);
        if (matches.find()) {
            return true;
        }
        return false;
    }

    public static boolean isPhoneNumber(String value) {
        if (isNullOrEmpty(value)) return false;
        String pattern = new StringBuilder()
                .append("(^0(86|96|97|98|32|33|34|35|36|37|38|39|89|90|93|70|79")
                .append("|77|76|78|88|91|94|83|84|85|81|82|92|56|58|99|59|52)")
                .append("+\\d{7}$)").toString();
        return isMatch(pattern, value);
    }

    public static boolean isEmail(String value) {
        if (isNullOrEmpty(value)) return false;
        String pattern = new StringBuilder()
                .append("^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|")
                .append("[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])")
                .append("+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|")
                .append("[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|")
                .append("((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?")
                .append("(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|")
                .append("[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900")
                .append("-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c")
                .append("\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-")
                .append("\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)")
                .append("?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF")
                .append("\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-")
                .append("\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-")
                .append("\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|")
                .append("[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+")
                .append("(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])")
                .append("|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])")
                .append("([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF")
                .append("\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\")
                .append("uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$").toString();
        return isMatch(pattern, value);
    }

    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<T>(c.size());
        for (Object o : c)
            r.add(clazz.cast(o));
        return r;
    }

    public static void setTimeout(RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(30000);
        restTemplate.setRequestFactory(requestFactory);
    }

    public static String generateKeyS3(GenerateFileRequest fileRequest) {
        String keyName = "contract/";
        if (!BaseUtil.isNullOrEmpty(fileRequest.getContractId()))
            keyName += fileRequest.getContractId() + "/";
        if (!BaseUtil.isNullOrEmpty(fileRequest.getFileType()))
            keyName += fileRequest.getFileType() + "/";
        return keyName;
    }

    public static byte[] loadFile(File file) throws IOException {
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

