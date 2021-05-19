/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.config;

import com.scoperetail.fusion.orchestrator.common.HashUtil;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class HTTPInterceptor implements HandlerInterceptor {

  private static final String SHA_256 = "SHA-256";
  private static final String HEADER_AUTHORIZATION = "AUTHORIZATION";

  @Value("${hash.key}")
  private String credentials;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String requestURL = request.getRequestURL().toString();

    if (requestURL.contains("actuator")) {
      return true;
    }

    Optional<String> headerAuthorization =
        Optional.ofNullable(request.getHeader(HEADER_AUTHORIZATION));
    if (headerAuthorization.isPresent()) {
      validateCredentials(headerAuthorization.get().replace("Basic", "").strip());
    } else {
      log.error("Error authorization not found");
      throw new HeaderValidException();
    }

    return true;
  }

  private void validateCredentials(String credentials) {
    String value = HashUtil.getHash(credentials, SHA_256);
    if (!this.credentials.equals(value)) {
      log.error("not authorized {}", credentials);
      throw new HeaderValidException();
    }
  }
}
