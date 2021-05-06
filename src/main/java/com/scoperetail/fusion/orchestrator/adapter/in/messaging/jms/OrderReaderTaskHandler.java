package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.orchestrator.application.port.in.query.OrderReaderUseCase;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class OrderReaderTaskHandler implements MessageListener<String> {

	MessageRouterReceiver messageRouterReceiver;

	OrderReaderUseCase orderReaderUseCase;

	@PostConstruct
	private void initialize() {
		messageRouterReceiver.registerListener("scopeBroker", "ORDER.DROP.READER", this);
	}

	@Override
	public TaskResult doTask(String message) {
		try {
			boolean result = orderReaderUseCase.readOrder(message);
		} catch (Exception e) {
			log.error("OrderReaderTaskHandler ERROR{}", e);
		}
		return TaskResult.SUCCESS;
	}

}
