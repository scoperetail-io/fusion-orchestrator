package com.scoperetail.fusion.orchestrator.application.service.transform.impl;

import static com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper.HASH_KEY_TEMPLATE;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.orchestrator.application.service.transform.AbstractTransformer;
import com.scoperetail.fusion.orchestrator.common.HashUtil;
import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.DomainEvent;
import com.scoperetail.fusion.shared.kernel.events.Event;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DomainToDomainEventJsonTransformer extends AbstractTransformer {

	public DomainToDomainEventJsonTransformer(final DomainHelper domainHelper) {
		super(domainHelper);
	}

	@Override
	public String transform(final Event event, final Object domainEntity, final String templateName) throws Exception {
		String keyHash = null;
		Map<String, String> keyMap = null;
		log.trace("Event: {}", event);
		final String payload = JsonUtils.marshal(Optional.ofNullable(domainEntity));
		log.trace("Marshalled domain entity: {}", payload);
		final String keyJson = getTextFromTemplate(event, domainEntity, HASH_KEY_TEMPLATE);
		keyMap = getkeyMap(keyJson);
		keyHash = HashUtil.getHash(keyJson);

		final DomainEvent domainEvent = DomainEvent.builder().eventId(keyHash).event(event).keyMap(keyMap)
				.payload(payload).build();
		return JsonUtils.marshal(Optional.ofNullable(domainEvent));
	}
}
