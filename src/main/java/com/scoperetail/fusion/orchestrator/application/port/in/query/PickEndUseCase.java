package com.scoperetail.fusion.orchestrator.application.port.in.query;

public interface PickEndUseCase {

	boolean endPick(Object event, boolean isValid);
}
