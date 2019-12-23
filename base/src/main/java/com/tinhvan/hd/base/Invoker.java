package com.tinhvan.hd.base;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Invoker {


    public Invoker() {
    }

    private static RestTemplate caller() {
        RequestFactory requestFactory = new RequestFactory();
        RestTemplate caller = requestFactory.getRestTemplate();
        return caller;
    }

    private static HttpHeaders getHeader() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("x-environment", HDConstant.ENVIRONMENT.WEB_ADMIN);
        ServletRequestAttributes ra =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (ra != null) {
            HttpServletRequest httpRequest = ra.getRequest();
            String[] headers = new String[]{
                    "authorization",
                    "x-api-key",
                    "accept-language",
                    "content-type",
                    "x-environment"
            };
            for (String header : headers) {
                requestHeaders.set(header, httpRequest.getHeader(header));
            }
        }
        return requestHeaders;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <R> ResponseDTO<R> call(String uri, HDPayload body,
                                          ParameterizedTypeReference parameterizedTypeReference) {
        try {
            RequestDTO requestDTO = new RequestDTO(body);
            HttpHeaders requestHeaders = getHeader();
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestDTO, requestHeaders);
            RestTemplate caller = caller();
            setTimeout(caller);
            ResponseEntity<ResponseDTO<R>> response = caller.exchange(uri, HttpMethod.POST,
                    requestEntity, parameterizedTypeReference);
            if (response != null) {
                if (response.getBody() != null) {
                    return response.getBody();
                }
            }
        } catch (HttpStatusCodeException ex) {
            Gson gson = HDUtil.gson();
            Type pType = new TypeToken<ResponseDTO<R>>() {
            }.getType();
            return gson.fromJson(ex.getResponseBodyAsString(), pType);
        } catch (Exception ex) {
            throw new InternalServerErrorException(ex.getMessage());
        }
        return null;
    }

    public static void setTimeout(RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60 * 1000); // set short connect timeout
        requestFactory.setReadTimeout(60 * 1000); // set slightly longer read timeout
        restTemplate.setRequestFactory(requestFactory);
    }

}
