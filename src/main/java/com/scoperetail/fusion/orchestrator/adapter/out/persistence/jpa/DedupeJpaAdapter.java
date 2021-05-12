package com.scoperetail.fusion.orchestrator.adapter.out.persistence.jpa;

import com.scoperetail.fusion.adapter.out.persistence.jpa.repository.DedupeKeyRepository;
import com.scoperetail.fusion.orchestrator.application.port.out.persistence.DedupeOutboundPort;
import com.scoperetail.fusion.shared.kernel.common.annotation.PersistenceAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PersistenceAdapter
@AllArgsConstructor
@Slf4j
public class DedupeJpaAdapter implements DedupeOutboundPort {
  private DedupeKeyRepository dedupeKeyRepository;

  @Override
  public Boolean isNotDuplicate(String logKey) {
    return dedupeKeyRepository.insertIfNotExist(logKey) > 0;
  }
}
