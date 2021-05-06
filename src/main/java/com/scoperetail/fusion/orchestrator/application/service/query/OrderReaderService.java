package com.scoperetail.fusion.orchestrator.application.service.query;

import static com.scoperetail.fusion.shared.kernel.events.Event.OrderDropEventReader;

import java.io.IOException;
import java.util.Optional;

import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.in.query.OrderReaderUseCase;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.OrderDropEvent;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;
import com.scoperetail.fusion.shared.kernel.events.DomainEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@AllArgsConstructor
@Slf4j
public class OrderReaderService implements OrderReaderUseCase {

	PosterUseCase posterUseCase;

	@Override
	public boolean readOrder(String message) throws IOException {
		OrderDropEvent orderDropEvent = unmarshal(message);
		posterUseCase.post(OrderDropEventReader, orderDropEvent);
		return false;
	}

	private OrderDropEvent unmarshal(String message) throws IOException {
		DomainEvent domainEvent = JsonUtils.unmarshal(Optional.ofNullable(message),
				DomainEvent.class.getCanonicalName());
		return JsonUtils.unmarshal(Optional.ofNullable(domainEvent.getPayload()),
				OrderDropEvent.class.getCanonicalName());
	}

}
