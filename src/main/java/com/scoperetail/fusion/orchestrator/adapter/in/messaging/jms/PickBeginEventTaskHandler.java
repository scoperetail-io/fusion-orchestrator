/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.messaging.schema.pick.begin.PickingOrderBeginEventMessage;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PickBeginUseCase;
import javax.xml.validation.Schema;
import org.springframework.stereotype.Component;

@Component
public class PickBeginEventTaskHandler extends AbstractMessageListener {

  private final PickBeginUseCase pickBeginUseCase;

  public PickBeginEventTaskHandler(
      final MessageRouterReceiver messageRouterReceiver,
      final Schema pickBeginXmlSchema,
      final PickBeginUseCase pickBeginUseCase) {
    super(
        "fusionBroker",
        "US.FUSION.ALPHA.OUT.QUEUE",
        MessageType.XML,
        pickBeginXmlSchema,
        "pickingOrderBeginEventMessage",
        messageRouterReceiver);
    this.pickBeginUseCase = pickBeginUseCase;
  }

  @Override
  protected void handleMessage(final Object event, final boolean isValid) throws Exception {
    pickBeginUseCase.beginPick(event, isValid);
  }

  @Override
  protected Class<PickingOrderBeginEventMessage> getClazz() {
    return PickingOrderBeginEventMessage.class;
  }
}
