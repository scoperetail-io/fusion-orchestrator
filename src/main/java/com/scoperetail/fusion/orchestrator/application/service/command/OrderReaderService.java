package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.shared.kernel.events.Event.OrderDropEventReader;

import java.io.IOException;
import java.util.Optional;

import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderReaderUseCase;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.OrderDropEvent;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;

import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class OrderReaderService implements OrderReaderUseCase {

	private PosterUseCase posterUseCase;

	@Override
	public void readOrder(final Object message, final boolean isValid) throws Exception {
		posterUseCase.post(OrderDropEventReader, unmarshal(message), isValid);
	}

	private OrderDropEvent unmarshal(final Object message) throws IOException {
		return JsonUtils.unmarshal(Optional.ofNullable(message.toString()), OrderDropEvent.class.getCanonicalName());
	}
}
