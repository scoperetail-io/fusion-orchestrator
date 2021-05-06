package com.scoperetail.fusion.orchestrator.application.service.transform;

import com.scoperetail.fusion.shared.kernel.events.Event;

public interface Transformer {

	String transform(Event event, Object object) throws Exception;
}
