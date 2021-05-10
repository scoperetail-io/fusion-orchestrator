package com.scoperetail.fusion.orchestrator.application.port.in.command.create;

public interface OrderReaderUseCase {

	void readOrder(Object event, boolean isValid) throws Exception;
}
