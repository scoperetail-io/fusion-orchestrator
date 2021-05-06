package com.scoperetail.fusion.orchestrator.application.port.in.command.create;

import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.shared.kernel.events.Event;

public interface PosterUseCase {

	ModelApiResponse post(Event event, Object domainEntity);
}
