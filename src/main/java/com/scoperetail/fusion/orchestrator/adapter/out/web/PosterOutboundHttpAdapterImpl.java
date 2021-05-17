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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PosterOutboundHttpAdapterImpl implements PosterOutboundHttpAdapter {

	@Override
	public void post(final String url, final String methodType, final String requestBody,
			final Map<String, String> httpHeaders) {
		final HttpHeaders headers = new HttpHeaders();
		httpHeaders.entrySet().forEach(mapEntry -> headers.add(mapEntry.getKey(), mapEntry.getValue()));
		final HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> exchange = null;
		exchange = this.callRestService(url, methodType, httpEntity);
		log.trace("REST request sent to URL: {} and Response received is: {}", url, exchange);
	}

	@Retryable(value = { RestClientException.class },
			maxAttemptsExpression = "#{${fusion.retryPolicies[0].maxAttempt}}",
			backoff = @Backoff(delayExpression = "#{${fusion.retryPolicies[0].backoffMS}}"))
	private ResponseEntity<String> callRestService(final String url, final String methodType, final HttpEntity<String> httpEntity) {
		final RestTemplate restTemplate = new RestTemplate();
		return restTemplate.exchange(url, HttpMethod.valueOf(methodType), httpEntity, String.class);
	}

	@Recover
	public void retryRecovery(RestClientException restClientException, String message) {
		log.error("retryRecover on message: {}, Exception was: {} ", message, restClientException.getMessage());
	}
}
