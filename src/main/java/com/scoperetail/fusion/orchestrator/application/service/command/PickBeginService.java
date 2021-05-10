package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.shared.kernel.events.Event.PickBeginEvent;

import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PickBeginUseCase;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;

import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class PickBeginService implements PickBeginUseCase {

	private PosterUseCase posterUseCase;

	@Override
	public void beginPick(final Object event, final boolean isValid) throws Exception {
		posterUseCase.post(PickBeginEvent, event, isValid);
	}

}
