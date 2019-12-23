package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dto.HDContractResponse;
import com.tinhvan.hd.exception.InternalServerErrorException;
import com.tinhvan.hd.service.HDMiddleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class HDMiddleServiceImpl implements HDMiddleService {

    @Value("${hd.url.request.middle}")
    private String baseUrl;

    @Override
    public HDContractResponse sendPostRequest(HttpHeaders header, String body) {
        HDContractResponse contractResponse = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(baseUrl);
            HttpEntity<String> request = new HttpEntity<>(body, header);

            ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
        }catch (URISyntaxException ex){
            throw new InternalServerErrorException();
        }
        return contractResponse;
    }
}
