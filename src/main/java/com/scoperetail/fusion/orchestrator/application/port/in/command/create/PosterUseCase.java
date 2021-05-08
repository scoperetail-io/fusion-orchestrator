package com.scoperetail.fusion.orchestrator.application.port.in.command.create;

import com.scoperetail.fusion.shared.kernel.events.Event;

public interface PosterUseCase {

	void post(Event event, Object domainEntity, boolean isValid) throws Exception;
}
