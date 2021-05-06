package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

import static com.scoperetail.fusion.shared.kernel.events.Event.OrderDropEvent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.scoperetail.fusion.orchestrator.adapter.in.web.OrdersApiDelegate;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.OrderDropEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class OrderCommandDelegate implements OrdersApiDelegate {

	private PosterUseCase posterUseCase;

	@Override
	public ResponseEntity<ModelApiResponse> orderDropEvent(OrderDropEvent orderDropEventRequest) {
		ModelApiResponse response = posterUseCase.post(OrderDropEvent, orderDropEventRequest);
		return ResponseEntity.ok(response);
	}

}