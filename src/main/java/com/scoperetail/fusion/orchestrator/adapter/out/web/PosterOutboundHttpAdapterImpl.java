/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.adapter.out.web;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PosterOutboundHttpAdapterImpl implements PosterOutboundHttpAdapter {
  RetryRestService retryRestService;

  public PosterOutboundHttpAdapterImpl(RetryRestService retryRestService) {
    this.retryRestService = retryRestService;
  }

  @Override
  public void post(
      final String url,
      final String methodType,
      final String requestBody,
      final Map<String, String> httpHeaders) {
    final HttpHeaders headers = new HttpHeaders();
    httpHeaders.entrySet().forEach(mapEntry -> headers.add(mapEntry.getKey(), mapEntry.getValue()));
    final HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<String> exchange =
        retryRestService.callRestServiceRetryWithRecovery(url, methodType, httpEntity);
    log.trace("REST request sent to URL: {} and Response received is: {}", url, exchange);
  }
}
