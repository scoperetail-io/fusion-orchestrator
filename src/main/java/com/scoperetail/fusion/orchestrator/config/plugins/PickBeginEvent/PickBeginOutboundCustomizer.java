package com.scoperetail.fusion.orchestrator.config.plugins.PickBeginEvent;

import java.util.HashMap;
import java.util.Map;
import com.scoperetail.fusion.orchestrator.config.plugins.commons.DateToolCustomizer;
import com.scoperetail.fusion.orchestrator.config.plugins.commons.SecurityHeaderCustomizer;

public class PickBeginOutboundCustomizer {


  private PickBeginOutboundCustomizer() {}

  public static Map<String, Object> getParamsMap() {
    Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.putAll(SecurityHeaderCustomizer.getSecurityParams());
    paramsMap.putAll(DateToolCustomizer.getDateParams());
    return paramsMap;
  }
}
