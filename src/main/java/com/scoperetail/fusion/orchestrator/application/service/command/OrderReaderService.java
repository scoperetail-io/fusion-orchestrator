package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.shared.kernel.events.Event.OrderDropEventReader;

import java.io.IOException;
import java.util.Optional;

import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderReaderUseCase;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.OrderDropEvent;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;
import com.scoperetail.fusion.shared.kernel.events.DomainEvent;

import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class OrderReaderService implements OrderReaderUseCase {

	private PosterUseCase posterUseCase;

	@Override
	public void readOrder(final Object message, final boolean isValid) throws Exception {
		final OrderDropEvent orderDropEvent = unmarshal(message);
		posterUseCase.post(OrderDropEventReader, orderDropEvent, isValid);
	}

	private OrderDropEvent unmarshal(final Object message) throws IOException {
		final DomainEvent domainEvent = JsonUtils.unmarshal(Optional.ofNullable(message.toString()),
				DomainEvent.class.getCanonicalName());
		return JsonUtils.unmarshal(Optional.ofNullable(domainEvent.getPayload()),
				OrderDropEvent.class.getCanonicalName());
	}

}
