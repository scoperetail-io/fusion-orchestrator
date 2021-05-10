/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.adapter.out.web;

import java.util.Map;

public interface PosterOutboundHttpAdapter {

  void post(String url, String methodType, String requestBody, Map<String, String> httpHeaders);
}
