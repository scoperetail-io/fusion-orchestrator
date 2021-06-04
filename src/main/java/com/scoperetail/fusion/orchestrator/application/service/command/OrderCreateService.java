package com.scoperetail.fusion.orchestrator.application.service.command;

import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaREST;
import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaJMS;
import com.scoperetail.fusion.core.adapter.out.persistence.jpa.DedupeJpaAdapter;
import com.scoperetail.fusion.core.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.core.common.HashUtil;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderCreateUseCase;
import com.scoperetail.fusion.orchestrator.domain.OrderCreateRequest;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class OrderCreateService implements OrderCreateUseCase {

  private PosterUseCase posterUseCase;
  private DedupeJpaAdapter dedupeJpaAdapter;

  @Override
  public void handleRestEvent(OrderCreateRequest orderCreateRequest) throws Exception {
    posterUseCase.post(OrderCreateViaREST.name(), orderCreateRequest, true);
  }

  @Override
  public void handleJmsEvent(final Object event, final boolean isValid) throws Exception {
    posterUseCase.post(OrderCreateViaJMS.name(), event, isValid);
  }

  @Override
  public boolean isNotDuplicate(OrderCreateRequest orderCreateRequest) {
    final String logKey = orderCreateRequest.getRequestId();
    return dedupeJpaAdapter.isNotDuplicate(HashUtil.getHash(logKey, HashUtil.SHA3_512));
  }
}
