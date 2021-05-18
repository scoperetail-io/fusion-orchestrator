package com.scoperetail.fusion.orchestrator.adapter.out.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

public interface RetryRestService {
  @Retryable(
      value = {RuntimeException.class},
      maxAttemptsExpression = "#{${fusion.retryPolicies[0].maxAttempt}}",
      backoff = @Backoff(delayExpression = "#{${fusion.retryPolicies[0].backoffMS}}"))
  ResponseEntity<String> retryPost(
      final String url, final String methodType, final HttpEntity<String> httpEntity);

  @Recover
  void recover(RuntimeException e, String message);
}