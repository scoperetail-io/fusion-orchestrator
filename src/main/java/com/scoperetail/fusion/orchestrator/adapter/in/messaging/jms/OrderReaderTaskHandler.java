package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderReaderUseCase;
import com.scoperetail.fusion.shared.kernel.events.DomainEvent;

@Component
public class OrderReaderTaskHandler extends AbstractMessageListener {

	private final OrderReaderUseCase orderReaderUseCase;

	public OrderReaderTaskHandler(final MessageRouterReceiver messageRouterReceiver,
			final OrderReaderUseCase orderReaderUseCase) {
		super("fusionBroker", "ORDER.DROP.OUTBOUND", MessageType.JSON, null, null, messageRouterReceiver);
		this.orderReaderUseCase = orderReaderUseCase;
	}

	@Override
	protected void handleMessage(final Object event, final boolean isValid) throws Exception {
		orderReaderUseCase.readOrder(event, isValid);
	}

	@Override
	protected Class getClazz() {
		return DomainEvent.class;
	}

}
