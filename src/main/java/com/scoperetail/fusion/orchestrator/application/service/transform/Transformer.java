package com.scoperetail.fusion.orchestrator.application.service.transform;

import static org.apache.commons.lang.StringUtils.EMPTY;

import com.scoperetail.fusion.shared.kernel.events.Event;

public interface Transformer {

	default String transform(Event event, Object object) throws Exception {
		return EMPTY;
	}

	default String transform(Event event, Object object, String templateName) {
		return EMPTY;
	}
}
