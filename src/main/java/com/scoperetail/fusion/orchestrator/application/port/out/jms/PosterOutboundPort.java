package com.scoperetail.fusion.orchestrator.application.port.out.jms;

public interface PosterOutboundPort {

	boolean post(String brokerId, String queueName, String payload);
}
