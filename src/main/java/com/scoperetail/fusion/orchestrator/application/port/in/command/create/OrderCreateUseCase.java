package com.scoperetail.fusion.orchestrator.application.port.in.command.create;

import com.scoperetail.fusion.orchestrator.domain.OrderCreateRequest;

public interface OrderCreateUseCase {
  void handleRestEvent(OrderCreateRequest orderCreateRequest) throws Exception;

  boolean isNotDuplicate(OrderCreateRequest orderCreateRequest);

  void handleJmsEvent(Object event, boolean isValid) throws Exception;
}
