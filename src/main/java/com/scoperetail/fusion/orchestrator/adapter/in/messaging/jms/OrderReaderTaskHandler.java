/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderReaderUseCase;
import com.scoperetail.fusion.shared.kernel.events.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderReaderTaskHandler extends AbstractMessageListener {

  private final OrderReaderUseCase orderReaderUseCase;

  public OrderReaderTaskHandler(
      final MessageRouterReceiver messageRouterReceiver,
      final OrderReaderUseCase orderReaderUseCase) {
    super(
        "fusionBroker", "FUSION.ORDER.IN", MessageType.JSON, null, null, messageRouterReceiver);
    this.orderReaderUseCase = orderReaderUseCase;
  }

  @Override
  protected void handleMessage(final Object event, final boolean isValid) throws Exception {
    orderReaderUseCase.readOrder(event, isValid);
  }

  @Override
  protected Class<DomainEvent> getClazz() {
    return DomainEvent.class;
  }
}
