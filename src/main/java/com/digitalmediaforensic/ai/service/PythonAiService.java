
package com.digitalmediaforensic.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class PythonAiService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String predictEndpoint;

    public PythonAiService(
            @Value("${python.api.base-url}") String baseUrl,
            @Value("${python.api.predict-endpoint}") String predictEndpoint) {

        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
        this.predictEndpoint = predictEndpoint;
    }

    public String analyzeImage(byte[] imageBytes, String filename) {

        ByteArrayResource imageResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<ByteArrayResource> fileEntity =
                new HttpEntity<>(imageResource, fileHeaders);

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add("file", fileEntity);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, requestHeaders);

        String url = baseUrl + predictEndpoint;

        String result = restTemplate.postForObject(
                url,
                requestEntity,
                String.class
        );

        return result;
    }
}
