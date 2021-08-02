package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaJMS;
import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaREST;
import com.scoperetail.fusion.core.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderCreateUseCase;
import com.scoperetail.fusion.orchestrator.domain.OrderCreateRequest;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@AllArgsConstructor
@Slf4j
public class OrderCreateService implements OrderCreateUseCase {

  private PosterUseCase posterUseCase;

  @Override
  public void handleRestEvent(final OrderCreateRequest orderCreateRequest) throws Exception {
    posterUseCase.post(OrderCreateViaREST.name(), orderCreateRequest, true);
  }

  @Override
  public void doValidationFailure(final String event) {
    try {
      posterUseCase.post(OrderCreateViaJMS.name(), event, false);
    } catch (final Exception e) {
      log.error(
          "An exception occured while executing doValidationFailure() for Usecase: {},  Exception: {}",
          OrderCreateViaJMS.name(),
          e);
    }
  }

  @Override
  public void doHandleMessage(final Object event) throws Exception {
    posterUseCase.post(OrderCreateViaJMS.name(), event, true);
  }

  @Override
  public void doHandleFailure(final String event) {
    //BO
    log.error("Unable to process event: {}", event);
  }
}
