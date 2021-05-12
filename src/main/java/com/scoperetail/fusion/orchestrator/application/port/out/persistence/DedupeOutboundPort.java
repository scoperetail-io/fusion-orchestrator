package com.scoperetail.fusion.orchestrator.application.port.out.persistence;

public interface DedupeOutboundPort {
  Boolean isNotDuplicate(String logKey);
}
