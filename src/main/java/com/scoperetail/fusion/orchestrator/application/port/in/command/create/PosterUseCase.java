package com.scoperetail.fusion.orchestrator.application.port.in.command.create;

import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;

public interface PosterUseCase {
  //For the output DTO we have 2 choices, 
  //1. either to use the domain entity or 
  //2. to use a new DTO. 
  ModelApiResponse post(Object entity);
}

