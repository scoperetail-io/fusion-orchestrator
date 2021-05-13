package com.scoperetail.fusion.orchestrator.application.service.transform;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.Map;

import com.scoperetail.fusion.shared.kernel.events.Event;

public interface Transformer {

	String DOMAIN_ENTITY = "DOMAIN_ENTITY";

	default String transform(final Event event, final Map<String, Object> params, final String templateName)
			throws Exception {
		return EMPTY;
	}
}
