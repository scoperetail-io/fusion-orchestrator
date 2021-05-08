package com.scoperetail.fusion.orchestrator.application.port.in.command.create;

public interface PickBeginUseCase {

	void beginPick(Object message, boolean isValid) throws Exception;
}
