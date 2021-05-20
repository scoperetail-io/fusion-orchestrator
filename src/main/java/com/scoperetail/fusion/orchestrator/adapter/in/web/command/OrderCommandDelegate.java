package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

import static com.scoperetail.fusion.shared.kernel.events.Event.OrderDropEvent;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.scoperetail.fusion.orchestrator.adapter.in.web.OrdersApiDelegate;
import com.scoperetail.fusion.orchestrator.adapter.out.persistence.jpa.DedupeJpaAdapter;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.common.HashUtil;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.OrderDropEvent;

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
		HttpStatus result = CONFLICT;
		if (isNotDuplicate(orderDropEventRequest)) {
			try {
				posterUseCase.post(OrderDropEvent, orderDropEventRequest, true);
				result = ACCEPTED;
			} catch (final Exception e) {
				result = INTERNAL_SERVER_ERROR;
				log.error("Exception occurred during processing event {} exception is: {}", OrderDropEvent, e);
			}
		}
		return buildResponseEntity(result);
	}

	private boolean isNotDuplicate(final OrderDropEvent orderDropEventRequest) {
		String storeNbr = orderDropEventRequest.getRoutingInfo().getDestinationNode().getNodeID();
		Long orderNbr = orderDropEventRequest.getOrder().getFulfillmentOrder().getOrderNbr();
		String logKey = OrderDropEvent.name() + storeNbr + orderNbr.toString();
		return dedupeJpaAdapter.isNotDuplicate(HashUtil.getHash(logKey, HashUtil.SHA3_512));
	}

	private ResponseEntity<ModelApiResponse> buildResponseEntity(final HttpStatus httpStatus) {
		final ModelApiResponse response = new ModelApiResponse();
		response.setCode(httpStatus.value());
		response.setMessage(httpStatus.getReasonPhrase());
		response.setType(httpStatus.name());
		return ResponseEntity.status(httpStatus).body(response);
	}
}