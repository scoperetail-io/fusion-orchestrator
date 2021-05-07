package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

import static com.scoperetail.fusion.shared.kernel.events.Event.OrderDropEvent;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;
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
		boolean result = posterUseCase.post(OrderDropEvent, orderDropEventRequest, true);
		return ResponseEntity.ok(buildResult(result));
	}

	private ModelApiResponse buildResult(boolean result) {
		HttpStatus httpStatus = result ? ACCEPTED : INTERNAL_SERVER_ERROR;
		ModelApiResponse response = new ModelApiResponse();
		response.setCode(httpStatus.value());
		response.setMessage(httpStatus.getReasonPhrase());
		response.setType(httpStatus.name());
		return response;
	}

}