/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.application.port.out.jms;

public interface PosterOutboundJmsPort {

  void post(String brokerId, String queueName, String payload);
}
