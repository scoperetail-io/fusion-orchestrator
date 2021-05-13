package com.scoperetail.fusion.orchestrator.config.plugins.PickEndEvent.copy;

import java.util.HashMap;
import java.util.Map;
import com.scoperetail.fusion.orchestrator.config.plugins.commons.DateToolCustomizer;
import com.scoperetail.fusion.orchestrator.config.plugins.commons.SecurityHeaderCustomizer;

public class PickEndOutboundCustomizer {


  private PickEndOutboundCustomizer() {}

  public static Map<String, Object> getParamsMap() {
    Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.putAll(SecurityHeaderCustomizer.getSecurityParams());
    paramsMap.putAll(DateToolCustomizer.getDateParams());
    return paramsMap;
  }
}
