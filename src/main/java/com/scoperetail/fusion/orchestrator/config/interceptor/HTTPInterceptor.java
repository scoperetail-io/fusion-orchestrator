/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.config.interceptor;

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

  private static final Integer UNAUTHORIZED = 401;
  private static final String SHA_256 = "SHA-256";
  private static final String HEADER_AUTHORIZATION = "Authorization";

  @Value("${fusion.credentials}")
  private String credentials;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    Optional<String> headerAuthorization =
        Optional.ofNullable(request.getHeader(HEADER_AUTHORIZATION));
    if (headerAuthorization.isPresent()) {
      if(!validateCredentials(headerAuthorization.get().replace("Basic", "").strip())) {
        response.sendError(UNAUTHORIZED);
      }
    } else {
      log.error("Authorization header not found");
      response.sendError(UNAUTHORIZED);
    }
    return true;
  }

  private boolean validateCredentials(String credentials) {
    String value = HashUtil.getHash(credentials, SHA_256);
    if (!this.credentials.equals(value)) {
      log.error("Unauthorized {}", credentials);
      return false;
    }
    return true;
  }
}
