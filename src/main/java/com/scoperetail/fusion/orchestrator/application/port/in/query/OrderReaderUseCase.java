package com.scoperetail.fusion.orchestrator.application.port.in.query;

import java.io.IOException;

public interface OrderReaderUseCase {

	boolean readOrder(String message) throws IOException;
}
