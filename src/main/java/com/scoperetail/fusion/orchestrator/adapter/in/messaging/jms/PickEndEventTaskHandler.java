package com.scoperetail.fusion.orchestrator.adapter.in.messaging.jms;

import javax.xml.validation.Schema;

import org.springframework.stereotype.Component;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import com.scoperetail.fusion.messaging.schema.pick.end.PickingSubSystemOrderCompleteMessage;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.PickEndUseCase;

@Component
public class PickEndEventTaskHandler extends AbstractMessageListener {

	private final PickEndUseCase pickEndUseCase;

	public PickEndEventTaskHandler(final MessageRouterReceiver messageRouterReceiver, final Schema pickEndXmlSchema,
			final PickEndUseCase pickEndUseCase) {
		super("fusionBroker", "MCS.PICK.OUT", MessageType.XML, pickEndXmlSchema, "PickingSubSystemOrderCompleteMessage",
				messageRouterReceiver);
		this.pickEndUseCase = pickEndUseCase;
	}

	@Override
	protected void handleMessage(final Object event, final boolean isValid) throws Exception {
		pickEndUseCase.endPick(event, isValid);
	}

	@Override
	protected Class getClazz() {
		return PickingSubSystemOrderCompleteMessage.class;
	}
}
