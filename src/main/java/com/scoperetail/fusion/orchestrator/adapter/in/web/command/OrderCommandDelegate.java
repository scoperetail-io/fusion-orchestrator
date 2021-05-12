package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

import static com.scoperetail.fusion.shared.kernel.events.Event.OrderDropEvent;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.scoperetail.fusion.orchestrator.adapter.out.persistence.jpa.DedupeJpaAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.scoperetail.fusion.orchestrator.adapter.in.web.OrdersApiDelegate;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.OrderDropEvent;
import com.scoperetail.fusion.orchestrator.common.HashUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class OrderCommandDelegate implements OrdersApiDelegate {

	private PosterUseCase posterUseCase;
	private DedupeJpaAdapter dedupeJpaAdapter;

	@Override
	public ResponseEntity<ModelApiResponse> orderDropEvent(final OrderDropEvent orderDropEventRequest) {
		boolean result = false;
		try {
			String storeNbr = orderDropEventRequest.getOrder().getFulfillmentOrder().getDestinationBusinessUnit().getDestDivisonNumber();
			Long orderNbr = orderDropEventRequest.getOrder().getFulfillmentOrder().getOrderNbr();
			String logKey = OrderDropEvent.name() + storeNbr + orderNbr.toString();
			// todo: If it is duplicate return status HTTP 409
      if (dedupeJpaAdapter.isNotDuplicate(HashUtil.getHash(logKey))) {
        posterUseCase.post(OrderDropEvent, orderDropEventRequest, true);
        result = true;
			}
		} catch (final Exception e) {
			log.error("Exception occurred during processing event {} exception is: {}", OrderDropEvent, e);
		}
		return ResponseEntity.ok(buildResult(result));
	}

	private ModelApiResponse buildResult(final boolean result) {
		final HttpStatus httpStatus = result ? ACCEPTED : INTERNAL_SERVER_ERROR;
		final ModelApiResponse response = new ModelApiResponse();
		response.setCode(httpStatus.value());
		response.setMessage(httpStatus.getReasonPhrase());
		response.setType(httpStatus.name());
		return response;
	}

}