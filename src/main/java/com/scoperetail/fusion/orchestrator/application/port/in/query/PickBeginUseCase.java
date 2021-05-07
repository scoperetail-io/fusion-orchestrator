package com.scoperetail.fusion.orchestrator.application.port.in.query;

public interface PickBeginUseCase {

	boolean beginPick(Object message, boolean isValid);
}
