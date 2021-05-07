package com.scoperetail.fusion.orchestrator.application.port.out.persistence;

import com.scoperetail.fusion.orchestrator.domain.events.DomainEvent;

public interface AuditorOutboundPort {
  Boolean insertIfNotExist(DomainEvent domainEvent);
}
