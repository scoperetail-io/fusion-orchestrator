package com.scoperetail.fusion.orchestrator.application.port.in.command.create;

import com.scoperetail.fusion.orchestrator.domain.OrderCreateRequest;

public interface OrderCreateUseCase {
  void handleRestEvent(OrderCreateRequest orderCreateRequest) throws Exception;

  void doValidationFailure(String event);

  void doHandleMessage(Object event) throws Exception;

  void doHandleFailure(String event);
}
