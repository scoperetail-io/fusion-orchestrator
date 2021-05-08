package com.scoperetail.fusion.orchestrator.application.service.transform;

import static org.apache.commons.lang.StringUtils.EMPTY;

import com.scoperetail.fusion.shared.kernel.events.Event;

public interface Transformer {

	default String transform(final Event event, final Object domainEntity) throws Exception {
		return EMPTY;
	}

	default String transform(final Event event, final Object domainEntity, final String templateName) {
		return EMPTY;
	}
}
