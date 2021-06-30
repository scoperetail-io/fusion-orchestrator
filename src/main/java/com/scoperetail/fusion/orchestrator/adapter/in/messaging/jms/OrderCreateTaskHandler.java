/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import javax.xml.validation.Schema;
import org.springframework.stereotype.Component;
import com.scoperetail.fusion.core.adapter.in.messaging.jms.AbstractMessageListener;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderCreateUseCase;

@Component
public class OrderCreateTaskHandler extends AbstractMessageListener {

  private final OrderCreateUseCase orderCreateUseCase;

  public OrderCreateTaskHandler(final MessageRouterReceiver messageRouterReceiver,
      final Schema orderMessageXmlSchema, final OrderCreateUseCase orderCreateUseCase) {
    super("fusionBroker", "FUSION.ORDER.IN.QUEUE", MessageType.XML, orderMessageXmlSchema, "orderMessage",
        messageRouterReceiver);
    this.orderCreateUseCase = orderCreateUseCase;
  }

  @Override
  protected void handleMessage(final Object event, final boolean isValid) throws Exception {
    orderCreateUseCase.handleJmsEvent(event, isValid);
  }
}
