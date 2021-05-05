package com.scoperetail.fusion.orchestrator.application.port.out.jms;

import java.io.IOException;

import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;

public interface PosterOutboundPort {

  ModelApiResponse post(Object object) throws IOException;
}
