package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaREST;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.scoperetail.fusion.orchestrator.adapter.in.web.OrdersApiDelegate;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderCreateUseCase;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.OrderCreateRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class OrderCreateCommandDelegate implements OrdersApiDelegate {

  private OrderCreateUseCase orderCreateUseCase;

  @Override
  public ResponseEntity<ModelApiResponse> orderCreate(OrderCreateRequest orderCreateRequest) {
    HttpStatus result = CONFLICT;
    if (orderCreateUseCase.isNotDuplicate(orderCreateRequest)) {
      try {
        orderCreateUseCase.handleRestEvent(orderCreateRequest);
        result = ACCEPTED;
      } catch (final Exception e) {
        result = INTERNAL_SERVER_ERROR;
        log.error("Exception occurred during processing event {} exception is: {}",
            OrderCreateViaREST, e);
      }
    }
    return buildResponseEntity(result);
  }

  private ResponseEntity<ModelApiResponse> buildResponseEntity(final HttpStatus httpStatus) {
    final ModelApiResponse response = new ModelApiResponse();
    response.setCode(httpStatus.value());
    response.setMessage(httpStatus.getReasonPhrase());
    response.setType(httpStatus.name());
    return ResponseEntity.status(httpStatus).body(response);
  }
}
