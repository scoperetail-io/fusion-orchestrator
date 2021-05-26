package com.scoperetail.fusion.orchestrator.adapter.out.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RetryRestServiceImpl implements RetryRestService {
  @Override
  public ResponseEntity<String> retryPost(
      String url, String methodType, HttpEntity<String> httpEntity) {
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
        HttpClientBuilder.create().build());
    final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
    try {
      return restTemplate.exchange(url, HttpMethod.valueOf(methodType), httpEntity, String.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void recover(RuntimeException exception, String message) {
    log.error(
        "On recover after retryPost failed. message: {}, Exception was: {} ",
        message,
        exception.getMessage());
  }
}