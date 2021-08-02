package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaREST;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.scoperetail.fusion.core.adapter.in.web.command.AbstractBaseDelegate;
import com.scoperetail.fusion.core.application.port.in.command.DuplicateCheckUseCase;
import com.scoperetail.fusion.orchestrator.adapter.in.web.OrdersApiDelegate;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderCreateUseCase;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.OrderCreateRequest;

@Component
public class OrderCreateCommandDelegate extends AbstractBaseDelegate implements OrdersApiDelegate {

  private final OrderCreateUseCase orderCreateUseCase;

  public OrderCreateCommandDelegate(
      final DuplicateCheckUseCase duplicateCheckUseCase,
      final OrderCreateUseCase orderCreateUseCase) {
    super(duplicateCheckUseCase);
    this.orderCreateUseCase = orderCreateUseCase;
  }

  @Override
  public ResponseEntity<ModelApiResponse> orderCreate(final OrderCreateRequest orderCreateRequest) {
    final HttpStatus result = doEvent(OrderCreateViaREST.name(), orderCreateRequest);
    return buildResponseEntity(result);
  }

  @Override
  protected HttpStatus processEvent(final Object domainEntity) throws Exception {
    orderCreateUseCase.handleRestEvent((OrderCreateRequest) domainEntity);
    return HttpStatus.ACCEPTED;
  }

  private ResponseEntity<ModelApiResponse> buildResponseEntity(final HttpStatus httpStatus) {
    final ModelApiResponse response = new ModelApiResponse();
    response.setCode(httpStatus.value());
    response.setMessage(httpStatus.getReasonPhrase());
    response.setType(httpStatus.name());
    return ResponseEntity.status(httpStatus).body(response);
  }
}
