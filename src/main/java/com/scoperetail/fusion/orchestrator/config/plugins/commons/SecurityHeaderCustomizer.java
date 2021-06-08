package com.scoperetail.fusion.orchestrator.config.plugins.commons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SecurityHeaderCustomizer {

  private static final String SECURITY_HEADERS = "SECURITY_HEADERS";

  private SecurityHeaderCustomizer() {

  }

  public static Map<String, Object> getSecurityParams() {
    final Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.put("cred_base64", "Basic ZnVzaW9uOmZ1c2lvbg==");
    return Collections.singletonMap(SECURITY_HEADERS, paramsMap);
  }
}
