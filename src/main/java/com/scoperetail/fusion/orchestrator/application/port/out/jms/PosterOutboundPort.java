package com.scoperetail.fusion.orchestrator.application.port.out.jms;

public interface PosterOutboundPort {

	void post(String brokerId, String queueName, String payload);
}
