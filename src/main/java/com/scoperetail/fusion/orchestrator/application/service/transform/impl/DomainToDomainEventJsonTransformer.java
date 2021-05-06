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

	public DomainToDomainEventJsonTransformer(DomainHelper domainHelper) {
		super(domainHelper);
	}

	@Override
	public String transform(Event event, Object entity) throws Exception {
		String keyHash = null;
		Map<String, String> keyMap = null;
		log.trace("Processing message {}", event);
		String payload = JsonUtils.marshal(Optional.ofNullable(entity));
		log.trace("Marshalled message {}", event);
		String keyJson = getTextFromTemplate(event, entity, HASH_KEY_TEMPLATE);
		keyMap = getkeyMap(keyJson);
		keyHash = HashUtil.getHash(keyJson);
		log.trace("Created hash of hash key {}", event);

		DomainEvent domainEvent = DomainEvent.builder().eventId(keyHash).event(event).keyMap(keyMap).payload(payload)
				.build();
		return JsonUtils.marshal(Optional.ofNullable(domainEvent));
	}

}
