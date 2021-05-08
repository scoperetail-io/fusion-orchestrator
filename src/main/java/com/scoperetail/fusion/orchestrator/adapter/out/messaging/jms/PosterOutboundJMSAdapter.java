package com.scoperetail.fusion.orchestrator.adapter.out.messaging.jms;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterSender;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundPort;
import com.scoperetail.fusion.shared.kernel.common.annotation.MessagingAdapter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@MessagingAdapter
@AllArgsConstructor
@Slf4j
public class PosterOutboundJMSAdapter implements PosterOutboundPort {

	private MessageRouterSender messageSender;

	@Override
	public void post(final String brokerId, final String queueName, final String payload) {
		messageSender.send(brokerId, queueName, payload);
	}

}
