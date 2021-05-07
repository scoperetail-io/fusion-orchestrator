package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.orchestrator.application.port.in.query.OrderReaderUseCase;
import com.scoperetail.fusion.shared.kernel.events.DomainEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderReaderTaskHandler extends AbstractMessageListener {

	OrderReaderUseCase orderReaderUseCase;

	public OrderReaderTaskHandler(MessageRouterReceiver messageRouterReceiver, OrderReaderUseCase orderReaderUseCase) {
		super("scopeBroker", "ORDER.DROP.OUTBOUND", MessageType.JSON, null, messageRouterReceiver);
		this.orderReaderUseCase = orderReaderUseCase;
	}

	@Override
	protected boolean handleMessage(Object event, boolean isValid) {
		return orderReaderUseCase.readOrder(event, isValid);
	}

	@Override
	protected Class getClazz() {
		return DomainEvent.class;
	}

}
