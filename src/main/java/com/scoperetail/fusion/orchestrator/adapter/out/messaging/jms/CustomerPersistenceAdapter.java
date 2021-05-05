package com.scoperetail.fusion.orchestrator.adapter.out.messaging.jms;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageSender;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundPort;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.shared.kernel.common.annotation.MessagingAdapter;

import lombok.AllArgsConstructor;

@MessagingAdapter
@AllArgsConstructor
class PosterOutboundJMSAdapter implements PosterOutboundPort {

	private static final String ORDER_DROP_IN = "ORDER.DROP.IN";

	private MessageSender messageSender;
	
	@Override
	public ModelApiResponse post(final Object object) throws IOException {
		System.out.println("Sending to JMS Queue. Data=" + object);
		String payload = JsonUtils.marshal(Optional.ofNullable(object));
		messageSender.auditAndSend("broker1", ORDER_DROP_IN, payload);
		return buildResult();
	}

	private ModelApiResponse buildResult() {
		final ModelApiResponse result = new ModelApiResponse();
		result.setCode(HttpStatus.OK.value());
		result.setMessage(HttpStatus.OK.getReasonPhrase());
		result.setType(HttpStatus.OK.name());
		return result;
	}
}
