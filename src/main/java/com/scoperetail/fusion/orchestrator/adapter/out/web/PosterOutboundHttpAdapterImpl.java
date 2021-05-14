/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.adapter.out.web;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PosterOutboundHttpAdapterImpl implements PosterOutboundHttpAdapter {
	@Retryable(value = { RuntimeException.class },
			maxAttempts = 3,
			backoff = @Backoff(delay = 100))
	@Override
	public void post(final String url, final String methodType, final String requestBody,
			final Map<String, String> httpHeaders) {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		httpHeaders.entrySet().forEach(mapEntry -> headers.add(mapEntry.getKey(), mapEntry.getValue()));
		final HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> exchange = null;
		try {
			exchange = restTemplate.exchange(url, HttpMethod.valueOf(methodType), httpEntity, String.class);
		} catch (Exception e) {
			throw new RuntimeException("Error");
		}
		log.trace("REST request sent to URL: {} and Response received is: {}", url, exchange);
	}

	@Recover
	public void retryRecovery(RuntimeException t, String s) {
		log.error("Error calling REST Service after retry: {} ", t.getMessage());
	}
}
