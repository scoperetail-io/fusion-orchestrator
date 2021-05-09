package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import javax.xml.validation.Schema;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.messaging.schema.pick.begin.PickingOrderBeginEventMessage;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PickBeginUseCase;

@Component
public class PickBeginEventTaskHandler extends AbstractMessageListener {

	private final PickBeginUseCase pickBeginUseCase;

	public PickBeginEventTaskHandler(final MessageRouterReceiver messageRouterReceiver, final Schema pickBeginXmlSchema,
			final PickBeginUseCase pickBeginUseCase) {
		super("mcsBroker", "MCS.PICK.OUT", MessageType.XML, pickBeginXmlSchema, messageRouterReceiver,
				"pickingOrderBeginEventMessage");
		this.pickBeginUseCase = pickBeginUseCase;
	}

	@Override
	protected void handleMessage(final Object event, final boolean isValid) throws Exception {
		pickBeginUseCase.beginPick(event, isValid);
	}

	@Override
	protected Class getClazz() {
		return PickingOrderBeginEventMessage.class;
	}
}
