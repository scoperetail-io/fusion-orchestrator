package com.scoperetail.fusion.orchestrator.application.port.in.query;

public interface OrderReaderUseCase {

	boolean readOrder(Object event, boolean isValid);
}
