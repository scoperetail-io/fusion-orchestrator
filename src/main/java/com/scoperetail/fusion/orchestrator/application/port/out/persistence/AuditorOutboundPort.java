package com.scoperetail.fusion.orchestrator.application.port.out.persistence;

import com.scoperetail.fusion.shared.kernel.events.DomainEvent;

public interface AuditorOutboundPort {
  Boolean insertIfNotExist(DomainEvent domainEvent);
}
