/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import javax.xml.validation.Schema;
import org.springframework.stereotype.Component;
import com.scoperetail.fusion.core.adapter.in.messaging.jms.AbstractMessageListener;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.messaging.schema.order.OrderMessage;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderCreateUseCase;

@Component
public class OrderCreateTaskHandler extends AbstractMessageListener {

  private final OrderCreateUseCase orderCreateUseCase;

  public OrderCreateTaskHandler(
      final MessageRouterReceiver messageRouterReceiver,
      final Schema orderMessageXmlSchema,
      final OrderCreateUseCase orderCreateUseCase) {
    super(
        "fusionBroker",
        "FUSION.ORDER.IN.QUEUE",
        MessageType.XML,
        orderMessageXmlSchema,
        "orderMessage",
        messageRouterReceiver);
    this.orderCreateUseCase = orderCreateUseCase;
  }

  @Override
  public void handleValidationFailure(final String event) throws Exception {
    orderCreateUseCase.doValidationFailure(event);
  }

  @Override
  protected void handleMessage(final Object event) throws Exception {
    orderCreateUseCase.doHandleMessage(event);
  }

  @Override
  public void handleFailure(final String event) {
    orderCreateUseCase.doHandleFailure(event);
  }

  @Override
  protected Class<OrderMessage> getClazz() {
    return OrderMessage.class;
  }
}
