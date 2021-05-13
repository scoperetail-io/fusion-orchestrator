package com.scoperetail.fusion.orchestrator.config.plugins.OrderDropEventReader;

import com.scoperetail.fusion.orchestrator.config.plugins.commons.DateToolCustomizer;
import com.scoperetail.fusion.orchestrator.config.plugins.commons.SecurityHeaderCustomizer;
import com.scoperetail.fusion.orchestrator.config.plugins.commons.TimeZoneCustomizer;

import java.util.HashMap;
import java.util.Map;

public class OrderDropOutboundCustomizer {

  private OrderDropOutboundCustomizer() {}

  public static Map<String, Object> getParamsMap() {
    Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.putAll(SecurityHeaderCustomizer.getSecurityParams());
    paramsMap.putAll(DateToolCustomizer.getDateParams());
    paramsMap.putAll(TimeZoneCustomizer.getTimeZoneParams());
    return paramsMap;
  }
}
