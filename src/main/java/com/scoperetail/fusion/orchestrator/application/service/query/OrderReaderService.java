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

	private PosterUseCase posterUseCase;

	@Override
	public boolean readOrder(Object message, boolean isValid) {
		boolean result = false;
		try {
			OrderDropEvent orderDropEvent = unmarshal(message);
			result = posterUseCase.post(OrderDropEventReader, orderDropEvent, isValid);
		} catch (IOException e) {
			log.error("OrderReaderService:: {}", e);
		}
		return result;
	}

	private OrderDropEvent unmarshal(Object message) throws IOException {
		DomainEvent domainEvent = JsonUtils.unmarshal(Optional.ofNullable(message.toString()),
				DomainEvent.class.getCanonicalName());
		return JsonUtils.unmarshal(Optional.ofNullable(domainEvent.getPayload()),
				OrderDropEvent.class.getCanonicalName());
	}

}
