package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.shared.kernel.events.Event.PickEndEvent;

import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PickEndUseCase;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;

import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class PickEndService implements PickEndUseCase {

	private PosterUseCase posterUseCase;

	@Override
	public void endPick(final Object event, final boolean isValid) throws Exception {
		posterUseCase.post(PickEndEvent, event, isValid);
	}

}
