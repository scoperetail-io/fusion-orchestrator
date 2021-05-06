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

	protected final String getKeyJson(Event event, Object entity, String template) {
		String keyJson = domainHelper.generateTextFromTemplate(event, entity, template);
		log.trace("Hash key text generated {}", keyJson);
		return keyJson;
	}

	protected final Map<String, String> getkeyMap(String keyJson) throws IOException {
		Map<String, String> keyMap = JsonUtils.unmarshal(Optional.of(keyJson), Map.class.getCanonicalName());
		log.trace("Hash key text unmarshaled {}", keyJson);
		return keyMap;
	}

}
