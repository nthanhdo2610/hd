package com.tinhvan.hd.base;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.URI;

public class RequestFactory {

    private RestTemplate restTemplate;

    private void init() {
        try {
            HttpComponentsClientHttpRequestWithBodyFactory factory = new HttpComponentsClientHttpRequestWithBodyFactory();
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);
            CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(context)
                    .build();
            factory.setHttpClient(httpClient);
            this.restTemplate = new RestTemplate();
            this.restTemplate.setRequestFactory(factory);
        } catch (Exception ex) {
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    private static final class HttpComponentsClientHttpRequestWithBodyFactory extends HttpComponentsClientHttpRequestFactory {
        @Override
        protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
            if (httpMethod == HttpMethod.GET) {
                return new HttpGetRequestWithEntity(uri);
            }
            return super.createHttpUriRequest(httpMethod, uri);
        }
    }

    private static final class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {

        public HttpGetRequestWithEntity(final URI uri) {
            super.setURI(uri);
        }

        @Override
        public String getMethod() {
            return HttpMethod.GET.name();
        }
    }

    public RestTemplate getRestTemplate() {
        this.init();
        return this.restTemplate;
    }
}