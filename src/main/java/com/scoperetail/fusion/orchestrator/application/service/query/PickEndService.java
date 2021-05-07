package com.scoperetail.fusion.orchestrator.application.service.query;

import static com.scoperetail.fusion.shared.kernel.events.Event.PickEndEvent;

import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.orchestrator.application.port.in.query.PickEndUseCase;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@AllArgsConstructor
@Slf4j
public class PickEndService implements PickEndUseCase {

	private PosterUseCase posterUseCase;

	@Override
	public boolean endPick(Object event, boolean isValid) {
		return posterUseCase.post(PickEndEvent, event, isValid);
	}

}
