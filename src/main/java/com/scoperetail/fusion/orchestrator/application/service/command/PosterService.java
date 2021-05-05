package com.scoperetail.fusion.orchestrator.application.service.command;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.out.jms.PosterOutboundPort;
import com.scoperetail.fusion.orchestrator.common.HashUtil;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.events.DomainEvent;
import com.scoperetail.fusion.orchestrator.domain.events.Event;
import com.scoperetail.fusion.orchestrator.domain.shared.kernel.common.annotation.UseCase;
import com.scoperetail.fusion.orchestrator.domain.shared.kernel.helper.DomainHelper;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
class PosterService implements PosterUseCase {

	private final PosterOutboundPort posterOutboundPort;

	private final DomainHelper domainHelper;

	@Override
	public ModelApiResponse post(final Object entity) {
		ModelApiResponse result = null;
		try {
			String payload = JsonUtils.marshal(Optional.ofNullable(entity));
			String keyText = domainHelper.getHashKey(entity);
			Map<String, String> keyMap = JsonUtils.unmarshal(Optional.of(keyText), Map.class.getCanonicalName());
			String keyHash = HashUtil.getHash(keyText);
			DomainEvent domainEvent = DomainEvent.builder().eventId(keyHash).eventName(Event.ORDER_DROP).keyMap(keyMap)
					.payload(payload).build();
			result = posterOutboundPort.post(domainEvent);
		} catch (Exception e) {
			result = new ModelApiResponse();
			result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			result.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			result.setType(HttpStatus.INTERNAL_SERVER_ERROR.name());
		}
		return result;
	}
}
