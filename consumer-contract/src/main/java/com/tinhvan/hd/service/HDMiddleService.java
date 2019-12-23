package com.tinhvan.hd.service;

import com.tinhvan.hd.dto.HDContractResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface HDMiddleService {

    HDContractResponse sendPostRequest(HttpHeaders header, String body);
}
