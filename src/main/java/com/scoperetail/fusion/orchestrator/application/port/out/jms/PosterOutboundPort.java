package com.scoperetail.fusion.orchestrator.application.port.out.jms;

import java.io.IOException;

import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;
import com.scoperetail.fusion.orchestrator.domain.events.DomainEvent;

public interface PosterOutboundPort {

	boolean post(DomainEvent domainEvent, UsecaseResult usecaseResult) throws IOException;
}
