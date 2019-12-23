package com.tinhvan.hd.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.dto.ContractInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;

import static com.tinhvan.hd.utils.ConstantStatus.WAIT_FOR_SIGNING_CONTRACT;

public class ContractUtils {

    public static String convertStatus(String status) {
        String statusFm = "";
        if (HDUtil.isNullOrEmpty(status)) {
            return statusFm;
        }

        status = status.toUpperCase();

        String contractWaitingStatus = WAIT_FOR_SIGNING_CONTRACT;
        String contractCurrentStatus = ConstantStatus.CURRENT_CONTRACT;
        String contractDisbursedStatus = ConstantStatus.CONTRACT_DISBURSED;
        String contractSignedContract = ConstantStatus.SIGNED_CONTRACT;

        List<String> lstWaiting = new ArrayList<String>(Arrays.asList(contractWaitingStatus.split(",")));

        List<String> lstCurrent = new ArrayList<String>(Arrays.asList(contractCurrentStatus.split(",")));

        List<String> lstDisbursed = new ArrayList<String>(Arrays.asList(contractDisbursedStatus.split(",")));

        List<String> lstSignedContract = new ArrayList<String>(Arrays.asList(contractSignedContract.split(",")));

        //ContractStatus contractStatus = ContractStatus.parseValue(status);
        try {
//            switch (contractStatus) {
//                case LIVE:
//                    statusFm = "Đang vay";
//                    break;
//                case MATURED:
//                    statusFm = "Đã giải ngân";
//                    break;
//                case WAITING_FOR_SIGNING:
//                    statusFm = "Đang đợi ký";
//                    break;
//                case WAITING_FOR_APPROVAL:
//                    statusFm = "Đang chờ duyệt";
//                    break;
//                default:
//                    statusFm = "";
//                    break;
//
//            }
            if (lstWaiting.contains(status)) {
                statusFm = "Chờ ký";
            } else if (lstDisbursed.contains(status)) {
                statusFm = "Đã kết thúc";
            } else if (lstCurrent.contains(status)) {
                statusFm = "Kích hoạt";
            } else if (lstSignedContract.contains(status)) {
                statusFm = "Đã ký";
            } else {
                statusFm = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return statusFm;
    }

    public static List<String> splitStatus(String status) {
        if (StringUtils.isEmpty(status)) {
            return new ArrayList<>();
        }

        return Arrays.asList(status.split(",\\s+"));
    }

    public static Collection<Field> getFields(Class<?> clazz) {
//    if (log.isDebugEnabled()) {
        //    log.debug("getFields(Class<?>) - start");
        //}

        Map<String, Field> fields = new HashMap<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!fields.containsKey(field.getName())) {
                    fields.put(field.getName(), field);
                }
            }

            clazz = clazz.getSuperclass();
        }

        Collection<Field> returnCollection = fields.values();
        //  if (log.isDebugEnabled()) {
        //  log.debug("getFields(Class<?>) - end");
        //  }
        return returnCollection;
    }

    public void validateToken(String urlCustomerRequest, String urlStaffRequest, JWTPayload jwtPayload) {
        IdPayload idPayload = new IdPayload();
        Invoker invoker = new Invoker();
        try {
            String token = JWTProvider.encode(jwtPayload);
            idPayload.setId(token);
            if (jwtPayload.getRole() == HDConstant.ROLE.CUSTOMER) {
                ResponseDTO<Object> dto = invoker.call(urlCustomerRequest + "/validate_token", idPayload,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto == null || dto.getCode() != HttpStatus.OK.value()) {
                    throw new UnauthorizedException();
                }
            }
            /*if (jwtPayload.getRole() == HDConstant.ROLE.STAFF) {
                ResponseDTO<Object> dto = invoker.call(urlStaffRequest + "/check_token", idPayload,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                if (dto == null || dto.getCode() != HttpStatus.OK.value()) {
                    throw new UnauthorizedException();
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnauthorizedException();
        }
    }

    public static String generateFullName(ContractInfo info) {
        String str = "";
        if (!HDUtil.isNullOrEmpty(info.getLastName()))
            str += info.getLastName() + " ";
        if (!HDUtil.isNullOrEmpty(info.getMidName()))
            str += info.getMidName() + " ";
        if (!HDUtil.isNullOrEmpty(info.getFirstName()))
            str += info.getFirstName();
        return str.trim();
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

    public static List<String> word2pdf_b64(String converterURI, List<String> lstBase64) {
        ObjectMapper mapper = new ObjectMapper();
        String converterConfig = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(("converter.ini")));
            converterConfig = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.getMessage();
        }
        if (!HDUtil.isNullOrEmpty(converterConfig)) {
            System.out.println("converter-service:" + converterConfig);
            converterURI = converterConfig;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        Invoker.setTimeout(restTemplate);
        // Data attached to the request.
        HttpEntity<List<String>> requestBody = new HttpEntity<>(lstBase64, headers);

        // Send request with POST method
        ResponseEntity<String> result = restTemplate.postForEntity(converterURI, requestBody, String.class);

        // Code = 200.
        if (result != null && result.getStatusCode() == HttpStatus.OK && result.getBody() != null) {
            try {
                List<String> lst = mapper.readValue(result.getBody(), new TypeReference<List<String>>() {
                });
                return lst;
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerErrorException();
            }
        }
        throw new InternalServerErrorException();
    }

    public static ResponseEntity word2pdf(String uri, List<File> lstFile) {
        try {
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            for (File file : lstFile) {
                FileSystemResource value = new FileSystemResource(file);
                map.add("files", value);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-environment", "FILE-HANDLER");
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
