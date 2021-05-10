package com.scoperetail.fusion.orchestrator.application.port.in.command.create;

public interface PickEndUseCase {

	void endPick(Object event, boolean isValid) throws Exception;
}
