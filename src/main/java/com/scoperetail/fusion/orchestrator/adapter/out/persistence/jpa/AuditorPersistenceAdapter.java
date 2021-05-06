package com.scoperetail.fusion.orchestrator.adapter.out.persistence.jpa;

import com.scoperetail.fusion.adapter.out.persistence.jpa.repository.MessageLogRepository;
import com.scoperetail.fusion.orchestrator.application.port.out.persistence.AuditorOutboundPort;
import com.scoperetail.fusion.orchestrator.domain.events.DomainEvent;
import com.scoperetail.fusion.orchestrator.domain.shared.kernel.common.annotation.PersistenceAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PersistenceAdapter
@AllArgsConstructor
@Slf4j
public class AuditorPersistenceAdapter implements AuditorOutboundPort {
  private MessageLogRepository repository;

  @Override
  public Boolean insertIfNotExist(DomainEvent domainEvent) {
    return repository.insertIfNotExist(
        domainEvent.getEventId(),
        domainEvent.getEvent().name(),
        domainEvent.getTimestamp(),
        domainEvent.getPayload(),
        1) > 0;
  }
}
