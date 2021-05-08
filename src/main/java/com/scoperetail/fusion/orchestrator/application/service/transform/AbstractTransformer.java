package com.scoperetail.fusion.orchestrator.application.service.transform;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.scoperetail.fusion.orchestrator.common.JsonUtils;
import com.scoperetail.fusion.orchestrator.domain.helper.DomainHelper;
import com.scoperetail.fusion.shared.kernel.events.Event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public abstract class AbstractTransformer implements Transformer {

	protected DomainHelper domainHelper;

	protected final String getTextFromTemplate(final Event event, final Object entity, final String template) {
		return domainHelper.generateTextFromTemplate(event, entity, template);
	}

	protected final Map<String, String> getkeyMap(final String keyJson) throws IOException {
		final Map<String, String> keyMap = JsonUtils.unmarshal(Optional.of(keyJson), Map.class.getCanonicalName());
		log.trace("Key map: {}", keyMap);
		return keyMap;
	}

}
