package com.scoperetail.fusion.orchestrator.config.plugins.order.create;

import java.util.HashMap;
import java.util.Map;
import com.scoperetail.fusion.core.config.plugins.commons.DateToolCustomizer;
import com.scoperetail.fusion.orchestrator.config.plugins.commons.SecurityHeaderCustomizer;

public class OrderCreateOutboundCustomizer {

  private OrderCreateOutboundCustomizer() {}

  public static Map<String, Object> getParamsMap() {
    final Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.putAll(DateToolCustomizer.getDateParams());
    paramsMap.putAll(SecurityHeaderCustomizer.getSecurityParams());
    return paramsMap;
  }
}
